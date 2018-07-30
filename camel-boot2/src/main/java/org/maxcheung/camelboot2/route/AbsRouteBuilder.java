package org.maxcheung.camelboot2.route;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.maxcheung.camelboot2.model.RouteDef;

public abstract class AbsRouteBuilder extends RouteBuilder {

	private static final int DEFAULT_BACKOFF_MULTIPLIER = 4;
	private static final int DEFAULT_MAXIIMUM_REDELIVERIES = 5;
	protected static final String DEFAULT_TRACING = "true";
	protected static final String DEFAULT_BODY = ">>> ${body}";
	protected final String routeId;
	protected final String from;
	protected final String[] toUris;
	protected final String log;
	protected final String tracing;
	protected final RouteDef routeDef;
	protected final double backOffMultiplier;
	protected final int maximumRedeliveries;

	public AbsRouteBuilder(CamelContext camelContext, RouteDef routeDef) {
		super(camelContext);
		this.routeId = routeDef.getRouteId();
		this.from = routeDef.getFrom();
		this.toUris = routeDef.getToUris();
		this.log = StringUtils.defaultIfEmpty(routeDef.getLog(), DEFAULT_BODY);
		this.tracing = StringUtils.defaultIfEmpty(routeDef.getTracing(), DEFAULT_TRACING);
		this.backOffMultiplier = (double) ObjectUtils.defaultIfNull(routeDef.getBackOffMultiplier(), DEFAULT_BACKOFF_MULTIPLIER);
		this.maximumRedeliveries = ObjectUtils.defaultIfNull(routeDef.getMaximumRedeliveries(), DEFAULT_MAXIIMUM_REDELIVERIES);
		this.routeDef = routeDef;

		
		
		initErrorHandling();

	}

	// general error handler
	private void initErrorHandling() {
		errorHandler(defaultErrorHandler()
				.maximumRedeliveries(maximumRedeliveries)
				.backOffMultiplier(backOffMultiplier)
				.retryAttemptedLogLevel(LoggingLevel.WARN));
	    }


}