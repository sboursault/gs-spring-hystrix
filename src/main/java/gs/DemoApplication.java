package gs;

import com.netflix.hystrix.strategy.HystrixPlugins;
import gs.hystrixcontext.AddCorrelationIdHttpHeaderInterceptor;
import gs.hystrixcontext.AddCorrelationIdToDiagnosticContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/*
 * Sprint boot appplication
 */
@SpringBootApplication
@EnableCircuitBreaker
public class DemoApplication {

    static {
        HystrixPlugins.getInstance().registerCommandExecutionHook(new AddCorrelationIdToDiagnosticContext());
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .interceptors(new AddCorrelationIdHttpHeaderInterceptor())
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
