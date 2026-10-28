package com.kubelab.core.simulations;

import io.gatling.javaapi.core.Simulation;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;



public class LoadSimulation extends Simulation {

    String baseUrl = System.getenv()
        .getOrDefault("TARGET_URL", "http://localhost:8080");

    String endpoint = System.getenv()
        .getOrDefault("TARGET_ENDPOINT", "/api/compute");

    int users = Integer.parseInt(
        System.getenv().getOrDefault("LOAD_USERS", "100")
    );

    int duration = Integer.parseInt(
        System.getenv().getOrDefault("LOAD_DURATION", "60")
    );

    public LoadSimulation() {

        setUp(
            scenario("KubeLab Load Test")
                .exec(
                    http("Target Request")
                    .get(endpoint)
                    .check(status().is(200))
                )
                .injectOpen(
                    constantUsersPerSec(users).during(duration)
                )
        ).protocols(
            http.baseUrl(baseUrl)
        );
    }
}