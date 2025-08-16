package com.example.camel.util;

import org.apache.camel.Exchange;
import com.ibm.mq.MQException;
import com.ibm.msg.client.jms.DetailedJMSException;

import java.util.Set;

public class MqExceptionUtils {

    private static final Set<Integer> CONNECTIVITY_ISSUES = Set.of(2538, 2009);

    private MqExceptionUtils() {
    }

    public static boolean isMqConnectivityIssue(Exchange exchange) {
        MQException mqEx = extractMqException(exchange);
        return mqEx != null && CONNECTIVITY_ISSUES.contains(mqEx.reasonCode);
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
