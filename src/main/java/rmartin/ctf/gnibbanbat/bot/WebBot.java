package rmartin.ctf.gnibbanbat.bot;


import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class WebBot implements Runnable {

    private static final Logger log = Logger.getLogger(WebBot.class.getName());
    private final String flag;
    private final BlockingQueue<URLToVisit> queue;
    private final Set<String> requestingIPs;
    private final String webdriverUrl;
    private volatile boolean stopping = false;
    private Pattern twitterUserPattern = Pattern.compile("@[a-zA-Z0-9_]{1,15}");

    public WebBot(String webdriverUrl, String flag, BlockingQueue<URLToVisit> queue, Set<String> requestingIPs) {
        this.flag = flag;
        this.queue = queue;
        this.requestingIPs = requestingIPs;
        this.webdriverUrl = webdriverUrl;
    }

    @Override
    public void run() {
        while (!stopping) {
            URLToVisit next;
            // Sleep until an element is available or we are interrupted and have to shutdown
            try {
                next = queue.take();
            } catch (InterruptedException e) {
                stopping = true;
                break;
            }

            try {
                var driver = getDriver();
                scrapeTwitterProfile(next, driver);
            } catch (InterruptedException e) {
                stopping = true;
            } catch (Exception e) {
                log.warning(String.format("Error while visiting URL %s, submitted by IP %s: %s", next.url, next.userIP, e));
            } finally {
                // Remove IP from locked IPs to allow the user to request a new visit
                requestingIPs.remove(next.userIP);
            }
        }
    }

    private void scrapeTwitterProfile(URLToVisit next, WebDriver driver) throws InterruptedException {
        // Load user profile using /user/ endpoint
        // Example of user profile URL: https://challenge3.web/user/192.168.1.1
        driver.get(next.url);

        // Wait 3 seconds until page loads
        Thread.sleep(3000);

        // Sleep a bit more just in case? Sleep is good
        Thread.sleep(2000);

        // Find text area
        var textarea = driver.findElement(By.id("keywords"));
        // Delete text in text area
        textarea.clear();
        // Simulate keyboard and type the following text
        textarea.sendKeys("Hi, I am a bot beep boop");
        textarea.sendKeys("The challenge flag is: .... wait no, this would be too similar to the HackOn challenge :)"); //

        // Submit form
        driver.findElement(By.id("submit")).click();

        // Wait a bit and cleanup
        Thread.sleep(2000);
        driver.quit();
    }

    private WebDriver getDriver() {
        // Create a new WebDriver for each user/request just in case, do not reuse
        try {
            // Use the remote firefox browser
            var options = new FirefoxOptions();
            options.addPreference("general.useragent.override", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)" + flag);
            var webDriver = new RemoteWebDriver(new URL(webdriverUrl), options);
            return webDriver;
        } catch (MalformedURLException e) {
            // Should never happen if the URL in the docker-compose.yml is well formed, just in case
            throw new RuntimeException(e);
        }
    }

    public void requestStop() {
        this.stopping = true;
    }
}
