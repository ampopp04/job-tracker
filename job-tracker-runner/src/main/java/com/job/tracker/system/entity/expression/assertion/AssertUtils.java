package com.job.tracker.system.entity.expression.assertion;

import static org.springframework.util.StringUtils.isEmpty;

public class AssertUtils {

    public static void assertNotEmpty(String string, String errorMessage) {
        if (isEmpty(string)) {
            throw new RuntimeException(errorMessage);
        }
    }

    public static void assertNotNull(Object o, String errorMessage) {
        if (o == null) {
            throw new RuntimeException(errorMessage);
        }
    }

}
