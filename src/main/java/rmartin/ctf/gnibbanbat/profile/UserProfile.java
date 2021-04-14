package rmartin.ctf.gnibbanbat.profile;

public class UserProfile {
    private String userIP;
    private String name;
    private String avatarURL;
    private String backgroundURL;
    private String description;
    private String facebookURL;
    private String twitterURL;

    public UserProfile(String userIP, String name, String description, String avatarURL, String backgroundURL, String facebookURL, String twitterURL) {
        this.userIP = userIP;
        this.name = name;
        this.description = description;
        this.avatarURL = avatarURL;
        this.backgroundURL = backgroundURL;
        this.facebookURL = facebookURL;
        this.twitterURL = twitterURL;
    }

    public UserProfile(String userIP){
        this(userIP,
                "b3st h4ker 1n th3 w0rld",
                "CEH, Security+, CISSP, CISM, CISA, NCSF, CCSP, CHFI, CCNA, GSEC, OSCP, OSWE, OSWP",
                "https://img.etimg.com/thumb/width-640,height-480,imgsize-236554,resizemode-1,msid-75214600/tech/internet/hackers-are-a-busy-lot-in-these-lockdown-days/hacker-getty.jpg",
                "https://cdn.pixabay.com/photo/2020/06/15/15/34/fog-5302291_960_720.jpg",
                "https://facebook.com/b3sth4kerw0rld",
                "https://twitter.com/b3sth4kerw0rld"
        );
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "userIP='" + userIP + '\'' +
                ", name='" + name + '\'' +
                ", avatarURL='" + avatarURL + '\'' +
                ", backgroundURL='" + backgroundURL + '\'' +
                ", description='" + description + '\'' +
                ", facebookURL='" + facebookURL + '\'' +
                ", twitterURL='" + twitterURL + '\'' +
                '}';
    }

    public String getUserIP() {
        return userIP;
    }

    public String getName() {
        return name;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public String getBackgroundURL() {
        return backgroundURL;
    }

    public String getDescription() {
        return description;
    }

    public String getFacebookURL() {
        return facebookURL;
    }

    public String getTwitterURL() {
        return twitterURL;
    }
}
