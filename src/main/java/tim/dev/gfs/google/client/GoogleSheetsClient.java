package tim.dev.gfs.google.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import tim.dev.gfs.dto.GoogleSheetRequest;

@Component
public class GoogleSheetsClient {
	
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${google.sheets.url}")
    private String scriptUrl;

    public ResponseEntity<String> post(GoogleSheetRequest<?> request) {

        ResponseEntity<String> response = restTemplate.postForEntity(
                scriptUrl,
                request,
                String.class
        );

        System.out.println("Status : " + response.getStatusCode());
        System.out.println("Headers: " + response.getHeaders());
        System.out.println("Body   : " + response.getBody());

        return response;
    }


}
