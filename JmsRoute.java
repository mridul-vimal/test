package com.sdge.contractjms.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;

import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;



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

//        from("file:D:/Projects/January/sample?recursive=true")
//                .doTry()
//                .unmarshal(xmlDataFormat)
//                .log(" Data --------------------->   ${body}")
//                .process(new Processor() {
//            @Override
//            public void process(Exchange exchange) throws Exception {
//                Employee employee = exchange.getIn().getBody(Employee.class);
//                System.out.println("Employee Details "+employee);
//               // employee.setName("JavaInUse Rocks");
//                exchange.getIn().setBody(employee);
//            }
//        }).marshal(jsonDataFormat)
//                .to("activemq:queue:EMP_QUEUE").doCatch(Exception.class)
//                .process(new Processor() {
//                    @Override
//                    public void process(Exchange exchange) throws Exception {
//                        Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
//                        System.out.println(cause);
//                    }
//                });

        from("cxf:bean:test-one?dataFormat=PAYLOAD")
                .routeId("route1")
                .log(LoggingLevel.INFO, "Received SOAP message: ${body}")
                .to("activemq:queue:EMP_QUEUE");
    }

}


//    @Override
//    public void configure() throws Exception {
//        from("cxf:bean:nSROrderStatusService?dataFormat=PAYLOAD")
//                .routeId("orderStatusRequestFlow")
//                .log(LoggingLevel.INFO, "Received SOAP message: ${body}")
//                .onException(Throwable.class)
//                .handled(true)
//                .bean("errorLogger", "logError(${exception}, ${messageHistory(false)})")
//                .setBody(constant(DEFAULT_NACK))
//                .log(STATUS_IDENTIFER_STRING + "Failed")
//                .convertBodyTo(String.class)
//                .to("freemarker:freemarker/syncResponse.ftl")
//
//                .end()
//                .bean("orderParser", "extractRequestDetails(${exchange},${body})")
//                .bean("orderParser", "removeDuplicates( ${exchangeProperty.ponList}, ${exchangeProperty.tnList})")
//                .bean("requestProcessingTransformer", "createOrderMessage(${body},${exchangeProperty.ponList},${exchangeProperty.tnList})")
//                .setProperty("orderMessagesList", simple("${body}"))
//                .setBody(simple("${exchangeProperty.customerMessage}"))
//                .convertBodyTo(String.class)
//                .doTry()
//                .to("direct:schemaValidation")
//                .bean("requestProcessingTransformer", "markSuccess(${property.orderMessagesList}, ${body})")
//                .doCatch(ValidationException.class)
//                .log(LoggingLevel.WARN, "Validation failure: ${exception.message}")
//                .bean("requestProcessingTransformer", "markError(${property.orderMessagesList}, ${exception}, ${body})")
//                .bean("esrStatusResponseProcessor", "createNackResponse(${body},  ${exception.message}, ${exchangeProperty.customerId}, ${exchangeProperty.supplierId})")
//                .setProperty("orderStatusResponse", simple("${body}"))
//                .setProperty("errorMessage", simple("${exception}"))
//                .log("Schema Validation Failed, Validation status updated to invalid")
//                .end()
//                .choice()
//                .when(simple("${property.errorMessage}"))
//                .bean("orderStatusMessageUpdate", "updateSyncResponse(${property.orderStatusResponse},${property.orderMessagesList})")
//                .bean("apiDBupdater", "insertOrderMessage(${body})")
//                .setBody(simple("${property.orderStatusResponse}"))
//                .endChoice()
//                .otherwise()
//                .setBody(simple("${property.orderMessagesList}"))
//                .to("direct:toBPELProcess")
//                .endChoice()
//                .end()
//                .log(LoggingLevel.INFO, "FreeMarker Processing")
//                .convertBodyTo(String.class)
//                .to("freemarker:freemarker/syncResponse.ftl");
//
//        from("direct:schemaValidation")
//                .routeId("schemaValidation")
//                .errorHandler(noErrorHandler())
//                .to("validator:artifacts/schema/ESROrderStatus.xsd")
//                .log(LoggingLevel.INFO, "Schema Validation successfull.");
//
//
//        from("direct:toBPELProcess")
//                .routeId("toBPELProcess")
//                .errorHandler(noErrorHandler())
//                .log(LoggingLevel.INFO, "Forwarding request to BPEL")
//                .bean("requestValidator", "validateRequest(${exchange})")
//                .choice()
//                .when(simple("${exchangeProperty.errorMessage}"))
//                .bean("requestProcessingTransformer", "updateStatus(${body},${exchangeProperty.errorMessage})")
//                .setProperty("orderMessagesList", simple("${body}"))
//                .bean("esrStatusResponseProcessor", "createNackResponse(${body}, ${exchangeProperty.errorMessage}, ${exchangeProperty.customerId}, ${exchangeProperty.supplierId})")
//                .setProperty("orderStatusResponse", simple("${body}"))
//                .bean("orderStatusMessageUpdate", "updateSyncResponse(${property.orderStatusResponse},${property.orderMessagesList})")
//                .setBody(simple("${property.orderStatusResponse}"))
//                .bean("apiDBupdater", "insertOrderMessage(${property.orderMessagesList})")
//                .log(LoggingLevel.INFO, "Holding order because of Business Rule Validation Failure ")
//                .endChoice()
//                .otherwise()
//                .to("direct:deliverToBPEL")
//                .endChoice()
//                .end();
//
//
//        from("direct:deliverToBPEL")
//                .routeId("deliverToBPEL")
//                .doTry()
//                .bean("fetchESRStatus", "getOrderStatus(${exchangeProperty.customerId},${exchangeProperty.supplierId},${exchangeProperty.ponList},${exchangeProperty.tnList}, ${body})")
//                .setProperty("orderMessagesList", simple("${body}"))
//                .choice()
//                .when(simple("${exchangeProperty.esrStatus}"))
//                .bean("esrStatusResponseProcessor", "createErrorOrderResponse(${body})")
//                .setProperty("orderStatusResponse", simple("${body}"))
//                .bean("orderStatusMessageUpdate", "updateSyncResponse(${property.orderStatusResponse},${property.orderMessagesList})")
//                .setProperty("orderMessagesList", simple("${body}"))
//                .setBody(simple("${property.orderStatusResponse}"))
//                .bean("apiDBupdater", "insertOrderMessages(${property.orderMessagesList})")
//                .log(LoggingLevel.INFO, "Order is logged into ESR Database with Errors")
//                .otherwise()
//                .bean("esrStatusResponseProcessor", "createSuccessOrderResponse(${body})")
//                .setProperty("orderStatusResponse", simple("${body}"))
//                .bean("orderStatusMessageUpdate", "updateSyncResponse(${property.orderStatusResponse},${property.orderMessagesList})")
//                .setProperty("orderMessagesList", simple("${body}"))
//                .setBody(simple("${property.orderStatusResponse}"))
//                .bean("apiDBupdater", "insertOrderMessages(${property.orderMessagesList})")
//                .log(LoggingLevel.INFO, "Order is logged into ESR Database with Successful Status")
//                .endChoice()
//                .end()
//                .endDoTry()
//                .doCatch(Exception.class)
//                .bean("errorLogger", "logBpelServiceFailure(${exception}})")
//                .bean("esrStatusResponseProcessor", "createErrorOrderResponse(${body})")
//                .setProperty("orderStatusResponse", simple("${body}"))
//                .bean("orderStatusMessageUpdate", "updateSyncResponse(${property.orderStatusResponse},${property.orderMessagesList})")
//                .setProperty("orderMessagesList", simple("${body}"))
//                .setBody(simple("${property.orderStatusResponse}"))
//                .bean("apiDBupdater", "insertOrderMessages(${property.orderMessagesList})");
//
//    }