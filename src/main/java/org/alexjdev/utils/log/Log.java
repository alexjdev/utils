package org.alexjdev.utils.log;


import org.slf4j.LoggerFactory;

import static org.apache.commons.lang.StringUtils.defaultString;

public class Log {

    /**
     * Стандартная обработка исключений
     *
     * @param err   исключение
     * @param clazz класс в котором возникло исключение
     */
    public static <T> T error(Exception err, Class clazz, String message) {
        logError(err, clazz, message);
        throw new RuntimeException(defaultString(message) + " " + err.getMessage());
    }

    public static <T> T logError(Exception err, Class clazz, String message) {
        LoggerFactory.getLogger(clazz).error(defaultString(message) + " " + err.getMessage(), err);
        return null;
    }
}
