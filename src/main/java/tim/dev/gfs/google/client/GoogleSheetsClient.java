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

@Component
public class GoogleSheetsClient {

    private final HttpClient httpClient;
    private final Gson gson;

    @Value("${google.sheets.url}")
    private String scriptUrl;

    public GoogleSheetsClient() {

        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        this.gson = new Gson();
    }

    /**
     * Sends a POST request to Google Apps Script.
     *
     * @param module       Module name (EVENT, MEMBER, etc.)
     * @param action       Action (CREATE, READ, UPDATE, DELETE, etc.)
     * @param data         Data to send (can be null)
     * @param responseType Expected response type
     * @return Response converted to the specified type
     */
    public <T, R> R post(String module,
                         String action,
                         T data,
                         Class<R> responseType)
            throws IOException, InterruptedException {

        GoogleSheetRequest<T> request = new GoogleSheetRequest<>();
        request.setModule(module);
        request.setAction(action);
        request.setData(data);

        // Convert request object to JSON
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

        // If the expected response is plain text
        if (responseType == String.class) {
            return responseType.cast(response.body());
        }

        // Otherwise, deserialize JSON into the requested type
        return gson.fromJson(response.body(), responseType);
    }
}