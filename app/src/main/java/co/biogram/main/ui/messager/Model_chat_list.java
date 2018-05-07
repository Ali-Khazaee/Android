package co.biogram.main.ui.messager;

public class Model_chat_list {

    int profile;
    String Username,Endmessage,cunter,Etime;

    public Model_chat_list(int profile, String username, String endmessage, String cunter, String etime) {
        this.profile = profile;
        Username = username;
        Endmessage = endmessage;
        this.cunter = cunter;
        Etime = etime;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEndmessage() {
        return Endmessage;
    }

    public void setEndmessage(String endmessage) {
        Endmessage = endmessage;
    }

    public String getCunter() {
        return cunter;
    }

    public void setCunter(String cunter) {
        this.cunter = cunter;
    }

    public String getEtime() {
        return Etime;
    }

    public void setEtime(String etime) {
        Etime = etime;
    }
}
