package gs;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.hamcrest.core.IsAnything.anything;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoTests {

    private MockRestServiceServer mockServer;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Before
    public void setup() {
        mockServer = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void correlation_ID_is_passed_across_services() {

        // initialize mock server, expecting a request with a specific correlation ID
        mockServer.expect(requestTo("http://remote/service"))
                .andExpect(header("X-Correlation-ID", "123e4567-e89b-12d3-a456-426655440000"))
                .andRespond(withSuccess());

        // call demo with correlation ID
        httpGet("/demo", headers("X-Correlation-ID", "123e4567-e89b-12d3-a456-426655440000"));

        // verify expectations
        mockServer.verify();
    }

    @Test
    public void correlation_ID_is_generated_if_missing() {

        // initialize mock server, expecting a request with any correlation ID
        mockServer.expect(requestTo("http://remote/service"))
                .andExpect(header("X-Correlation-ID", anything()))
                .andRespond(withSuccess());

        // call demo without correlation ID
        httpGet("/demo", noHeaders());

        // verify expectations
        mockServer.verify();
    }

    // helpers

    private HttpHeaders noHeaders() {
        return new HttpHeaders();
    }

    private HttpHeaders headers(String key, String value)  {
        return headers(ImmutableMap.of(key, value));
    }

    private HttpHeaders headers(Map<String, String> map)  {
        HttpHeaders requestHeaders = new HttpHeaders();
        map.entrySet().stream()
                .forEach(entry -> requestHeaders.add(entry.getKey(), entry.getValue()));
        return requestHeaders;
    }

    private void httpGet(String url, HttpHeaders headers) {
        testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
    }
}
