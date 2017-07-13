package gs.hystrixcontext;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Collections;

/**
 *
 * <p>ClientHttpRequestInterceptor to add the correlation ID header on outgoing requests.</p>
 */
public class AddCorrelationIdHttpHeaderInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String correlationId = CorrelationId.get();
        if (correlationId != null) {
            request.getHeaders().put(CorrelationId.HTTP_HEADER, Collections.singletonList(correlationId));
        }
        return execution.execute(request, body);
    }
}
