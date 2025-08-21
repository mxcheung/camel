from("direct:processInstruction")
    .process(exchange -> {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("region", System.getenv("REGION"));
        metadata.put("clientId", System.getenv("CLIENT_ID"));
        exchange.getIn().setHeader("instruction_metadata", metadata);
    })
    .to("jms:queue:instructions");
