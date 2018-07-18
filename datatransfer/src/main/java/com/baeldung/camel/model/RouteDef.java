package com.baeldung.camel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "routeId", "from", "destination", "log", "tracing" })
public class RouteDef {

	private String routeId;
	private String from;
	private String destination;
	private String log;
	private String tracing;
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public String getTracing() {
		return tracing;
	}
	public void setTracing(String tracing) {
		this.tracing = tracing;
	}


}
