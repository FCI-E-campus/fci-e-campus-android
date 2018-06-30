package eg.edu.cu.fci.ecampus.fci_e_campus.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
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

import java.util.ArrayList;
import java.util.HashMap;

import eg.edu.cu.fci.ecampus.fci_e_campus.adapters.TasksAdapter;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Task;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.APIUtils;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.network.RequestQueueSingleton;

public class CourseTasksActivity extends AppCompatActivity {

    private String token, username, userType, courseCode, courseTitle;
    private TasksAdapter tasksAdapter;
    private ExpandableListView expListView;
    private ArrayList<String> listDataHeader;
    private HashMap<String, Task> listDataChild;
    private ArrayList<Task> tasks;
    private ProgressBar progressBar;
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
        setContentView(R.layout.activity_course_tasks);
        readUserDataFromSharedPreference();

        progressBar = findViewById(R.id.tasks_progress_bar);
        emptyStateTextView = findViewById(R.id.tasks_empty_title_text);
        emptyStateTextView.setVisibility(View.GONE);
        expListView = findViewById(R.id.tasks_expandable_list_view);

        courseCode = getIntent().getStringExtra("course_code");
        courseTitle = getIntent().getStringExtra("course_title");
        setTitle(courseTitle.toUpperCase() + " - Tasks");

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

        FloatingActionButton fab = findViewById(R.id.fab_tasks);
        if (userType.equals(getString(R.string.student_user_type))) {
            fab.setVisibility(View.GONE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseTasksActivity.this, AddTaskActivity.class);
                intent.putExtra("course_code", courseCode);
                intent.putExtra("course_title", courseTitle);
                startActivityForResult(intent, 1);
            }
        });
        getTasks();
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add("ERP Project - Phase 3");
        listDataHeader.add("Assignment-1");
        listDataHeader.add("Compilers Project - Phase 2");

        // Adding child data
        Task top250 = new Task();
        top250.setDescription("Node.js Phase of the Project");
        top250.setCreatorUsername("5 Nov");

        Task nowShowing = new Task();
        nowShowing.setDescription("Algorithms Assignment 1 about 3 Problems to be solved");
        nowShowing.setCreatorUsername("7 Nov");

        Task comingSoon = new Task();
        comingSoon.setDescription("Phase Lexical Analysis of the Compilers Project");
        comingSoon.setCreatorUsername("10 Nov");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }

    public void getTasks() {
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();
        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.course_prefix))
                .appendPath(getString(R.string.show_tasks_endpoint))
                .build();

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("_token", token);
            requestBody.put("course_code", courseCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest materialsRequest = new JsonObjectRequest(Request.Method.POST
                , uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        Gson gson = new Gson();
                        tasks = new ArrayList<>();
                        listDataHeader = new ArrayList<>();
                        listDataChild = new HashMap<>();
                        tasks = gson.fromJson(response.getJSONArray("result").toString(), new TypeToken<ArrayList<Task>>() {
                        }.getType());
                        for (int i = 0; i < tasks.size(); i++) {
                            Log.e("tasks", tasks.get(i).getName() + "");
                            listDataHeader.add(tasks.get(i).getName());
                            listDataChild.put(listDataHeader.get(i), tasks.get(i));
                        }
                        tasksAdapter = new TasksAdapter(CourseTasksActivity.this, listDataHeader, listDataChild);
                        progressBar.setVisibility(View.GONE);
                        expListView.setAdapter(tasksAdapter);
                        if (tasks.size() < 1) {
                            emptyStateTextView.setVisibility(View.VISIBLE);
                        }
                    } else if (response.getString("status").equals("failed")) {
                        progressBar.setVisibility(View.GONE);
                        int errorCode = response.getInt("error_code");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(CourseTasksActivity.this
                                , errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CourseTasksActivity.this
                            , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CourseTasksActivity.this
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(materialsRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                getTasks();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
