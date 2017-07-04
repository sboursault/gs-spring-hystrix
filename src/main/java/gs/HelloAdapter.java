package gs;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class HelloAdapter {

  private final RestTemplate restTemplate;

  public HelloAdapter(RestTemplate rest) {
    this.restTemplate = rest;
  }

  @HystrixCommand(fallbackMethod = "degradedHello")
  public String callHello() {
    System.out.println("call hello api {correlationId: " + CorrelationIdRequestContext.get() + "}");
    return this.restTemplate.getForObject(URI.create("http://localhost:8090/hello"), String.class);
  }

  private String degradedHello() {
    System.out.println("going to degraded hello {correlationId: " + CorrelationIdRequestContext.get() + "}");
    return "...degraded hello...";
  }

}