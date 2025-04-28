package simulations;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class VirtualThreadSimulations extends Simulation {
    private HttpProtocolBuilder httpProtocol = http.baseUrl("http://host.docker.internal:8080");
    private ScenarioBuilder scn = scenario("Teste de Carga com Java").exec(http("Requisição autenticada").get("/virtual-threads-future").check(status().is(200)));
    {
        setUp(
                scn.injectOpen(atOnceUsers(10))
        ).protocols(httpProtocol);
    }
}
