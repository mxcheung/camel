package com.baeldung.camel.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MyRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "routeId")
    private String routeId;

    @Column(name = "source")
    private String source;
    
    
    @Column(name = "destination")
    private String destination;

	public MyRoute() {
		super();
	}

	public MyRoute(String routeId, String source, String destination) {
		super();
		this.routeId = routeId;
		this.source = source;
		this.destination = destination;
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
    
}
