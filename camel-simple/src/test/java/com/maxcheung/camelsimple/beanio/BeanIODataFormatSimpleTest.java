package com.maxcheung.camelsimple.beanio;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.dataformat.beanio.BeanIODataFormat;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class BeanIODataFormatSimpleTest extends CamelTestSupport {

    // START SNIPPET: e2
    private static final String FIXED_DATA =
            "Joe,Smith,Developer,75000,10012009" + LS
            + "Jane,Doe,Architect,80000,01152008" + LS
            + "Jon,Anderson,Manager,85000,03182007" + LS;
    // END SNIPPET: e2

    @Test
    public void testMarshal() throws Exception {
        List<Employee> employees = getEmployees();

        MockEndpoint mock = getMockEndpoint("mock:beanio-marshal");
        mock.expectedBodiesReceived(FIXED_DATA);

        template.sendBody("direct:marshal", employees);

        mock.assertIsSatisfied();
    }

    @Test
    public void testUnmarshal() throws Exception {
        List<Employee> employees = getEmployees();

        MockEndpoint mock = getMockEndpoint("mock:beanio-unmarshal");
        mock.expectedBodiesReceived(employees);

        template.sendBody("direct:unmarshal", FIXED_DATA);

        mock.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // START SNIPPET: e1
                // setup beanio data format using the mapping file, loaded from the classpath
                DataFormat format = new BeanIODataFormat(
                        "beanio/mappings.xml",
                        "employeeFile");

                // a route which uses the bean io data format to format a CSV data
                // to java objects
                from("direct:unmarshal")
                    .unmarshal(format)
                    // and then split the message body so we get a message for each row
                    .split(body())
                        .to("mock:beanio-unmarshal");

                // convert list of java objects back to flat format
                from("direct:marshal")
                    .marshal(format)
                    .to("mock:beanio-marshal");
                // END SNIPPET: e1
            }
        };
    }

    private List<Employee> getEmployees() throws ParseException {
        List<Employee> employees = new ArrayList<Employee>();
        Employee one = new Employee();
        one.setFirstName("Joe");
        one.setLastName("Smith");
        one.setTitle("Developer");
        one.setSalary(75000);
        one.setHireDate(new SimpleDateFormat("MMddyyyy").parse("10012009"));
        employees.add(one);

        Employee two = new Employee();
        two.setFirstName("Jane");
        two.setLastName("Doe");
        two.setTitle("Architect");
        two.setSalary(80000);
        two.setHireDate(new SimpleDateFormat("MMddyyyy").parse("01152008"));
        employees.add(two);

        Employee three = new Employee();
        three.setFirstName("Jon");
        three.setLastName("Anderson");
        three.setTitle("Manager");
        three.setSalary(85000);
        three.setHireDate(new SimpleDateFormat("MMddyyyy").parse("03182007"));
        employees.add(three);
        return employees;
    }
}