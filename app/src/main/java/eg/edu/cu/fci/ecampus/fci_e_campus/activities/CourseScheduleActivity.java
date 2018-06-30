package eg.edu.cu.fci.ecampus.fci_e_campus.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;

import eg.edu.cu.fci.ecampus.fci_e_campus.adapters.ForumAdapter;
import eg.edu.cu.fci.ecampus.fci_e_campus.adapters.ScheduleAdapter;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Forum;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Slot;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.APIUtils;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.network.RequestQueueSingleton;

public class CourseScheduleActivity extends AppCompatActivity {

    private String token, username, userType, courseCode, courseTitle;
    private ScheduleAdapter scheduleAdapter;
    private ArrayList<Slot> slots;
    private ProgressBar progressBar;
    private ListView scheduleListView;
    private TextView emptyStateTextView;

    private void readUserDataFromSharedPreference() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.authentication_shared_preference_file_name),
                Context.MODE_PRIVATE);

        token = sharedPref.getString(getString(R.string.saved_token_key), null);
        username = sharedPref.getString(getString(R.string.saved_username_key), null);
        userType = sharedPref.getString(getString(R.string.saved_user_type_key), null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_schedule);

        readUserDataFromSharedPreference();

        progressBar = findViewById(R.id.schedule_progress_bar);
        emptyStateTextView = findViewById(R.id.schedule_empty_title_text);
        emptyStateTextView.setVisibility(View.GONE);
        scheduleListView = findViewById(R.id.schedule_list_view);

        //Check Connectivity
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
        if (!isConnected) { //Not Connected to Internet
            progressBar.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_internet);
            return;
        }
        //Connected to Internet

        courseCode = getIntent().getStringExtra("course_code");
        courseTitle = getIntent().getStringExtra("course_title");
        setTitle(courseTitle.toUpperCase() + " - Schedule");

        getSchedule();

    }

    private void getSchedule() {
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();
        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.course_prefix))
                .appendPath(getString(R.string.show_schedule_endpoint))
                .build();

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("_token", token);
            requestBody.put("COURSECODE", courseCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest scheduleRequest = new JsonObjectRequest(Request.Method.POST
                , uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        Gson gson = new Gson();
                        slots = new ArrayList<>();
                        slots = gson.fromJson(response.getJSONArray("result").toString(), new TypeToken<ArrayList<Slot>>() {
                        }.getType());
                        scheduleAdapter = new ScheduleAdapter(CourseScheduleActivity.this, slots);
                        progressBar.setVisibility(View.GONE);
                        scheduleListView.setAdapter(scheduleAdapter);
                        emptyStateTextView.setVisibility(View.INVISIBLE);
                        if (slots.size() < 1) {
                            emptyStateTextView.setVisibility(View.VISIBLE);
                        }
                    } else if (response.getString("status").equals("failed")) {
                        progressBar.setVisibility(View.GONE);
                        int errorCode = response.getInt("error_code");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(CourseScheduleActivity.this
                                , errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CourseScheduleActivity.this
                            , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CourseScheduleActivity.this
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(scheduleRequest);
    }

    /*private void prepareData() {
        Time time = new Time(10, 30, 0);
        Slot slot = new Slot("Sunday", time, 1, 2, "Hall 1", "Cs120");
        slots = new ArrayList<>();
        slots.add(slot);
        slots.add(slot);
        slots.add(slot);
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
