package gs.hystrixcontext;

import org.slf4j.MDC;

/**
 * Created by sboursault on 14/07/17.
 */
public class CorrelationIdLoggingContext {

    public static void initialize() {
        MDC.put("correlationId", CorrelationIdRequestContext.get());
    }

    public static void clear() {
        MDC.remove("correlationId");
    }
}
