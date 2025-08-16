Ahh, that explains it 👌 — when using Spring JMS with IBM MQ, the UncategorizedJmsException usually wraps a DetailedJMSException, and that in turn holds the actual MQException inside its linkedException.

So instead of checking only ex.getCause(), you need to unwrap twice:

UncategorizedJmsException →

DetailedJMSException →

getLinkedException() → actual MQException with reason code (2009, 2538, etc.)
