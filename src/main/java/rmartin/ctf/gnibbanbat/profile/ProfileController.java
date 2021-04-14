package rmartin.ctf.gnibbanbat.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import rmartin.ctf.gnibbanbat.bot.WebBotService;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@Controller
public class ProfileController {

    private static final Logger log = Logger.getLogger(WebBotService.class.getName());

    @Autowired
    ProfileManager profileManager;

    @Autowired
    WebBotService botService;

    @GetMapping("/profile")
    public ModelAndView renderProfile(ModelAndView mv, HttpServletRequest request){
        var userIP = request.getRemoteAddr();
        var profileData = profileManager.getOrCreateProfile(userIP);
        log.info(String.format("Returning profile for IP %s", userIP));
        mv.setViewName("profile");
        mv.addObject("profile", profileData);
        return mv;
    }

    @PostMapping("/profile")
    public String updateProfile(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String avatarURL,
            @RequestParam String backgroundURL,
            @RequestParam String twitterURL,
            @RequestParam String facebookURL,
            HttpServletRequest request
    ){
        if(!(
            isValid(facebookURL) &&
            isValid(twitterURL) &&
            isValid(backgroundURL) &&
            isValid(avatarURL)
        )){
            throw new IllegalArgumentException("Arguments failed validation");
        }

        var userIP = request.getRemoteAddr();
        var profileData = profileManager.updateProfile(userIP, name, description, avatarURL, backgroundURL, facebookURL, twitterURL);
        log.info(String.format("Updating profile for IP %s, new profile %s", userIP, profileData));
        return "redirect:/profile";
    }

    private boolean isValid(String url) {
        if(!url.startsWith("http")){
            return false;
        }
        if(url.contains("script")){
            return false;
        }
        if (!url.contains("/")){
            return false;
        }
        return true;
    }

    @GetMapping("/enqueue")
    public ResponseEntity<String> enqueue(HttpServletRequest request){
        var userProfile = profileManager.getOrCreateProfile(request.getRemoteAddr());
        var added = botService.visitProfile(userProfile);
        if(!added){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Bot already has a pending visit to your profile, wait a bit");
        } else {
            return ResponseEntity.ok("Added to queue, bot will visit your profile shortly");
        }
    }

    @GetMapping("/user/{ip}")
    public ModelAndView viewUserProfile(@PathVariable String ip){
        var userProfile = profileManager.getOrCreateProfile(ip);
        var mv = new ModelAndView();
        mv.setViewName("viewprofile");
        mv.addObject("profile", userProfile);
        return mv;
    }

    @PostMapping("/user/{ip}")
    public String resultForUser(@PathVariable String ip, HttpServletRequest request, @RequestParam String keywords){
        log.info(String.format("Recieved results from %s, for user %s --> keywords %s", request.getRemoteAddr(), ip, keywords));
        return "redirect:/";
    }


}
