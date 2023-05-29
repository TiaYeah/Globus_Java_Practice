package com.router;

import com.dto.PatientDTO;
import com.entity.PatientEntity;
import com.generated.Patient;
import com.mapper.PatientMapper;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveRouter extends RouteBuilder {
    private final PatientMapper mapper;

    public void configure() {
        from("direct:save_to_db")
                .choice()
                .when(body().isInstanceOf(Patient.class))
                .log("Message received from Kafka : ${body}")
                .log("    on the topic ${headers[kafka.TOPIC]}")
                .process(exchange -> {
                    Patient in = exchange.getIn().getBody(Patient.class);
                    PatientEntity Patient = mapper.mapGenerated(in);

                    exchange.getMessage().setBody(Patient, PatientEntity.class);
                })
                .log("Saving ${body} to database...")
                .to("jpa:com.entity.PatientEntity")
                .process(exchange -> {
                    PatientEntity in = exchange.getIn().getBody(PatientEntity.class);
                    PatientDTO Patient = mapper.mapWithoutId(in);

                    exchange.getMessage().setBody(Patient, PatientDTO.class);
                })
                .log("Saving ${body} to kafka")
                .marshal().json(JsonLibrary.Jackson)
                .to("kafka:results?brokers=localhost:9092")
                .setBody(simple("<status>ok</status>"))
                .to("direct:status")
                .to("direct:metrics_router_increment_success_messages")
                .to("direct:metrics_router_stop_timer")
                .otherwise()
                .setBody(simple("<status>error</status><message>XML data isn't instance of Patient</message>"))
                .to("direct:status")
                .to("direct:metrics_router_increment_fail_messages")
                .to("direct:metrics_router_stop_timer");
    }
}