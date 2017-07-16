package gs.hystrixcontext;

import com.netflix.hystrix.strategy.HystrixPlugins;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;

/**
 * Hystrix related configuration. Defines the tuned rest template to put to context.
 */
@Configuration
@EnableCircuitBreaker
public class HystrixConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .interceptors(new AddCorrelationIdHttpHeaderInterceptor())
                .build();
    }

    /**
     * <p>ClientHttpRequestInterceptor to add the correlation ID header on outgoing requests.</p>
     */
    static class AddCorrelationIdHttpHeaderInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            String correlationId = CorrelationId.get();
            if (correlationId != null) {
                request.getHeaders().put(CorrelationId.HTTP_HEADER, Collections.singletonList(correlationId));
            }
            return execution.execute(request, body);
        }
    }
}
