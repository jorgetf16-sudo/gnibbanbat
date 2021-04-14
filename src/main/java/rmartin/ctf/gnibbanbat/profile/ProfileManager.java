package rmartin.ctf.gnibbanbat.profile;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProfileManager {
    private final Map<String, UserProfile> profiles = new ConcurrentHashMap<>();

    public UserProfile getOrCreateProfile(String ip){
        profiles.computeIfAbsent(ip, UserProfile::new);
        return profiles.get(ip);
    }

    public UserProfile updateProfile(String ip, String name, String description, String avatarURL, String backgroundURL, String facebookURL, String twitterURL){
        var updatedProfile = new UserProfile(ip, name, description, avatarURL, backgroundURL, facebookURL, twitterURL);
        profiles.put(ip, updatedProfile);
        return updatedProfile;
    }
}
