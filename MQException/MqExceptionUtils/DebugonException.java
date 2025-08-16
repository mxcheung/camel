onException(Exception.class)
    .log("Caught exception type = ${exception.class}, cause = ${exception.cause.class}, message = ${exception.message}")
    .handled(false);
