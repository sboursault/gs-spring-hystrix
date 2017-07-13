package gs;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class RemoteServiceAdapter {

    private static Logger LOGGER = LoggerFactory.getLogger(RemoteServiceAdapter.class);

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand
    public void callRemote() {
        LOGGER.info("call remote service");
        restTemplate.getForObject(URI.create("http://remote/service"), String.class);
    }

}
