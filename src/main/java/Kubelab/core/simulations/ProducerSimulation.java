package com.kubelab.core.simulations;

import io.gatling.javaapi.core.Simulation;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
public class ProducerSimulation extends Simulation {



    public ProducerSimulation()  throws IOException{

    Properties props = new Properties();

    try (InputStream input = getClass().getClassLoader()
            .getResourceAsStream("application.properties")) {

        if (input == null) {
            throw new IllegalStateException("application.properties not found");
        }

        props.load(input);
    }

    String baseUrl = props.getProperty("TARGET_URL");
    String endpoint = props.getProperty("PRODUCER_ENDPOINT");

    int minUsers = Integer.parseInt(props.getProperty("MIN_LOAD_USERS"));
    int maxUsers = Integer.parseInt(props.getProperty("MAX_LOAD_USERS"));
    int duration = Integer.parseInt(props.getProperty("LOAD_DURATION"));

        setUp(
            scenario("Kafka Producer Load Test")
                .exec(
                    http("Produce Event")
                        .post(endpoint)
                        .header("Content-Type", "application/json")
                        .body(StringBody("""
                            {
                                "eventId": "#{randomUuid()}",
                                "type": "LOAD_TEST",
                                "source": "KubeLab",
                                "message": "Performance testing event"
                            }
                            """))
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