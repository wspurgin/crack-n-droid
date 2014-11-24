package com.kraqen.crackn;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;


public class MainActivity extends Activity {
    private final String LOGTAG = "CRN-Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOGTAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences = getSharedPreferences("CRN", Activity.MODE_PRIVATE);

        // Clear the persistent file store
        preferences.edit().clear().commit();

        CracknRestClient.usePersistentCookieStore(this.getApplicationContext());
        ImageButton logoButton = (ImageButton) findViewById(R.id.get_crakn_button);
        logoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onStart() {
        Log.i(LOGTAG, "onStart");
        super.onStart();
    }

    @Override
    public void onRestart() {
        Log.i(LOGTAG, "onRestart");
        super.onRestart();
    }

    @Override
    public void onResume() {
        Log.i(LOGTAG, "onResume");
        if (checkForUser()) {
            SharedPreferences preferences = getSharedPreferences("CRN", Activity.MODE_PRIVATE);
            JSONObject user = new JSONObject();
            try {
                user = new JSONObject(preferences.getString("user", "{}")).getJSONObject("user");
                Log.i(LOGTAG, user.toString());
            } catch (JSONException e) {
                Log.i(LOGTAG, e.getMessage());
            }
            // if the email isn't set then we know there is some error
            if (user.isNull("email")) {
                Log.i(LOGTAG, user.toString());
            }
            else {
                // Declare intent
                Intent intent = new Intent(this.getApplicationContext(), HomeActivity.class);
                try {
                    User temp = new User(user);
                    intent.putExtra("com.kraqen.crackn.User", temp.toString());
                    startActivity(intent);
                } catch (JSONException e) {
                    Log.i(LOGTAG, e.getMessage());
                }
            }
        }
        super.onResume();
    }

    private boolean checkForUser() {
        SharedPreferences preferences = getSharedPreferences("CRN", Activity.MODE_PRIVATE);
        return preferences.contains("user");
    }
}
