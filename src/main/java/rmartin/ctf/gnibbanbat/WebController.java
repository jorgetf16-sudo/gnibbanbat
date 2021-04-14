package rmartin.ctf.gnibbanbat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import rmartin.ctf.gnibbanbat.bot.WebBotService;

import java.util.logging.Logger;

@Controller
public class WebController {
    private static final Logger log = Logger.getLogger(WebController.class.getName());

    @Autowired
    WebBotService webBotService;

    @GetMapping("/")
    String index(){
        return "index";
    }

    @GetMapping("/error")
    ModelAndView error(ModelAndView mv){
        mv.setViewName("error");
        return mv;
    }

}
