package com.kraqen.crackn;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple representation of a message
 *
 * Created by Will on 11/25/14.
 */
public class Message implements Serializable {
    private String _id;
    private String user_id;
    private String username;
    private String body;
    private Date timeSent;

    Message() {}

    Message(String id, String user_id, String username, String body, Date timeSent) {
        this._id = id;
        this.user_id = user_id;
        this.username = username;
        this.body = body;
        this.timeSent = timeSent;
    }

    Message(JSONObject message) throws JSONException, ParseException {
        this(
                message.getString("_id"),
                message.getString("user_id"),
                message.getString("username"),
                message.getString("body"),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(message.getString(
                        "timeSent"))
        );
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(Date timeSent) {
        this.timeSent = timeSent;
    }

    public String toString() {
        return String.format("%s: %s", this.username, this.body);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("_id", this._id);
        jsonMessage.put("user_id", this.user_id);
        jsonMessage.put("username", this.username);
        jsonMessage.put("body", this.body);
        jsonMessage.put("timeSent", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .format(this.timeSent));
        return jsonMessage;
    }
}
