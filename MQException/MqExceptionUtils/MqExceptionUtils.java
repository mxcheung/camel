package com.example.camel.util;

import org.apache.camel.Exchange;
import com.ibm.mq.MQException;

public class MqExceptionUtils {

    private MqExceptionUtils() {
        // Utility class: prevent instantiation
    }

    public static boolean isMqHostNotAvailable(Exchange exchange) {
        Exception ex = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        Throwable cause = ex != null ? ex.getCause() : null;
        if (cause instanceof MQException mqEx) {
            return mqEx.reasonCode == 2538; // MQRC_HOST_NOT_AVAILABLE
        }
        return false;
    }

    public static boolean isMqConnectionBroken(Exchange exchange) {
        Exception ex = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        Throwable cause = ex != null ? ex.getCause() : null;
        if (cause instanceof MQException mqEx) {
            return mqEx.reasonCode == 2009; // MQRC_CONNECTION_BROKEN
        }
        return false;
    }

    public static boolean isMqHostNotAvailableOrConnectionBroken(Exchange exchange) {
        return isMqHostNotAvailable(exchange) || isMqConnectionBroken(exchange);
    }
}
