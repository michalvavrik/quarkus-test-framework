package io.quarkus.test.services.quarkus;

import static io.quarkus.test.services.quarkus.GitRepositoryResourceBuilderUtils.cloneRepository;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ExtensionOpenShiftGitRepositoryQuarkusApplicationManagedResource
        extends ExtensionOpenShiftQuarkusApplicationManagedResource {

    public ExtensionOpenShiftGitRepositoryQuarkusApplicationManagedResource(
            GitRepositoryQuarkusApplicationManagedResourceBuilder model) {
        super(model);
    }

    @Override
    public void onPreBuild() {
        super.onPreBuild();

        cloneRepository(getModel());
    }

    @Override
    protected void withAdditionalArguments(List<String> args, QuarkusMavenPluginBuildHelper quarkusMvnPluginHelper) {
        String[] mvnArgs = StringUtils.split(getModel().getMavenArgsWithVersion(), " ");
        args.addAll(Arrays.asList(mvnArgs));
    }

    @Override
    protected void cloneProjectToServiceAppFolder() {
        // we are cloning app from git repo
    }

    @Override
    public void validate() {
        super.validate();

        if (model.requiresCustomBuild()) {
            fail("Custom source classes or forced dependencies is not supported by S2I `UsingOpenShiftExtension`");
        }
    }

    private GitRepositoryQuarkusApplicationManagedResourceBuilder getModel() {
        return (GitRepositoryQuarkusApplicationManagedResourceBuilder) model;
    }
}
