package gs.hystrixcontext;

import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;

/**
 * <p>HystrixCommandExecutionHook used on hystrix child threads to
 * fill the Mapped Diagnostic Context with a correlation ID.</p>
 */
public class AddCorrelationIdToDiagnosticContext extends HystrixCommandExecutionHook {
    @Override
    public <T> void onThreadStart(HystrixInvokable<T> commandInstance) {
        CorrelationId.fillThreadDiagnosticContext();
    }

    @Override
    public <T> void onThreadComplete(HystrixInvokable<T> commandInstance) {
        CorrelationId.clearThreadDiagnosticContext();
    }
}
