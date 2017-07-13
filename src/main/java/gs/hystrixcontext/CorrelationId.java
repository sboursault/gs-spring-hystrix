package gs.hystrixcontext;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * <p>Holder for the HystrixRequestVariableDefault instance that contains the correlationId</p>.
 */
public class CorrelationId {

    private static Logger LOGGER = LoggerFactory.getLogger(CorrelationId.class);

    public final static String HTTP_HEADER = "X-Correlation-ID";
 
    private static final HystrixRequestVariableDefault<String> requestVariable = new HystrixRequestVariableDefault<>();
 
    private CorrelationId() {}

    public static String get() {
        try {
            return requestVariable.get();
        } catch (IllegalStateException e) {
            LOGGER.warn("could not get correlation id: " + e.getMessage());
            return null;
        }
    }

    public static void set(String value) {
        requestVariable.set(value);
    }

    public static void fillThreadDiagnosticContext() {
        MDC.put("correlationId", CorrelationId.get());
    }

    public static void clearThreadDiagnosticContext() {
        MDC.remove("correlationId");
    }

}