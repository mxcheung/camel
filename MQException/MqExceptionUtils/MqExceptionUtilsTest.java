import com.ibm.mq.MQException;
import com.ibm.mq.constants.CMQC;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MqExceptionUtilsTest {

    @Test
    void testConnectivityErrors() {
        assertTrue(MqExceptionUtils.isConnectivity(new MQException(0, CMQC.MQRC_HOST_NOT_AVAILABLE, null)));
        assertTrue(MqExceptionUtils.isConnectivity(new MQException(0, CMQC.MQRC_CONNECTION_BROKEN, null)));
        assertTrue(MqExceptionUtils.isConnectivity(new MQException(0, CMQC.MQRC_Q_MGR_NOT_AVAILABLE, null)));
        assertTrue(MqExceptionUtils.isConnectivity(new MQException(0, CMQC.MQRC_CHANNEL_NOT_AVAILABLE, null)));

        // Should not be authorization
        assertFalse(MqExceptionUtils.isAuthorization(new MQException(0, CMQC.MQRC_HOST_NOT_AVAILABLE, null)));
        // Should not be unhandled
        assertFalse(MqExceptionUtils.isUnhandled(new MQException(0, CMQC.MQRC_HOST_NOT_AVAILABLE, null)));
    }

    @Test
    void testAuthorizationErrors() {
        assertTrue(MqExceptionUtils.isAuthorization(new MQException(0, CMQC.MQRC_NOT_AUTHORIZED, null)));
        assertTrue(MqExceptionUtils.isAuthorization(new MQException(0, CMQC.MQRC_SECURITY_ERROR, null)));

        // Should not be connectivity
        assertFalse(MqExceptionUtils.isConnectivity(new MQException(0, CMQC.MQRC_NOT_AUTHORIZED, null)));
        // Should not be unhandled
        assertFalse(MqExceptionUtils.isUnhandled(new MQException(0, CMQC.MQRC_SECURITY_ERROR, null)));
    }

    @Test
    void testUnhandledErrors() {
        int someOtherReasonCode = CMQC.MQRC_Q_FULL; // not in connectivity or auth sets

        MQException ex = new MQException(0, someOtherReasonCode, null);

        assertFalse(MqExceptionUtils.isConnectivity(ex));
        assertFalse(MqExceptionUtils.isAuthorization(ex));
        assertTrue(MqExceptionUtils.isUnhandled(ex));
    }

    @Test
    void testUtilityClassConstructorIsPrivate() throws Exception {
        var constructor = MqExceptionUtils.class.getDeclaredConstructor();
        assertTrue(constructor.canAccess(null) == false); // constructor is private
    }
}
