package io.quarkus.test.services.quarkus;

import static io.quarkus.test.services.quarkus.QuarkusMavenPluginBuildHelper.findJvmArtifact;
import static io.quarkus.test.services.quarkus.QuarkusMavenPluginBuildHelper.findNativeBuildExecutable;
import static io.quarkus.test.services.quarkus.model.QuarkusProperties.isNativeEnabled;
import static io.quarkus.test.utils.FileUtils.findTargetFile;

import java.lang.annotation.Annotation;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ServiceLoader;

import io.quarkus.test.bootstrap.ManagedResource;
import io.quarkus.test.bootstrap.ServiceContext;
import io.quarkus.test.security.certificate.CertificateBuilder;
import io.quarkus.test.services.QuarkusApplication;

public class ProdQuarkusApplicationManagedResourceBuilder extends ArtifactQuarkusApplicationManagedResourceBuilder {

    static final String NATIVE_RUNNER = "-runner";
    static final String EXE = ".exe";
    static final String TARGET = "target";

    private final ServiceLoader<QuarkusApplicationManagedResourceBinding> managedResourceBindingsRegistry = ServiceLoader
            .load(QuarkusApplicationManagedResourceBinding.class);

    private Path artifact;
    private QuarkusManagedResource managedResource;
    private String artifactSuffix;

    @Override
    protected Path getArtifact() {
        return artifact;
    }

    protected void setArtifactSuffix(String suffix) {
        if (suffix == null || suffix.isEmpty() || suffix.isBlank()) {
            this.artifactSuffix = null;
        } else {
            this.artifactSuffix = suffix;
        }
    }

    @Override
    public void init(Annotation annotation) {
        QuarkusApplication metadata = (QuarkusApplication) annotation;
        setPropertiesFile(metadata.properties());
        setSslEnabled(metadata.ssl());
        setGrpcEnabled(metadata.grpc());
        initAppClasses(metadata.classes());
        initForcedDependencies(metadata.dependencies());
        setCertificateBuilder(CertificateBuilder.of(metadata.certificates()));
    }

    @Override
    public ManagedResource build(ServiceContext context) {
        setContext(context);
        configureLogging();
        configureCertificates();
        managedResource = findManagedResource();
        build();

        return managedResource;
    }

    public void build() {
        managedResource.onPreBuild();
        copyResourcesToAppFolder();
        if (managedResource.needsBuildArtifact()) {
            this.artifact = tryToReuseOrBuildArtifact();
        }

        managedResource.onPostBuild();
    }

    protected QuarkusManagedResource findManagedResource() {
        for (QuarkusApplicationManagedResourceBinding binding : managedResourceBindingsRegistry) {
            if (binding.appliesFor(getContext())) {
                return binding.init(this);
            }
        }

        return new ProdLocalhostQuarkusApplicationManagedResource(this);
    }

    protected Path getTargetFolderForLocalArtifacts() {
        return Paths.get(TARGET);
    }

    private Path tryToReuseOrBuildArtifact() {
        Optional<String> artifactLocation = Optional.empty();
        final Path targetFolder = getTargetFolderForLocalArtifacts();
        if (artifactSuffix != null) {
            return findTargetFile(targetFolder, artifactSuffix)
                    .map(Path::of)
                    .orElseThrow(() -> new IllegalStateException(String.format("Folder %s doesn't contain '%s'",
                            targetFolder,
                            artifactSuffix)));
        }
        createSnapshotOfBuildPropertiesIfNotExists();
        if (!buildPropertiesChanged()) {
            if (isNativeEnabled(getContext())) {
                // custom native executable has different name, therefore we can safely re-use it
                artifactLocation = findNativeBuildExecutable(targetFolder, requiresCustomBuild(), getApplicationFolder());
            } else if (!requiresCustomBuild()) {
                artifactLocation = findJvmArtifact(targetFolder);
            }
        }

        return artifactLocation.map(Path::of).orElseGet(this::buildArtifact);
    }

    private Path buildArtifact() {
        var quarkusMvnPluginHelper = new QuarkusMavenPluginBuildHelper(this, getTargetFolderForLocalArtifacts());
        if (isNativeEnabled(getContext())) {
            return quarkusMvnPluginHelper.buildNativeExecutable();
        }
        return quarkusMvnPluginHelper.jvmModeBuild();
    }

}
