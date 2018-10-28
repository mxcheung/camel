package com.maxcheung.camelsimple.route;


import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.apache.camel.component.slack.SlackComponent;

import com.maxcheung.camelsimple.model.RouteDef;

public class SlackRouteBuilder extends AbsRouteBuilder {

	public SlackRouteBuilder(CamelContext camelContext, Processor processor, RouteDef routeDef) {
		super(camelContext, processor, routeDef);
	}

	@Override
	public void configure() {
		
		  // get the slack component
	    final SlackComponent slackComponent = (SlackComponent) this.getContext().getComponent("slack");
	    // set the webhook URL
	    slackComponent.setWebhookUrl("https://hooks.slack.com/services/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");
		
		from(from)
		.routeId(routeId)
		.setHeader("applicationId", constant(applicationId))
        .process(processor)
		.to(toUris)
		.tracing(tracing)
		.log(log);
	}
}
