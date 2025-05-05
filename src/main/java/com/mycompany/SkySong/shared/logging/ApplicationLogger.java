package com.mycompany.SkySong.application.shared.logging;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public interface ApplicationLogger {

    void debug(String message);
    void info(String message);

    void warn(String message);

    void error(String message, Throwable throwable);

    void debug(String message, Context context);
    void info(String message, Context context);

    void warn(String message, Context context);

    void error(String message, Throwable throwable, Context context);


    record Context(Map<String, Object> values) {

        public static Context of(String key, Object value) {
            return new Context(Collections.singletonMap(key, value));
        }

        public static Context of(Map<String, Object> values) {
            Objects.requireNonNull(values, "Context values must not be null");
            return new Context(Map.copyOf(values));
        }

        public boolean isEmpty() {
            return values == null || values.isEmpty();
        }

        public String toFormattedString() {
            if (isEmpty()) {
                return "";
            }

            return values.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining(", ", "[", "]"));
        }
    }
}
