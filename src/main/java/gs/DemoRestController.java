package gs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sboursault on 7/7/17.
 */
@RestController
public class DemoRestController {

    @Autowired
    private RemoteServiceAdapter adapter;

    @RequestMapping("/demo")
    public void demo() {
        adapter.callRemote();
    }

}
