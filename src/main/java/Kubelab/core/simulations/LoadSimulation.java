package com.kubelab.core.simulations;

import io.gatling.javaapi.core.Simulation;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
public class LoadSimulation extends Simulation {



    public LoadSimulation()  throws IOException{

        Properties props = new Properties();

        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new IllegalStateException("application.properties not found");
            }

            props.load(input);
        }

        String baseUrl = props.getProperty("TARGET_URL");
        String endpoint = props.getProperty("COMPUTE_ENDPOINT");

        int users = Integer.parseInt(props.getProperty("LOAD_USERS"));
        int duration = Integer.parseInt(props.getProperty("LOAD_DURATION"));

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