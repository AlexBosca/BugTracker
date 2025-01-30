package ro.alexportfolio.backend.mapper;

import java.lang.reflect.RecordComponent;
import java.util.HashMap;
import java.util.Map;

public class RecordMapper {
    private RecordMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Map<String, Object> toMap(Record rec) {
        Map<String, Object> map = new HashMap<>();

        if(rec == null) {
            return map;
        }

        for(RecordComponent component : rec.getClass().getRecordComponents()) {
            try {
                Object value = component.getAccessor().invoke(rec);
                if (value != null) {
                    map.put(component.getName(), value);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error accessing record component: " + component.getName(), e);
            }
        }

        return map;
    }
}
