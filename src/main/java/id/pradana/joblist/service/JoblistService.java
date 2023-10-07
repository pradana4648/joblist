package id.pradana.joblist.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class JoblistService {

    public ResponseEntity<?> getJobs() {
        String url = "https://dev6.dansmultipro.com/api/recruitment/positions.json";
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.exchange(url, HttpMethod.GET, null, List.class);
        } catch (RestClientException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    public ResponseEntity<?> getJobById(String id) {
        String url = "https://dev6.dansmultipro.com/api/recruitment/positions/" + id;
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.exchange(url, HttpMethod.GET, null, Map.class);
        } catch (RestClientException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
