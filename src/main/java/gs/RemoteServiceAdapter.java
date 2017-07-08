package gs;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import gs.hystrixcontext.CorrelationIdRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class RemoteServiceAdapter {

    private static Logger LOGGER = LoggerFactory.getLogger(RemoteServiceAdapter.class);

    private final RestTemplate restTemplate;

    public RemoteServiceAdapter(RestTemplate rest) {
      this.restTemplate = rest;
    }

    @HystrixCommand(fallbackMethod = "degradedResponse")
    public String callRemote() {
        return this.restTemplate.getForObject(URI.create("http://remote/service"), String.class);
    }

    private String degradedResponse() {
        LOGGER.warn("going to degraded mode {correlationId: {} }", CorrelationIdRequestContext.get());
        return "...degraded hello...";
    }

}
