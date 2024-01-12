package app.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.*;


public class LogsUtils {
    public static String objectToJson(Object o) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(o);
        } catch (JsonProcessingException e) {
            return "LogsUtils.objectToJson(): JsonParsing error {} "+ e.getMessage();
        }
    }
}
