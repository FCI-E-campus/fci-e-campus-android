package eg.edu.cu.fci.ecampus.fci_e_campus.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.APIUtils;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.network.RequestQueueSingleton;

public class AddPostActivity extends AppCompatActivity {

    private String token, username, userType, courseCode, courseTitle;
    private TextInputLayout postHeaderTextInput;
    private TextInputLayout postBodyTextInput;
    private Button publishButton;

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
        setContentView(R.layout.activity_add_post);

        readUserDataFromSharedPreference();

        courseCode = getIntent().getStringExtra("course_code");
        courseTitle = getIntent().getStringExtra("course_title");
        setTitle(courseTitle.toUpperCase() + "- Add Post");

        postHeaderTextInput = findViewById(R.id.post_header_text_input);
        postBodyTextInput = findViewById(R.id.post_body_text_input);
        publishButton = findViewById(R.id.add_post_publish_button);

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });
    }

    private void sendRequest() {
        if (postHeaderTextInput.getEditText().getText().toString().trim().equals("")) {
            Toast.makeText(AddPostActivity.this, getString(R.string.post_header_not_valid), Toast.LENGTH_SHORT).show();
            postHeaderTextInput.getEditText().requestFocus();
            return;
        }
        if (postBodyTextInput.getEditText().getText().toString().trim().equals("")) {
            Toast.makeText(AddPostActivity.this, getString(R.string.post_body_not_valid), Toast.LENGTH_SHORT).show();
            postBodyTextInput.getEditText().requestFocus();
            return;
        }

        addPost();
    }

    private void addPost() {
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();
        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.forum_prefix))
                .appendPath(getString(R.string.add_post_endpoint))
                .build();

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("_token", token);
            requestBody.put("author_username", username);
            if (userType.equals(getString(R.string.student_user_type))) {
                requestBody.put("author_type", "stud");
            } else if (userType.equals(getString(R.string.professor_user_type))) {
                requestBody.put("author_type", "prof");
            } else {
                requestBody.put("author_type", "ta");
            }
            requestBody.put("course_code", courseCode);
            requestBody.put("post_title", postHeaderTextInput.getEditText().getText().toString());
            requestBody.put("post_body", postBodyTextInput.getEditText().getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("request", requestBody.toString());
        JsonObjectRequest addTaskRequest = new JsonObjectRequest(Request.Method.POST
                , uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        Toast.makeText(AddPostActivity.this, "Post has been added successfully",
                                Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        setResult(RESULT_OK, returnIntent);
                        finish();

                    } else if (response.getString("status").equals("failed")) {
                        int errorCode = response.getInt("error_code");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(AddPostActivity.this
                                , errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(AddPostActivity.this
                            , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Toast.makeText(AddPostActivity.this
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(addTaskRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
