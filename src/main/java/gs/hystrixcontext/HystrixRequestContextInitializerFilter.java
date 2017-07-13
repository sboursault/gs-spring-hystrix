package gs.hystrixcontext;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

/**
 * <p>ServletFilter for initializing HystrixRequestContext at the beginning of an HTTP
 * request and shutting down at the end.</p>
 *
 * <p>If a http header contains a request correlation ID, it is stored as an hystrix request
 * variable, otherwise an ID is generated.</p>
 *
 * <p>The correlation ID is then put in the thread logging context.</p>
 */
@Component
public class HystrixRequestContextInitializerFilter implements Filter {
 
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // initialize hystrix context
        HystrixRequestContext context = HystrixRequestContext.initializeContext();

        // get correlation ID from header or generate one
        String correlationId = null;
        if (request instanceof HttpServletRequest) {
            correlationId = ((HttpServletRequest) request).getHeader(CorrelationId.HTTP_HEADER);
        }
        if (correlationId == null) {
            correlationId = UUID.randomUUID().toString();
        }

        // keep correlation ID as an hystrix request variable
        CorrelationId.set(correlationId);

        // update thread logging context with the correlation id
        CorrelationId.fillThreadDiagnosticContext();

        // resolve filter chain
        try {
            chain.doFilter(request, response);
        } finally {
            // close hystrix context
            context.close();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}
 
    @Override
    public void destroy() {}
 
}