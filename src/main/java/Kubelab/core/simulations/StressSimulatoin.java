package com.kubelab.core.simulations;

import io.gatling.javaapi.core.Simulation;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;



public class StressSimulation extends Simulation {

    String baseUrl = System.getenv()
        .getOrDefault("TARGET_URL", "http://localhost:8080");

    String endpoint = System.getenv()
        .getOrDefault("TARGET_ENDPOINT", "/api/compute");

    int minUsers = Integer.parseInt(
        System.getenv().getOrDefault("MIN_LOAD_USERS", "10")
    );
    int maxUsers = Integer.parseInt(
        System.getenv().getOrDefault("MAX_LOAD_USERS", "200")
    );

    int duration = Integer.parseInt(
        System.getenv().getOrDefault("LOAD_DURATION", "180")
    );

    public LoadSimulation() {

        setUp(
            scenario("KubeLab Stress Test")
                .exec(
                    http("Target Request")
                    .get(endpoint)
                    .check(status().is(200))
                )
                .injectOpen(
                    rampUsersPerSec(minUsers).to(maxUsers).during(duration)
                )
        ).protocols(
            http.baseUrl(baseUrl)
        );
    }
}