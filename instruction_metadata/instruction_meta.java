from("direct:processInstruction")
    .process(exchange -> {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("region", System.getenv("REGION"));
        metadata.put("clientId", System.getenv("CLIENT_ID"));
        exchange.getIn().setHeader("instruction_metadata", metadata);
    })
    .to("jms:queue:instructions");

from("direct:processInstruction")
    .process(exchange -> {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("region", System.getenv("REGION"));
        metadata.put("clientId", System.getenv("CLIENT_ID"));
        exchange.getIn().setHeader("instruction_metadata", metadata);
    })
    .log("Instruction metadata = ${header.instruction_metadata}")
    .to("jms:queue:instructions");

from("direct:processInstruction")
    .process(exchange -> {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("region", System.getenv("REGION"));
        metadata.put("clientId", System.getenv("CLIENT_ID"));
        exchange.getIn().setHeader("instruction_metadata", metadata);
    })
    .log("{\"@message\":\"Sending Message\",\"instruction_metadata\":${header.instruction_metadata}}")
    .to("jms:queue:instructions");

from("direct:processInstruction")
    .process(exchange -> {
        Map<String, Object> instructionMeta = new HashMap<>();
        instructionMeta.put("region", System.getenv("REGION"));
        instructionMeta.put("clientId", System.getenv("CLIENT_ID"));
        exchange.getIn().setHeader("instruction_metadata", instructionMeta);
    })
    .log("{\"@message\":\"routeId:${routeId}, payload:${body}\", \"instruction_metadata\":${header.instruction_metadata}}")
    .to("jms:queue:instructions");

