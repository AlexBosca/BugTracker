package ro.alexportfolio.backend.util;

import java.lang.reflect.Field;
import java.util.Map;

import org.springframework.util.ReflectionUtils;

public class Patcher {

    private Patcher() {
        throw new IllegalStateException("Utility class");
    }

    public static void patch(Object source, Map<String, Object> updates) {
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(source.getClass(), key);

            if(field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, source, value);
                field.setAccessible(false);
            }
        });
    }
}
