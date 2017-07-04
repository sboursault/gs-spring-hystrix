package gs;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withUnauthorizedRequest;


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
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class Tests {

    private MockRestServiceServer mockServer;

    @Autowired
    private RestTemplate rest;

    @Autowired
    private HelloAdapter adapter;

    private HystrixRequestContext hystrixRequestContext;

    @Before
    public void setup() {

        hystrixRequestContext = HystrixRequestContext.initializeContext();

        CorrelationIdRequestContext.set("1234");

        mockServer = MockRestServiceServer.createServer(rest);
    }

    @After
    public void teardown() {
        mockServer = null;
        hystrixRequestContext.shutdown();
    }

    @Test
    public void nominal() {

        mockServer.expect(requestTo("http://localhost:8090/hello"))
                .andRespond(withSuccess("Hello, World!", TEXT_PLAIN));

        String actualResult = adapter.callHello();

        assertThat(actualResult).isEqualTo("Hello, World!");
    }

    @Test
    public void degraded() {
        mockServer.expect(requestTo("http://localhost:8090/hello"))
                .andRespond(withUnauthorizedRequest());

        String actualResult = adapter.callHello();

        assertThat(actualResult).isEqualTo("...degraded hello...");
    }
}
