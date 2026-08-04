package tim.dev.gfs.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import tim.dev.gfs.google.client.GoogleSheetsClient;

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


}