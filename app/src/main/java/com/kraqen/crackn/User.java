package com.kraqen.crackn;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Will on 11/17/14.
 */
public class User {
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

    public String toString() {
        return this.json;
    }
}
