package io.quarkus.maven.it;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.smallrye.common.os.OS;

final class TestUtils {

    private static final long DEFAULT_TIMEOUT = OS.current() == OS.WINDOWS ? 3L : 2L;

    private TestUtils() {
    }

    static long getDefaultTimeout() {
        return DEFAULT_TIMEOUT;
    }

    static List<String> nativeArguments(String... initialArguments) {
        final List<String> result = new ArrayList<>(Arrays.asList(initialArguments));
        System.getProperties().forEach((key, value) -> {
            if (key instanceof String keyStr && value instanceof String valueStr) {
                appendArgumentIfSet(keyStr, valueStr, result);
            }
        });
        return result;
    }

    private static void appendArgumentIfSet(String property, String value, List<String> result) {
        if (value != null) {
            if (value.isEmpty()) {
                result.add("-D" + property);
            } else {
                result.add(String.format("-D%s=\"%s\"", property, value));
            }
        }
    }
}
