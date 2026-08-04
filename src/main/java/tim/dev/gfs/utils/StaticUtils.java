package tim.dev.gfs.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

public class StaticUtils {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    public static String generateId(String prefix,
                                    String locationCode,
                                    int sequence) {

        String date = LocalDate.now().format(DATE_FORMAT);

        return prefix
                + locationCode
                + date
                + String.format("%05d", sequence);
    }

    /**
     * Extracts the sequence number from the last Event ID
     * and returns the next available sequence.
     *
     * Example:
     * EVPC2026080400001 -> 2
     * EVPC2026080400123 -> 124
     *
     * If no previous Event ID exists, returns 1.
     */
    public static int getNextSequence(String lastEventId) {

        if (lastEventId == null || lastEventId.isBlank()) {
            return 1;
        }

        try {

            // Last 5 characters contain the sequence number
            String sequence = lastEventId.substring(lastEventId.length() - 5);
            
            System.out.println("sequence: " + sequence);

            return Integer.parseInt(sequence) + 1;

        } catch (Exception e) {

            // Invalid format, start again at 1
            return 1;

        }
    }
    
    public static Gson getGson() {
    	return new GsonBuilder()

        	    // LocalDate -> "2026-08-04"
        	    .registerTypeAdapter(
        	        LocalDate.class,
        	        (JsonSerializer<LocalDate>) (src, type, context) ->
        	            new JsonPrimitive(src.toString())
        	    )

        	    // Google Sheet ISO -> LocalDate
        	    .registerTypeAdapter(
        	        LocalDate.class,
        	        (JsonDeserializer<LocalDate>) (json, type, context) ->
        	            java.time.Instant.parse(json.getAsString())
        	                .atZone(java.time.ZoneOffset.UTC)
        	                .toLocalDate()
        	    )


        	    // LocalTime -> "14:50:35"
        	    .registerTypeAdapter(
        	        LocalTime.class,
        	        (JsonSerializer<LocalTime>) (src, type, context) ->
        	            new JsonPrimitive(src.toString())
        	    )

        	    // Google Sheet ISO -> LocalTime
        	    .registerTypeAdapter(
        	        LocalTime.class,
        	        (JsonDeserializer<LocalTime>) (json, type, context) ->
        	            java.time.Instant.parse(json.getAsString())
        	                .atZone(java.time.ZoneOffset.UTC)
        	                .toLocalTime()
        	    )


        	    // Timestamp
        	    .registerTypeAdapter(
        	        Timestamp.class,
        	        (JsonSerializer<Timestamp>) (src, type, context) ->
        	            new JsonPrimitive(src.toInstant().toString())
        	    )

        	    .registerTypeAdapter(
        	    	    Timestamp.class,
        	    	    (JsonDeserializer<Timestamp>) (json, type, context) -> {

        	    	        String value = json.getAsString();

        	    	        if (value == null || value.isEmpty()) {
        	    	            return null;
        	    	        }

        	    	        return Timestamp.from(
        	    	            java.time.Instant.parse(value)
        	    	        );
        	    	    }
        	    	)

        	    .create();
    }

}