package rmartin.ctf.gnibbanbat.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rmartin.ctf.gnibbanbat.profile.UserProfile;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

@Service
public class WebBotService {

    private static final Logger log = Logger.getLogger(WebBotService.class.getName());

    // @Value gets the value from environment/Docker, or defaults to noflag if not found
    @Value("jorge")//here is the flag
    String flag;
    @Value("${webdriver.url:http://127.0.0.1:4444}")
    String webdriverURL;
    @Value("${webserver.url:http://localhost:8080}")
    String webserverURL;

    private WebBot bot;
    private BlockingQueue<URLToVisit> queue = new LinkedBlockingQueue<>();
    private Set<String> requestingIPs = ConcurrentHashMap.newKeySet();

    @PostConstruct
    public void createBot() {
        this.bot = new WebBot(webdriverURL, flag, queue, requestingIPs);
        new Thread(this.bot).start();
    }

    // True if enqueued, false if rejected.
    // The request is rejected if the user profile is already in the queue
    public boolean visitProfile(UserProfile profile) {
        String ip = profile.getUserIP();
        boolean added = requestingIPs.add(ip);
        if (!added) return false;

        String url = webserverURL + "/user/" + ip;
        queue.add(new URLToVisit(url, ip));
        return true;
    }


}
