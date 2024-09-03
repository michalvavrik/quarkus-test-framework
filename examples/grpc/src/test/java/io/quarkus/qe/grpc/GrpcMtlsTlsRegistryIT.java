package io.quarkus.qe.grpc;

import static io.quarkus.test.services.Certificate.Format.PEM;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.quarkus.test.bootstrap.GrpcService;
import io.quarkus.test.scenarios.QuarkusScenario;
import io.quarkus.test.services.Certificate;
import io.quarkus.test.services.Certificate.ClientCertificate;
import io.quarkus.test.services.QuarkusApplication;

@QuarkusScenario
public class GrpcMtlsTlsRegistryIT {

    private static final String CN = "Hagrid";
    private static final String NAME = "Albus";

    @QuarkusApplication(grpc = true, ssl = true)
    static final GrpcService app = (GrpcService) new GrpcService()
            .withProperty("quarkus.grpc.server.use-separate-server", "false")
            .withProperty("quarkus.http.insecure-requests", "disabled")
            .withProperty("quarkus.http.ssl.client-auth", "request")
            .withProperty("quarkus.http.auth.permission.perm-1.policy", "authenticated")
            .withProperty("quarkus.http.auth.permission.perm-1.paths", "*")
            .withProperty("quarkus.http.auth.permission.perm-1.auth-mechanism", "X509")
            .withProperty("quarkus.tls.grpc-tls.trust-store.pem.certs", "C:\\cygwin\\tmp\\quarkus-qe-certs3246157749933556819\\quarkus-qe-server-ca.crt")
            .withProperty("quarkus.tls.grpc-tls.key-store.pem.pem-1.cert", "C:\\cygwin\\tmp\\quarkus\n" +
                    "-qe-certs3246157749933556819\\quarkus-qe.crt")
            .withProperty("quarkus.tls.grpc-tls.key-store.pem.pem-1.key", "C:\\cygwin\\tmp\\quarkus-qe-certs3246157749933556819\\quarkus-qe.key");

    @Test
    public void testMutualTlsCommunicationWithHelloService() {
        try (var channel = app.securedGrpcChannel()) {
            // here both server and client certificates are generated and used
            HiRequest request = HiRequest.newBuilder().setName(NAME).build();
            HiReply response = GreeterGrpc.newBlockingStub(channel).sayHi(request);

            assertEquals("Hello " + NAME, response.getMessage());
            assertEquals("CN=Hagrid", response.getPrincipalName());
        }
    }

}
