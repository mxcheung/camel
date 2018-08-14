package com.maxcheung.camelsimple.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "routeId", "routeType", "from", "toUris", "log", "tracing", "backOffMultiplier",
		"maximumRedeliveries" })
@Entity
public class RouteDef {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

    @Column
	private String applicationId;
    @Column
	private String routeId;
    @Column
	private String routeType;
    @Column(name="fromUri")
	private String from;

    @Column(name="toUris")
    private String[] toUris;
    
    @Column
	private String log;
    @Column
	private String tracing;
    @Column
	private double backOffMultiplier;
    @Column
	private int maximumRedeliveries;

    
	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

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