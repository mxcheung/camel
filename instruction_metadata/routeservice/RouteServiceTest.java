@Test
void testLoadRoutes_ParsesJsonContent() throws Exception {
    Resource res1 = mock(Resource.class);
    Resource res2 = mock(Resource.class);

    String json1 = "{\"routeId\": \"r1\", \"from\": \"direct:start\", \"to\": \"log:end\"}";
    String json2 = "{\"routeId\": \"r2\", \"from\": \"seda:queue\", \"to\": \"mock:result\"}";

    when(res1.getFilename()).thenReturn("route1.json");
    when(res2.getFilename()).thenReturn("route2.json");

    when(res1.getInputStream()).thenReturn(new ByteArrayInputStream(json1.getBytes(StandardCharsets.UTF_8)));
    when(res2.getInputStream()).thenReturn(new ByteArrayInputStream(json2.getBytes(StandardCharsets.UTF_8)));

    when(mockResolver.getResources("classpath:routes/*.json"))
            .thenReturn(new Resource[]{res1, res2});

    List<RouteDef> routeDefs = routeService.loadRoutes();

    assertThat(routeDefs).hasSize(2);

    RouteDef r1 = routeDefs.get(0);
    assertThat(r1.getId()).isEqualTo("route1.json");
    assertThat(r1.getFromUri()).isEqualTo("direct:start");
    assertThat(r1.getToUri()).isEqualTo("log:end");

    RouteDef r2 = routeDefs.get(1);
    assertThat(r2.getId()).isEqualTo("route2.json");
    assertThat(r2.getFromUri()).isEqualTo("seda:queue");
    assertThat(r2.getToUri()).isEqualTo("mock:result");
}
