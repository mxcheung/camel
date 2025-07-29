// Kafka consumer route using Camel Kafka
from("kafka:topic-avro?brokers=…&groupId=…&valueDeserializer=io.confluent.kafka.serializers.KafkaAvroDeserializer&schema.registry.url=http://…")
  .unmarshal().avro(AvroLibrary.Jackson, JsonNode.class)
  .process(exchange -> {
    JsonNode json = exchange.getIn().getBody(JsonNode.class);
    // JSON-style access, e.g. json.get("field").asText();
  });
