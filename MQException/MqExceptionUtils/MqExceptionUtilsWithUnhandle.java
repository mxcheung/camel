import com.ibm.mq.MQException;
import com.ibm.mq.constants.CMQC;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.Set;
import java.util.HashSet;

public final class MqExceptionUtils {

    private static final Set<Integer> CONNECTIVITY_ERRORS = Set.of(
        CMQC.MQRC_HOST_NOT_AVAILABLE,
        CMQC.MQRC_CONNECTION_BROKEN,
        CMQC.MQRC_Q_MGR_NOT_AVAILABLE,
        CMQC.MQRC_CHANNEL_NOT_AVAILABLE
    );

    private static final Set<Integer> AUTHORIZATION_ERRORS = Set.of(
        CMQC.MQRC_NOT_AUTHORIZED,
        CMQC.MQRC_SECURITY_ERROR
    );

    public static boolean isConnectivity(MQException ex) {
        return CONNECTIVITY_ERRORS.contains(ex.reasonCode);
    }

    public static boolean isAuthorization(MQException ex) {
        return AUTHORIZATION_ERRORS.contains(ex.reasonCode);
    }

    public static boolean isUnhandled(MQException ex) {
        return !isConnectivity(ex) && !isAuthorization(ex);
    }

    private MqExceptionUtils() {}
}
