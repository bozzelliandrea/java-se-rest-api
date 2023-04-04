package server.util;

import java.time.ZonedDateTime;

public final class Logger {

    public static void forRequest(String method, String path) {
        System.out.println(
                ZonedDateTime.now() + " | " +
                        "RECEIVED CALL METHOD: " + method + " | " +
                        "REQUESTED PATH: " + path
        );
    }

    public static void forResponse(String message) {
        System.out.println(
                ZonedDateTime.now() + " | " +
                        "SERVE CLIENT WITH: " + message
        );
    }

    public static void forResponseError(String message) {
        System.err.println(ZonedDateTime.now() + " | " +
                "FAILED CLIENT REQUEST: " + message);
    }
}
