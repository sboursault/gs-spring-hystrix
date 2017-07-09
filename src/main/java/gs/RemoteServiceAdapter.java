package gs;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class RemoteServiceAdapter {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand
    public void callRemote() {
        restTemplate.getForObject(URI.create("http://remote/service"), String.class);
    }

}
