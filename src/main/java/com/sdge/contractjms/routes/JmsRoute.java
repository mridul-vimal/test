package com.sdge.contractjms.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;


/**
 * Created by ddmungui on 3/30/2017.
 */
@Component
public class JmsRoute extends RouteBuilder {

    public JmsRoute()  {
    }

    @Override
    public void configure() throws Exception {

        JaxbDataFormat xmlDataFormat = new JaxbDataFormat();
        JAXBContext con = JAXBContext.newInstance(Employee.class);
        xmlDataFormat.setContext(con);
        JacksonDataFormat jsonDataFormat = new JacksonDataFormat(Employee.class);

//        from("{{inbound.endpoint}}")
//                .transacted()
//                .log(LoggingLevel.INFO, log, "Recieved messages ")
//                .process(new Processor() {
//                    @Override
//                    public void process(Exchange exchange) throws Exception {
//                        log.info("Exchange: {}",exchange.getIn().getBody().toString());
//                    }
//                })
//                .loop()
//                .simple("{{outbound.loop.count}}")
//                .to("{{outbound.endpoint}}")
//                .log(LoggingLevel.INFO,log,"Message Sent. Iteraction ${property.CamelLoopIndex}")
//                .end();



//        from("activemq:queue:testQueue")
//                .log(LoggingLevel.INFO,log,"Recieved message ")
//                .log("{body }")
//                .to("log:?level=INFO&showBody=true");

        from("file:C:/Mridul/sample?recursive=true")
                .doTry()
                .unmarshal(xmlDataFormat)
                .log(" Data --------------------->   ${body}")
                .process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                Employee employee = exchange.getIn().getBody(Employee.class);
                System.out.println("Employee Details "+employee);
               // employee.setName("JavaInUse Rocks");
                exchange.getIn().setBody(employee);
            }
        }).marshal(jsonDataFormat)
                .to("activemq:queue:EMP_QUEUE").doCatch(Exception.class)
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
                        System.out.println(cause);
                    }
                });
    }

}
