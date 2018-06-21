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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.APIUtils;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.network.RequestQueueSingleton;

public class JoinCourseActivity extends AppCompatActivity {

    @BindView(R.id.course_code_text_input)
    TextInputLayout courseCodeTextInput;
    @BindView(R.id.pass_code_text_input)
    TextInputLayout passCodeTextInput;
    @BindView(R.id.join_button)
    Button joinButton;

    private String token, username;

    private void readUserDataFromSharedPreference() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.authentication_shared_preference_file_name),
                Context.MODE_PRIVATE);

        token = sharedPref.getString(getString(R.string.saved_token_key), null);
        username = sharedPref.getString(getString(R.string.saved_username_key), null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_course);
        ButterKnife.bind(this);
        readUserDataFromSharedPreference();
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinCourse();
            }
        });
    }

    private void joinCourse() {
        if (courseCodeTextInput.getEditText().getText().toString().trim().equals("")) {
            Toast.makeText(JoinCourseActivity.this, getString(R.string.course_code_not_valid), Toast.LENGTH_SHORT).show();
            courseCodeTextInput.getEditText().requestFocus();
            return;
        }
        if (passCodeTextInput.getEditText().getText().toString().trim().equals("")) {
            Toast.makeText(JoinCourseActivity.this, getString(R.string.pass_code_not_valid), Toast.LENGTH_SHORT).show();
            passCodeTextInput.getEditText().requestFocus();
            return;
        }
        sendRequest();
    }

    private void sendRequest() {
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();
        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.course_prefix))
                .appendPath(getString(R.string.join_course_endpoint))
                .build();

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("_token", token);
            requestBody.put("Username", username);
            requestBody.put("CourseCode", courseCodeTextInput.getEditText().getText().toString());
            requestBody.put("PassCode", passCodeTextInput.getEditText().getText().toString());
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
                        Toast.makeText(JoinCourseActivity.this
                                , "Joined successful!", Toast.LENGTH_SHORT).show();
                        onSupportNavigateUp();
                    } else if (response.getString("status").equals("failed")) {
                        int errorCode = response.getInt("error_msg");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(JoinCourseActivity.this
                                , errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("error1", e.toString());
                    Toast.makeText(JoinCourseActivity.this
                            , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Log.e("error2", error.toString());
                Toast.makeText(JoinCourseActivity.this
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
