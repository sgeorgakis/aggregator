package com.pollfish.client.util;

import com.pollfish.core.LevelType;
import com.pollfish.core.LoggingEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class RandomEventUtil {

    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static DateFormat dateFormat = new SimpleDateFormat(FORMAT);
    private static Random random = new Random();

    private RandomEventUtil() {
    }

    /**
     * Generates a random LoggingEvent object.
     *
     * @return the generated {@link LoggingEvent} object
     */
    public static LoggingEvent generateRandomEventUtil() {
        LoggingEvent event = new LoggingEvent();
        event.setId(UUID.randomUUID().toString());
        event.setTime(dateFormat.format(new Date()));
        event.setM(UUID.randomUUID().toString());
        event.setLevel(randomEnum(LevelType.class));
        return event;
    }

    /**
     * Returns a random value of an enum type
     *
     * @param clazz the enum class
     * @param <T> the enum type to be returned
     * @return A random value of the enum type
     */
    private static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }
}
