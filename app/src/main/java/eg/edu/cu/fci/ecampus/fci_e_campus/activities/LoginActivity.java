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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
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


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.ti_username)
    TextInputLayout usernameTextInput;
    @BindView(R.id.ti_password)
    TextInputLayout passwordTextInput;
    @BindView(R.id.login_teacher_type)
    LinearLayout teacherTypeView;
    @BindView(R.id.login_teacher_type_radio_group)
    RadioGroup teacherTypeRadioGroup;

    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        userType = getIntent().getStringExtra(WelcomeActivity.EXTRA_USER_TYPE);

        TextView signupLink = findViewById(R.id.link_signup);
        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                intent.putExtra(WelcomeActivity.EXTRA_USER_TYPE, userType);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        if (userType.equals(getString(R.string.student_user_type))) {
            teacherTypeView.setVisibility(View.GONE);
        }

        Button loginButton = findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        if (userType.equals(getString(R.string.student_user_type))) {
            // student
            studentLogin();
        } else if (teacherTypeRadioGroup.getCheckedRadioButtonId() == R.id.login_type_professor) {
            userType = getString(R.string.professor_user_type);
            // professor
            profLogin();
        } else if (teacherTypeRadioGroup.getCheckedRadioButtonId() == R.id.login_type_ta) {
            userType = getString(R.string.ta_user_type);
            // ta
            taLogin();
        }
    }

    private void studentLogin() {
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();
        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.student_prefix))
                .appendPath(getString(R.string.login_endpoint))
                .build();

        Log.d(TAG, uri.toString());

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("STUDUSERNAME", usernameTextInput.getEditText().getText().toString());
            requestBody.put("STUDPASSWORD", passwordTextInput.getEditText().getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST
                , uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getString("status").equals("success")) {
                        Toast.makeText(LoginActivity.this
                                , "Login successful!", Toast.LENGTH_SHORT).show();

                        // save auth data in shared preference
                        SharedPreferences authSharedPreferences
                                = getSharedPreferences(getString(R.string.authentication_shared_preference_file_name),
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = authSharedPreferences.edit();
                        String token = response.getString("token");
                        editor.putString(getString(R.string.saved_token_key), token);
                        editor.putString(getString(R.string.saved_username_key)
                                , usernameTextInput.getEditText().getText().toString());
                        editor.putString(getString(R.string.saved_password_key)
                                , passwordTextInput.getEditText().getText().toString());
                        editor.putString(getString(R.string.saved_user_type_key), userType);
                        editor.apply();

                        // redirect to overview activity
                        Intent intent = new Intent(LoginActivity.this, OverviewActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (response.getString("status").equals("failed")) {
                        int errorCode = response.getInt("error_code");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(LoginActivity.this
                                , errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d(TAG, e.toString());
                    Toast.makeText(LoginActivity.this
                            , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Log.d(TAG, error.toString());
                Toast.makeText(LoginActivity.this
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(loginRequest);
    }

    private void profLogin() {
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();
        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.professor_prefix))
                .appendPath(getString(R.string.login_endpoint))
                .build();

        Log.d(TAG, uri.toString());

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("PROFUSERNAME", usernameTextInput.getEditText().getText().toString());
            requestBody.put("PROFPASSWORD", passwordTextInput.getEditText().getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST
                , uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getString("status").equals("success")) {
                        Toast.makeText(LoginActivity.this
                                , "Login successful!", Toast.LENGTH_SHORT).show();

                        // save auth data in shared preference
                        SharedPreferences authSharedPreferences
                                = getSharedPreferences(getString(R.string.authentication_shared_preference_file_name),
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = authSharedPreferences.edit();
                        String token = response.getString("token");
                        editor.putString(getString(R.string.saved_token_key), token);
                        editor.putString(getString(R.string.saved_username_key)
                                , usernameTextInput.getEditText().getText().toString());
                        editor.putString(getString(R.string.saved_password_key)
                                , passwordTextInput.getEditText().getText().toString());
                        editor.putString(getString(R.string.saved_user_type_key), userType);
                        editor.apply();

                        // redirect to overview activity
                        Intent intent = new Intent(LoginActivity.this, OverviewActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (response.getString("status").equals("failed")) {
                        int errorCode = response.getInt("error_code");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(LoginActivity.this
                                , errorMessage, Toast.LENGTH_SHORT).show();
                        if (errorCode == 3) {
                            Intent intent = new Intent(LoginActivity.this, ActivationActivity.class);
                            intent.putExtra(getString(R.string.saved_username_key), usernameTextInput.getEditText().getText().toString());
                            intent.putExtra(getString(R.string.saved_password_key), passwordTextInput.getEditText().getText().toString());
                            intent.putExtra(WelcomeActivity.EXTRA_USER_TYPE, userType);
                            startActivity(intent);
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    Log.d(TAG, e.toString());
                    Toast.makeText(LoginActivity.this
                            , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Log.d(TAG, error.toString());
                Toast.makeText(LoginActivity.this
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(loginRequest);
    }

    private void taLogin() {
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();
        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.ta_prefix))
                .appendPath(getString(R.string.login_endpoint))
                .build();

        Log.d(TAG, uri.toString());

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("TAUSERNAME", usernameTextInput.getEditText().getText().toString());
            requestBody.put("TAPASSWORD", passwordTextInput.getEditText().getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST
                , uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getString("status").equals("success")) {
                        Toast.makeText(LoginActivity.this
                                , "Login successful!", Toast.LENGTH_SHORT).show();

                        // save auth data in shared preference
                        SharedPreferences authSharedPreferences
                                = getSharedPreferences(getString(R.string.authentication_shared_preference_file_name),
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = authSharedPreferences.edit();
                        String token = response.getString("token");
                        editor.putString(getString(R.string.saved_token_key), token);
                        editor.putString(getString(R.string.saved_username_key)
                                , usernameTextInput.getEditText().getText().toString());
                        editor.putString(getString(R.string.saved_password_key)
                                , passwordTextInput.getEditText().getText().toString());
                        editor.putString(getString(R.string.saved_user_type_key), userType);
                        editor.apply();

                        // redirect to overview activity
                        Intent intent = new Intent(LoginActivity.this, OverviewActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (response.getString("status").equals("failed")) {
                        int errorCode = response.getInt("error_code");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(LoginActivity.this
                                , errorMessage, Toast.LENGTH_SHORT).show();
                        if (errorCode == 3) {
                            Intent intent = new Intent(LoginActivity.this, ActivationActivity.class);
                            intent.putExtra(getString(R.string.saved_username_key), usernameTextInput.getEditText().getText().toString());
                            intent.putExtra(getString(R.string.saved_password_key), passwordTextInput.getEditText().getText().toString());
                            intent.putExtra(WelcomeActivity.EXTRA_USER_TYPE, userType);
                            startActivity(intent);
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    Log.d(TAG, e.toString());
                    Toast.makeText(LoginActivity.this
                            , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Log.d(TAG, error.toString());
                Toast.makeText(LoginActivity.this
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(loginRequest);
    }
}
