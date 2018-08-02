package com.maxcheung.camelsimple.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("sgxFileMover")
public class SGXFileMover {

	private static final Logger LOG = LoggerFactory.getLogger(SGXMessageProcessor.class);

    
    public void checkHoldingBay() {
		LOG.info("Checking files in holdingBay");

	}
}
