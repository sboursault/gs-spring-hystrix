package gs;

import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import gs.hystrixcontext.AddCorrelationIdHeaderInterceptor;
import gs.hystrixcontext.CorrelationIdLoggingContext;
import gs.hystrixcontext.CorrelationIdRequestContext;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableCircuitBreaker
public class DemoApplication {

    static {
        HystrixPlugins.getInstance().registerCommandExecutionHook(new HystrixCommandExecutionHook() {
            @Override
            public <T> void onThreadStart(HystrixInvokable<T> commandInstance) {
                CorrelationIdLoggingContext.initialize();
            }

            @Override
            public <T> void onThreadComplete(HystrixInvokable<T> commandInstance) {
                CorrelationIdLoggingContext.clear();
            }
        });
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .interceptors(new AddCorrelationIdHeaderInterceptor())
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
