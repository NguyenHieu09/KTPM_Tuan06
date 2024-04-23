//package vn.edu.iuh.fit.config;
//
//import com.google.gson.*;
//import java.lang.reflect.Type;
//import java.time.LocalDateTime;
//
//public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
//    @Override
//    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
//        JsonObject obj = new JsonObject();
//        obj.addProperty("year", src.getYear());
//        obj.addProperty("month", src.getMonthValue());
//        obj.addProperty("day", src.getDayOfMonth());
//        obj.addProperty("hour", src.getHour());
//        obj.addProperty("minute", src.getMinute());
//        obj.addProperty("second", src.getSecond());
//        return obj;
//    }
//
//    @Override
//    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//        JsonObject obj = json.getAsJsonObject();
//        int year = obj.get("year").getAsInt();
//        int month = obj.get("month").getAsInt();
//        int day = obj.get("day").getAsInt();
//        int hour = obj.get("hour").getAsInt();
//        int minute = obj.get("minute").getAsInt();
//        int second = obj.get("second").getAsInt();
//        return LocalDateTime.of(year, month, day, hour, minute, second);
//    }
//
//
//}

package vn.edu.iuh.fit.config;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        String formattedDate = src.format(formatter);
        return new JsonPrimitive(formattedDate);
    }

    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String dateString = json.getAsString();
        return LocalDateTime.parse(dateString, formatter);
    }
}

