package com.kraqen.crackn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;


public class MessageBoardActivity extends Activity {

    private Project project;
    private User user;
    private static String LOGTAG = "CRN-MessageBoard";
    private ListView messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_board);
        Intent intent = getIntent();
        this.project = (Project) intent.getSerializableExtra("project");
        this.user = (User) intent.getSerializableExtra("user");

        // Initialize List view of messages
        messageList = (ListView) findViewById(R.id.messageList);
        final ArrayAdapter<Message> adapter = new ArrayAdapter<Message>(
                this,
                android.R.layout.simple_list_item_1,
                project.getMessages()
        );
        adapter.setNotifyOnChange(true);
        messageList.setAdapter(adapter);

        //Add click listener event for send button
        Button sendButton = (Button) findViewById(R.id.sendMessage);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meassage_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void pollMessages() {

    }

    public void sendMessage() {
        String postMessageUrl = String.format("projects/%s/messages", this.project.get_id());
        final EditText text = (EditText) findViewById(R.id.messageText);
        if (text.getText().length() == 0) return;

        // Construct data model of message
        final Message message = new Message("", this.user.getId(), this.user.getUsername(),
                text.getText().toString(), new Date());

        // add message to message listing
        final ArrayAdapter<Message> adapter = (ArrayAdapter<Message>) messageList.getAdapter();
        adapter.add(message); // notify on change is called internally in this case, see onCreate
        final int pos = adapter.getCount() - 1;

        // Send post request with message
        try {
            CracknRestClient.post(postMessageUrl, message.toJSON(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        // clear text on success
                        text.getText().clear();
                        Message message = adapter.getItem(pos);
                        message.set_id(response.getString("_id"));
                    } catch (JSONException e) {
                        Log.i(LOGTAG, e.getMessage(), e);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                      JSONObject errorResponse) {
                    Log.i(LOGTAG, throwable.getMessage(), throwable);
                    messageSendFailure();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.i(LOGTAG, responseString);
                    messageSendFailure();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.i(LOGTAG, errorResponse.toString(), throwable);
                    messageSendFailure();
                }

                private void messageSendFailure() {
                    // for now remove the message :(
                    adapter.remove(message);
                }
            });
        } catch (JSONException e) {
            Log.i(LOGTAG, e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            Log.i(LOGTAG, e.getMessage(), e);
        }
    }
}
