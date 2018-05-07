package co.biogram.main.ui.messager;

import android.opengl.Visibility;

public class Model_contact_chatlist {

    int profile;
    String Username,UserID;
    int select;

    public Model_contact_chatlist(int profile, String username, String userID, int select) {
        this.profile = profile;
        Username = username;
        UserID = userID;
        this.select = select;
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

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }
}
