package tim.dev.gfs.google.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import tim.dev.gfs.dto.GoogleSheetRequest;
import tim.dev.gfs.utils.StaticUtils;

@Component
public class GoogleSheetsClient {

    // Java's modern HTTP client
    private final HttpClient httpClient;

    // Gson instance used to serialize/deserialize JSON
    private final Gson gson;

    // Google Apps Script Web App URL
    @Value("${google.sheets.url}")
    private String scriptUrl;

    public GoogleSheetsClient() {

        // Configure HttpClient
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        /*
         * Configure Gson.
         *
         * Java 17+ (including Java 21) prevents Gson from accessing the
         * private fields of java.time classes using reflection.
         *
         * Therefore we register custom serializers/deserializers for
         * LocalDate and Timestamp.
         */

        this.gson = StaticUtils.getGson();
    }

    /**
     * Sends a POST request to Google Apps Script.
     *
     * @param module Module to execute (EVENT, MEMBER, etc.)
     * @param action Action to execute (CREATE, READ, UPDATE, etc.)
     * @param data Data object to send (can be null)
     * @param responseType Expected response type
     * @return Parsed response
     */
    public <T, R> R post(
            String module,
            String action,
            T data,
            Class<R> responseType)
            throws IOException, InterruptedException {

        // Build the request expected by Apps Script
        GoogleSheetRequest<T> request = new GoogleSheetRequest<>();
        request.setModule(module);
        request.setAction(action);
        request.setData(data);

        // Convert request object into JSON
        String json = gson.toJson(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(scriptUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(
                httpRequest,
                HttpResponse.BodyHandlers.ofString()
        );

        System.out.println("Status Code : " + response.statusCode());
        System.out.println("URI         : " + response.uri());
        System.out.println("Headers     : " + response.headers().map());
        System.out.println("Body:");
        System.out.println(response.body());

        if (response.statusCode() != 200) {
            throw new IOException(
                "Google Apps Script returned " +
                response.statusCode() +
                "\n" +
                response.body()
            );
        }
        
        // If Apps Script returns plain text, don't parse it as JSON
        if (responseType == String.class) {
            return responseType.cast(response.body());
        }

        // Otherwise deserialize the JSON response
        return gson.fromJson(response.body(), responseType);
    }
    
    
}