package gs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>A simple controller, it exposes a "/demo" end point which calls a hystrix command.</p>
 */
@RestController
public class DemoRestController {

    private static Logger LOGGER = LoggerFactory.getLogger(DemoRestController.class);

    @Autowired
    private RemoteServiceAdapter adapter;

    @GetMapping("/demo")
    public void demo() {
        LOGGER.info("start processing GET /demo");
        // call hystrix command
        adapter.callRemote();
        LOGGER.info("end processing GET /demo");
    }

}
