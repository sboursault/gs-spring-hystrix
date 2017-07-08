package gs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


/**
 * resources:
 * https://jfconavarrete.wordpress.com/2014/09/15/make-spring-security-context-available-inside-a-hystrix-command/
 * https://github.com/Netflix/Hystrix/wiki/Plugins
 * https://github.com/Netflix/Hystrix/issues/92
 * https://dzone.com/articles/implementing-correlation-ids-0
 *
 * TODO : initialize hystrixRequestContext in a filter (with shutdown)
 * TODO : configure log to output correlationId implicitly MCD ??
 *
 * @ConfigurationProperties
 * gestion des dépendances facilitée ???? pas besoin de préciser la version, au moins avec graddle
 *
 * revoir gestion des exceptions avec hystrix, jeter une checked exception si indispo ?
 *
 */

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

    @After
    public void teardown() {
        mockServer = null;
    }

    @Test
    public void nominal() {

        mockServer.expect(requestTo("http://remote/service"))
                .andRespond(withSuccess("12:00", TEXT_PLAIN));

        httpGet("/demo");

        mockServer.verify();
    }

    @Test
    public void degraded() {

        mockServer.expect(requestTo("http://remote/service"))
                .andRespond(withServerError());

        httpGet("/demo");

        mockServer.verify();
    }

    private void httpGet(String url) {
        testRestTemplate.getForObject(url, String.class);
    }
}
