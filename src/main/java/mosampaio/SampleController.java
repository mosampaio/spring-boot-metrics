package mosampaio;

import com.codahale.metrics.annotation.Timed;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SampleController {

    @Timed
    @ResponseBody
    @RequestMapping("/")
    public String home() {
        return "Hello world!";
    }

}