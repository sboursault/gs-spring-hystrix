package gs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by seb on 7/7/17.
 */
@RestController
public class DemoRestController {

    @Autowired
    private RemoteServiceAdapter helloAdapter;

    @RequestMapping("/demo")
    public String demo() {
        return helloAdapter.callRemote();
    }

}
