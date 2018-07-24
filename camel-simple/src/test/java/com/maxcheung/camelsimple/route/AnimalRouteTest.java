package com.maxcheung.camelsimple.route;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("mocktest")
@RunWith(SpringRunner.class)
@SpringBootTest
public class AnimalRouteTest{

    public static final String CAMEL_FILE_NAME = "CamelFileName";

    public static final String NICE_DOG = "nice dog", NASTY_CAT="nasty cat", SUPERNASTY_CAT="super nasty cat";

    @EndpointInject(uri = "{{dogEndpoint}}")
    protected MockEndpoint dogEndpoint;

    @EndpointInject(uri = "{{catEndpoint}}")
    protected MockEndpoint catEndpoint;

    @EndpointInject(uri = "{{animalSource}}")
    protected ProducerTemplate animalSource;

    @Test 
    @DirtiesContext 
    public void testDog() throws Exception {
        animalSource.sendBodyAndHeader("test",CAMEL_FILE_NAME,NICE_DOG);
        dogEndpoint.expectedMessageCount(1);
        dogEndpoint.message(0).predicate(m -> {
            String header = m.getIn().getHeader(CAMEL_FILE_NAME).toString();
            return NICE_DOG.equals(header);
        });
        dogEndpoint.assertIsSatisfied();
         catEndpoint.expectedMessageCount(0);
        catEndpoint.assertIsSatisfied();
    }
}