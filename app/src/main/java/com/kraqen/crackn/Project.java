package com.kraqen.crackn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Project
 * A Java representation of a Crack'n Project
 *
 * Created by Will on 11/24/14.
 */
public class Project implements Serializable {
    private String _id;
    private String name;
    private Date endDate;
    private String owner;
    private boolean isFlaggedForRemoval;
    private Date startDate;
    private ArrayList<Message> messages;
    // We can ignore the members and phases for the purposes of this app

    // static final format for use in constructors and setters with strings
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    Project() {
        this.messages = new ArrayList<Message>();
    }

    Project(String id, String name, Date endDate, String owner, boolean isFlaggedForRemoval,
            Date startDate, ArrayList<Message> messages) {

        this._id = id;
        this.name = name;
        this.endDate = endDate;
        this.owner = owner;
        this.isFlaggedForRemoval = isFlaggedForRemoval;
        this.startDate = startDate;
        this.messages = messages;
    }

    Project(JSONObject project) throws JSONException, ParseException {
        this._id = project.getString("_id");
        this.name = project.getString("name");
        this.endDate = dateFormat.parse(project.getString("endDate"));
        this.owner = project.getString("owner");
        this.isFlaggedForRemoval = project.getBoolean("isFlaggedForRemoval");
        this.startDate = dateFormat.parse(project.getString("startDate"));

        // initialize messages
        this.messages = new ArrayList<Message>();
        JSONArray JSONMessages = project.getJSONArray("messages");
        for (int i = 0; i < JSONMessages.length(); i++) {
            this.messages.add(new Message(JSONMessages.getJSONObject(i)));
        }
    }

    public String get_id() { return this._id; }

    public String getName() { return this.name; }

    public Date getStartDate() { return this.startDate; }

    public Date getEndDate() { return this.endDate; }

    public String getOwner() { return this.owner; }

    public boolean isFlaggedForRemoval() { return this.isFlaggedForRemoval; }

    public ArrayList<Message> getMessages() { return messages; }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setEndDate(String endDate) throws ParseException{
        this.endDate = dateFormat.parse(endDate);
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setFlaggedForRemoval(boolean isFlaggedForRemoval) {
        this.isFlaggedForRemoval = isFlaggedForRemoval;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setStartDate(String startDate) throws ParseException {
        this.startDate = dateFormat.parse(startDate);
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public String toString() {
        return this.name;
    }
}
