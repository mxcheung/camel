package com.baeldung.camel.model;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class MyRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "routeId")
    private String routeId;

    @Column(name = "routeType")
    private String routeType;

    @Column(name = "source")
    private String source;
    
    
    @Column(name = "destination")
    private String toUris;

    @Lob
    @Column(name = "options")
    private String options;

	public MyRoute() {
		super();
	}

	public MyRoute(RouteDef routeDef, String json) {
		super();
		this.routeId = routeDef.getRouteId();
		this.routeType = routeDef.getRouteType();
		this.source = routeDef.getFrom();
		this.toUris = Arrays.toString(routeDef.getToUris());
		this.options = json;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}


	public String getToUris() {
		return toUris;
	}

	public void setToUris(String toUris) {
		this.toUris = toUris;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}


	
    
}
