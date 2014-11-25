package com.kraqen.crackn;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;


public class HomeActivity extends Activity {
    private static final String LOGTAG = "CRN-Home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Fragment frag = new HomeFragment();
            frag.setArguments(new Bundle(intent.getExtras()));
            getFragmentManager().beginTransaction()
                    .add(R.id.container, frag)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

    /**
     * A fragment containing a simple view.
     */
    public static class HomeFragment extends Fragment {

        public HomeFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // return the root view
            return inflater.inflate(R.layout.fragment_home, container, false);
        }

        @Override
        public void onViewCreated(final View view, Bundle savedInstanceState) {
            try {
                Bundle args = getArguments();
                User user = new User(args.getString("com.kraqen.crackn.User"));
                TextView textView = (TextView) view.findViewById(R.id.home_greetings);
                textView.setText(user.getName().toCharArray(), 0, user.getName().length());
                CracknRestClient.get("projects", null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray JSONProjects) {
                        Log.i(LOGTAG, JSONProjects.toString());
                        ListView projectListing = (ListView) view.findViewById(R.id.gridView);

                        ArrayList<Project> projects = new ArrayList<Project>();
                        for (int i = 0; i < JSONProjects.length(); ++i) {
                            try {
                                projects.add(new Project(JSONProjects.getJSONObject(i)));
                            } catch (JSONException e) {
                                Log.i(LOGTAG, e.getMessage());
                            } catch (ParseException e) {
                                Log.i(LOGTAG, e.getMessage());
                            }
                        }
                        final ArrayAdapter<Project> adapter = new ArrayAdapter<Project>(
                                projectListing.getContext(),
                                android.R.layout.simple_list_item_1,
                                projects);
                        projectListing.setAdapter(adapter);
                    }
                });
            } catch (JSONException e) {
                Log.i(LOGTAG, e.getMessage());
            }
        }
    }
}
