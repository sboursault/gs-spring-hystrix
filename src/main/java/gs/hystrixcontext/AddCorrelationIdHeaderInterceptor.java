package gs.hystrixcontext;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Collections;

/**
 * Created by sboursault on 7/9/17.
 */
public class AddCorrelationIdHeaderInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String correlationId = CorrelationIdRequestContext.get();
        if (correlationId != null) {
            request.getHeaders().put(CorrelationIdRequestContext.HTTP_HEADER, Collections.singletonList(correlationId));
        }
        return execution.execute(request, body);
    }
}
