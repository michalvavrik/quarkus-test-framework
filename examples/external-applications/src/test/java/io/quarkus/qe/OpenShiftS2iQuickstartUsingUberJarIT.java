package io.quarkus.qe;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.test.bootstrap.RestService;
import io.quarkus.test.scenarios.OpenShiftScenario;
import io.quarkus.test.scenarios.annotations.DisabledOnNative;
import io.quarkus.test.scenarios.annotations.DisabledOnQuarkusSnapshot;
import io.quarkus.test.services.GitRepositoryQuarkusApplication;

@DisabledOnNative(reason = "This is to verify uber-jar, so it does not make sense on Native")
@DisabledOnQuarkusSnapshot(reason = "999-SNAPSHOT is not available in the Maven repositories in OpenShift")
@OpenShiftScenario
public class OpenShiftS2iQuickstartUsingUberJarIT {

    /**
     * Package type is set in the custom template.
     */
    @GitRepositoryQuarkusApplication(repo = "https://github.com/quarkusio/quarkus-quickstarts.git", branch = "2.13", contextDir = "getting-started")
    static final RestService appuberjar = new RestService();

    @Test
    public void test() {
        appuberjar.given()
                .get("/hello")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

}
