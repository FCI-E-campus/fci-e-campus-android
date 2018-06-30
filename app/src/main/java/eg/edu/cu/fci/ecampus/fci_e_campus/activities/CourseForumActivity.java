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
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.Date;

import eg.edu.cu.fci.ecampus.fci_e_campus.Adapters.ForumAdapter;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Forum;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.APIUtils;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.network.RequestQueueSingleton;

public class CourseForumActivity extends AppCompatActivity {

    private String token, username, userType, courseCode, courseTitle;
    private ForumAdapter forumAdapter;
    private ArrayList<Forum> forums;
    private ProgressBar progressBar;
    private ListView forumsListView;
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
        setContentView(R.layout.activity_course_forum);

        readUserDataFromSharedPreference();

        progressBar = findViewById(R.id.forum_progress_bar);
        emptyStateTextView = findViewById(R.id.forum_empty_title_text);
        emptyStateTextView.setVisibility(View.GONE);
        forumsListView = findViewById(R.id.course_forum_list_view);

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
        setTitle(courseTitle.toUpperCase() + " - Forum");


        getForum();

        forumsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CourseForumActivity.this, ForumPostActivity.class);
                intent.putExtra("forum", forums.get(i));
                intent.putExtra("course_code", courseCode);
                intent.putExtra("course_title", courseTitle);
                startActivityForResult(intent, 1);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab_course_forum);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseForumActivity.this, AddPostActivity.class);
                intent.putExtra("course_code", courseCode);
                intent.putExtra("course_title", courseTitle);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void getForum() {
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();
        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.course_prefix))
                .appendPath(getString(R.string.show_forum_endpoint))
                .build();

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("_token", token);
            requestBody.put("course_code", courseCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest forumRequest = new JsonObjectRequest(Request.Method.POST
                , uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        Gson gson = new Gson();
                        forums = new ArrayList<>();
                        forums = gson.fromJson(response.getJSONArray("result").toString(), new TypeToken<ArrayList<Forum>>() {
                        }.getType());
                        forumAdapter = new ForumAdapter(CourseForumActivity.this, forums);
                        progressBar.setVisibility(View.GONE);
                        forumsListView.setAdapter(forumAdapter);
                        emptyStateTextView.setVisibility(View.INVISIBLE);
                        if (forums.size() < 1) {
                            emptyStateTextView.setVisibility(View.VISIBLE);
                        }
                    } else if (response.getString("status").equals("failed")) {
                        progressBar.setVisibility(View.GONE);
                        int errorCode = response.getInt("error_code");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(CourseForumActivity.this
                                , errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CourseForumActivity.this
                            , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CourseForumActivity.this
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(forumRequest);
    }

    private void prepareListData() {
        Date date = new Date(100, 0, 15);
        Forum forum1 = new Forum("Lexical Analyzer", "some long text here, some long text here, some long text here, some long text here, some long text here, some long text here," +
                " some long text here, some long text here, some long text here", date, "ahmedotto", 1);
        Forum forum2 = new Forum("Lexical Analyzer", "some long text here, some long text here, some long text here, some long text here, some long text here, some long text here," +
                " some long text here, some long text here, some long text here", date, "ahmedotto", 0);
        Forum forum3 = new Forum("Lexical Analyzer", "some long text here, some long text here, some long text here, some long text here, some long text here, some long text here," +
                " some long text here, some long text here, some long text here", date, "ahmedotto", 1);
        forums = new ArrayList<>();
        forums.add(forum1);
        forums.add(forum2);
        forums.add(forum3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                progressBar.setVisibility(View.VISIBLE);
                getForum();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
