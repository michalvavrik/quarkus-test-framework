package io.quarkus.qe;

import org.junit.jupiter.api.Test;

import io.quarkus.test.bootstrap.RestService;
import io.quarkus.test.scenarios.KubernetesDeploymentStrategy;
import io.quarkus.test.scenarios.KubernetesScenario;
import io.quarkus.test.services.Dependency;
import io.quarkus.test.services.QuarkusApplication;

@KubernetesScenario(deployment = KubernetesDeploymentStrategy.UsingKubernetesExtension)
public class KubernetesUsingExtensionPingPongResourceIT {

    @QuarkusApplication(dependencies = @Dependency(artifactId = "quarkus-container-image-jib", groupId = "io.quarkus"))
    static final RestService pingpong = new RestService();

    @Test
    public void shouldPingPongIsUpAndRunning() {
        pingpong.logs().forQuarkus().installedFeatures().contains("kubernetes");
    }
}
