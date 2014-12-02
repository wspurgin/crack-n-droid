package com.kraqen.crackn;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
        Log.i(LOGTAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Fragment frag = new HomeFragment();
            if (intent.getExtras() != null) {
                frag.setArguments(new Bundle(intent.getExtras()));
            }
            getFragmentManager().beginTransaction()
                    .add(R.id.container, frag)
                    .commit();
        } else {
            Log.i(LOGTAG, "Restoring state");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public void onResume() {
        Log.i(LOGTAG, "onResume");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        Log.i(LOGTAG, "onDestroy");
        super.onDestroy();
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
        private static String LOGTAG = "CRN-Home-Frag";

        private ArrayList<Project> projects;
        private User user;

        public HomeFragment() {
            this.projects = new ArrayList<Project>();
            this.user = null;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (savedInstanceState == null) Log.i(LOGTAG, "savedInstanceState is null");

            // return the root view
            return inflater.inflate(R.layout.fragment_home, container, false);
        }

        @Override
        public void onSaveInstanceState(Bundle state) {
            Log.i(LOGTAG, "onSavedInstanceState");
            super.onSaveInstanceState(state);
            // Save project list state
            state.putSerializable("projects", this.projects);
            state.putSerializable("user", this.user);
            Log.i(LOGTAG, state.toString());
        }

        @Override
        public void onViewCreated(final View view, Bundle savedInstanceState) {
            if (savedInstanceState == null) {
                try {
                    // initialize the state from intent and server queries
                    Bundle args = getArguments();
                    SharedPreferences preferences = getActivity().getSharedPreferences("CRN",
                            Activity.MODE_PRIVATE);
                    this.user = new User(new JSONObject(preferences.getString("user", "{}"))
                            .getJSONObject("user"));
                    TextView textView = (TextView) view.findViewById(R.id.home_greetings);
                    textView.setText(user.getName().toCharArray(), 0, user.getName().length());

                    // query for project list
                    CracknRestClient.get("projects", null, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray JSONProjects) {
                            Log.i(LOGTAG, JSONProjects.toString());
                            for (int i = 0; i < JSONProjects.length(); ++i) {
                                try {
                                    projects.add(new Project(JSONProjects.getJSONObject(i)));
                                } catch (JSONException e) {
                                    Log.i(LOGTAG, e.getMessage());
                                } catch (ParseException e) {
                                    Log.i(LOGTAG, e.getMessage());
                                }
                            }
                            buildView(view);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                              JSONObject errorResponse) {
                            Log.i(LOGTAG, throwable.getMessage(), throwable);
                            Log.i(LOGTAG, String.valueOf(statusCode).concat(errorResponse.toString()));
                        }
                    });
                } catch (JSONException e) {
                    Log.i(LOGTAG, e.getMessage());
                }
            } else {
                Log.i(LOGTAG, "Restoring from saved state");
                // restore project listing from saved state.
                this.projects = (ArrayList<Project>) savedInstanceState.getSerializable("projects");
                this.user = (User) savedInstanceState.getSerializable("user");
                buildView(view);
            }
        }

        @Override
        public void onDestroy() {
            Log.i(LOGTAG, "onDestroy");
            super.onDestroy();
        }

        private void buildView(final View view) {
            // now initialize the fragment view
            ListView projectListing = (ListView) view.findViewById(R.id.gridView);
            final ArrayAdapter<Project> adapter = new ArrayAdapter<Project>(
                    projectListing.getContext(),
                    android.R.layout.simple_list_item_1,
                    projects);
            projectListing.setAdapter(adapter);
            projectListing.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    final Project item = (Project) parent.getItemAtPosition(position);
                    Intent intent = new Intent(parent.getContext(),
                            MessageBoardActivity.class);
                    intent.putExtra("project", item);
                    startActivity(intent);
                }

            });
        }
    }
}
