package gs.hystrixcontext;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;

/**
 * Holder for the HystrixRequestVariableDefault instance that contains the correlationId.
 */
public class CorrelationIdRequestContext {

    public final static String HTTP_HEADER = "X-Correlation-ID";
 
    private static final HystrixRequestVariableDefault<String> requestVariable = new HystrixRequestVariableDefault<>();
 
    private CorrelationIdRequestContext() {}

    public static String get() {
        return requestVariable.get();
    }

    public static void set(String value) {
        requestVariable.set(value);
    }

}