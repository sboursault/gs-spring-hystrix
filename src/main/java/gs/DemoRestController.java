package gs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sboursault on 7/7/17.
 */
@RestController
public class DemoRestController {

    private static Logger LOGGER = LoggerFactory.getLogger(DemoRestController.class);

    @Autowired
    private RemoteServiceAdapter adapter;

    @GetMapping("/demo")
    public void demo() {
        LOGGER.info("GET /demo");
        adapter.callRemote();
    }

}
