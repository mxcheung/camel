package com.example.camel.util;

import org.apache.camel.Exchange;
import com.ibm.mq.MQException;
import com.ibm.msg.client.jms.DetailedJMSException;

public class MqExceptionUtils {

    private MqExceptionUtils() {
    }

    public static boolean isMqHostNotAvailable(Exchange exchange) {
        MQException mqEx = extractMqException(exchange);
        return mqEx != null && mqEx.reasonCode == 2538; // MQRC_HOST_NOT_AVAILABLE
    }

    public static boolean isMqConnectionBroken(Exchange exchange) {
        MQException mqEx = extractMqException(exchange);
        return mqEx != null && mqEx.reasonCode == 2009; // MQRC_CONNECTION_BROKEN
    }

    public static boolean isMqHostNotAvailableOrConnectionBroken(Exchange exchange) {
        MQException mqEx = extractMqException(exchange);
        if (mqEx == null) return false;
        return mqEx.reasonCode == 2538 || mqEx.reasonCode == 2009;
    }

    private static MQException extractMqException(Exchange exchange) {
        Exception ex = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        if (ex == null) return null;

        Throwable cause = ex.getCause();
        if (cause instanceof DetailedJMSException detailed) {
            if (detailed.getLinkedException() instanceof MQException mqEx) {
                return mqEx;
            }
        }
        if (cause instanceof MQException mqEx) {
            return mqEx;
        }
        return null;
    }
}
