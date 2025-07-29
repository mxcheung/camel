import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.avro.AvroDataFormat;

public class MyRoute extends RouteBuilder {
  @Override
  public void configure() {
    Schema schema = new Parser().parse(schemaJson);
    AvroDataFormat avroFormat = new AvroDataFormat();
    // Pass schema object instead of SCHEMA$
    avroFormat.setSchema(schema);
    avroFormat.setInstanceClassName(null); // optional if no generated class

    from("direct:in")
      .marshal(avroFormat)
      .to("mock:out");

    from("direct:back")
      .unmarshal(avroFormat)
      .process(exchange -> {
        GenericRecord rec = exchange.getIn().getBody(GenericRecord.class);
        // handle record fields...
      });
  }
}
