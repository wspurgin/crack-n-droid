package com.kraqen.crackn;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Will on 11/17/14.
 */
public class User implements Serializable {
    private String email;
    private String username;
    private String id;
    private String name;
    private String json;

    public User(String json) throws JSONException {
        this(new JSONObject(json));
    }

    public User(JSONObject user) throws JSONException {
        this.email = user.getString("email");
        this.username = user.getString("username");
        this.id = user.getString("_id");
        this.name = user.getString("name");
        this.json = user.toString();
    }

    public User(Parcel in) {
        this.email = in.readString();
        this.username = in.readString();
        this.id = in.readString();
        this.name = in.readString();
    }

    public String getEmail() {
        return this.email;
    }

    public String getUsername() {
        return this.username;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getJson() {
        return json;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String toString() {
        return this.json;
    }
}
