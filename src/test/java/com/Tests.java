package com;

import com.entity.PatientEntity;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.text.ParseException;

@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest(properties = {"kafka-requests-path=direct:requests"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@MockEndpoints
public class Tests {

    @Autowired
    ProducerTemplate producerTemplate;

    @EndpointInject("mock:jpa:com.entity.PatientEntity")
    public MockEndpoint saveToDb;

    @EndpointInject("mock:kafka:results")
    public MockEndpoint kafkaResults;

    @EndpointInject("mock:kafka:status_topic")
    public MockEndpoint kafkaStatusTopic;

    @Test
    public void saveToDBTest() throws InterruptedException, ParseException {
        PatientEntity patient = new PatientEntity();
        patient.setPatient_surname("Kudryashov");
        patient.setPatient_name("Nikita");
        patient.setPatient_age(21);
        saveToDb.expectedBodiesReceived(patient);

        producerTemplate.sendBody("direct:requests", "<patient><surname>Kudryashov</surname><name>Nikita</name><age>21</age><height>188</height><weight>72</weight></patient>");

        saveToDb.assertIsSatisfied(5000);
    }

    @Test
    public void kafkaResultsTest() throws InterruptedException {
        kafkaResults.expectedBodiesReceived("{\"surname\":\"Kudryashov\",\"name\":\"Nikita\",\"age\":21}");

        producerTemplate.sendBody("direct:requests", "<patient><surname>Kudryashov</surname><name>Nikita</name><age>21</age><height>188</height><weight>72</weight></patient>");

        MockEndpoint.assertIsSatisfied(kafkaResults);
    }

    @Test
    public void sendOKStatusTest() throws InterruptedException {
        kafkaStatusTopic.expectedBodiesReceived("<status>ok</status>");

        producerTemplate.sendBody("direct:requests", "<patient><surname>Kudryashov</surname><name>Nikita</name><age>21</age><height>188</height><weight>72</weight></patient>");

        kafkaStatusTopic.assertIsSatisfied(5000);
    }

    @Test
    public void sendErrorStatusTest() throws InterruptedException {
        kafkaStatusTopic.expectedBodiesReceived("<status>error</status><message>Unmarshaling failed</message>");

        producerTemplate.sendBody("direct:requests", "<not_patient><surname>Kudryashov</surname><name>Nikita</name><age>21</age><height>188</height><weight>72</weight></not_patient>");

        kafkaStatusTopic.assertIsSatisfied(5000);
    }
}