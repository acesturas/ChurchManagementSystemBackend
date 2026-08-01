package tim.dev.gfs.google.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import tim.dev.gfs.dto.GoogleSheetRequest;

@Component
public class GoogleSheetsClient {
	
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${google.sheets.url}")
    private String scriptUrl;

    @PostConstruct
    public void init() {
        System.out.println("Google Script URL: " + scriptUrl);
    }
//    public <ResponseType, RequestData> ResponseType post(GoogleSheetRequest<RequestData> request, Class<ResponseType> responseType) {
//
//        return restTemplate.postForObject(
//                scriptUrl,
//                request,
//                responseType
//        );
//
//    }
    public String post(GoogleSheetRequest<?> request) {

        return restTemplate.postForObject(
        		scriptUrl,
                request,
                String.class
        );
    }


}
