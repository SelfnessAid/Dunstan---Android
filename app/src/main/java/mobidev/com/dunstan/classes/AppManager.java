package mobidev.com.dunstan.classes;

import android.app.Application;
import android.net.Uri;

import java.util.ArrayList;

public class AppManager extends Application{
    private String token;
    private ArrayList<Question> questions = new ArrayList<Question>();
    private ArrayList<User> users = new ArrayList<User>();
    private ArrayList<Door> doors = new ArrayList<Door>();

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }
}