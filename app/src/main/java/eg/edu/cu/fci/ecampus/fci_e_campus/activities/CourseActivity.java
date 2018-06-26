package eg.edu.cu.fci.ecampus.fci_e_campus.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import butterknife.BindView;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Course;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.APIUtils;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.network.RequestQueueSingleton;

public class CourseActivity extends AppCompatActivity {

    private TextView courseNameTextView;
    private TextView courseCodeTextView;
    private TextView courseDescriptionTextView;
    private TextView courseStaffTextView;

    private String courseCode;
    private Course course;

    private String token;

    private void readUserDataFromSharedPreference() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.authentication_shared_preference_file_name),
                Context.MODE_PRIVATE);

        token = sharedPref.getString(getString(R.string.saved_token_key), null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        course = new Course();
        courseNameTextView = findViewById(R.id.course_name);
        courseCodeTextView = findViewById(R.id.course_code);
        courseDescriptionTextView = findViewById(R.id.course_description);

        readUserDataFromSharedPreference();
        courseCode = getIntent().getStringExtra("course_code");
        setTitle(getIntent().getStringExtra("course_title").toUpperCase());
        getCourse();
        Button viewSchedule = findViewById(R.id.view_schedule_button);
        viewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseActivity.this, CourseScheduleActivity.class);
                intent.putExtra("course_title", courseCode);
                startActivity(intent);
            }
        });

        Button viewMaterials = findViewById(R.id.view_resources_button);
        viewMaterials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseActivity.this, CourseMaterialsActivity.class);
                intent.putExtra("course_title", courseCode);
                startActivity(intent);
            }
        });

        Button viewTasks = findViewById(R.id.view_tasks_button);
        viewTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseActivity.this, CourseTasksActivity.class);
                intent.putExtra("course_title", courseCode);
                startActivity(intent);
            }
        });
        Button openForum = findViewById(R.id.open_forum_button);
        openForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseActivity.this, CourseForumActivity.class);
                intent.putExtra("course_title", courseCode);
                startActivity(intent);
            }
        });
    }

    private void getCourse() {
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();
        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.course_prefix))
                .appendPath(getString(R.string.show_course_endpoint))
                .build();

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("_token", token);
            requestBody.put("COURSECODE", courseCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST
                , uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", response.toString());
                try {
                    if (response.getString("status").equals("success")) {
                        Gson gson = new Gson();
                        course = gson.fromJson(response.getJSONObject("result").getJSONObject("Course").toString(), Course.class);
                        course.setCode(courseCode);
                        courseNameTextView.setText(course.getTitle());
                        courseCodeTextView.setText(course.getCode());
                        courseDescriptionTextView.setText(course.getDescription());
                    } else if (response.getString("status").equals("failed")) {
                        int errorCode = response.getInt("error_code");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(CourseActivity.this
                                , errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("error1", e.toString());
                    Toast.makeText(CourseActivity.this
                            , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Log.e("error2", error.toString());
                Toast.makeText(CourseActivity.this
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(loginRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
