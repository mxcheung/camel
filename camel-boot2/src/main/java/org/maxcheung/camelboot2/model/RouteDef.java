package org.maxcheung.camelboot2.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "routeId", "routeType", "from", "toUris", "log", "tracing", "backOffMultiplier",
		"maximumRedeliveries" })
public class RouteDef {

	private String routeId;
	private String routeType;
	private String from;
	private String[] toUris;
	private String log;
	private String tracing;
	private double backOffMultiplier;
	private int maximumRedeliveries;

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getRouteType() {
		return routeType;
	}

	public void setRouteType(String routeType) {
		this.routeType = routeType;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String[] getToUris() {
		return toUris;
	}

	public void setToUris(String[] toUris) {
		this.toUris = toUris;
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

	public double getBackOffMultiplier() {
		return backOffMultiplier;
	}

	public void setBackOffMultiplier(double backOffMultiplier) {
		this.backOffMultiplier = backOffMultiplier;
	}

	public int getMaximumRedeliveries() {
		return maximumRedeliveries;
	}

	public void setMaximumRedeliveries(int maximumRedeliveries) {
		this.maximumRedeliveries = maximumRedeliveries;
	}

}