package mobidev.com.dunstan.classes;

import android.app.Application;
import android.net.Uri;

public class AppManager extends Application{
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
