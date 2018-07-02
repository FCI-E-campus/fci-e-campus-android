package eg.edu.cu.fci.ecampus.fci_e_campus.activities;

import android.content.Context;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.APIUtils;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.network.RequestQueueSingleton;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    @BindView(R.id.ti_current_password) TextInputLayout currentPasswordTextInput;
    @BindView(R.id.ti_new_password) TextInputLayout newPasswordTextInput;
    @BindView(R.id.btn_change_password) Button changePasswordButton;

    private String token, username, currentPassword, userType;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        readUserDataFromSharedPreferences();

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void readUserDataFromSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.authentication_shared_preference_file_name),
                Context.MODE_PRIVATE);

        token = sharedPref.getString(getString(R.string.saved_token_key), null);
        username = sharedPref.getString(getString(R.string.saved_username_key), null);
        currentPassword = sharedPref.getString(getString(R.string.saved_password_key), null);
        userType = sharedPref.getString(getString(R.string.saved_user_type_key), null);

        Log.d(TAG, username);
        Log.d(TAG, token);
    }

    private void changePassword() {

        String currentPasswordEntered = currentPasswordTextInput.getEditText().getText().toString();
        final String newPasswordEntered = newPasswordTextInput.getEditText().getText().toString();

        if (currentPassword.equals(currentPasswordEntered)) {

            if (newPasswordEntered.equals(currentPassword)) {
                Toast.makeText(this, "The new password is the same as the current password!"
                        , Toast.LENGTH_SHORT).show();
                return;
            }

            RequestQueue requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();

            if (userType.equals(getString(R.string.student_user_type))) {
                changeStudentPassword(newPasswordEntered, requestQueue);

            }
            else if (userType.equals(getString(R.string.professor_user_type))) {
                changeProfPassword(newPasswordEntered, requestQueue);

            }
            else if (userType.equals(getString(R.string.ta_user_type))) {
                changeTAPassword(newPasswordEntered, requestQueue);

            }
        }
        else {
            Toast.makeText(this, "The current password you entered is incorrect!"
                    , Toast.LENGTH_SHORT).show();
        }

    }

    private void changeTAPassword(final String newPasswordEntered, RequestQueue requestQueue) {
        // TA
        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.ta_prefix))
                .appendPath(getString(R.string.change_password_endpoint))
                .build();

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("_token", token);
            requestBody.put("TAUSERNAME", username);
            requestBody.put("TAPASSWORD", newPasswordEntered);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest changePasswordRequest = new JsonObjectRequest(Request.Method.POST,
                uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        Toast.makeText(SettingsActivity.this, "Password changed successfully!"
                                , Toast.LENGTH_SHORT).show();
                        resetPasswordEditTexts();

                        // update the password in shared preferences
                        SharedPreferences authSharedPreferences
                                = getSharedPreferences(getString(R.string.authentication_shared_preference_file_name),
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = authSharedPreferences.edit();
                        editor.putString(getString(R.string.saved_password_key), newPasswordEntered);
                        editor.apply();

                        currentPassword = newPasswordEntered;

                    } else if (response.getString("status").equals("failed")) {
                        int errorCode = response.getInt("error_code");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(SettingsActivity.this
                                , errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Log.d(TAG, error.toString());
                Toast.makeText(SettingsActivity.this
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();

            }
        });

        requestQueue.add(changePasswordRequest);
    }

    private void changeProfPassword(final String newPasswordEntered, RequestQueue requestQueue) {
        // Professor
        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.professor_prefix))
                .appendPath(getString(R.string.change_password_endpoint))
                .build();

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("_token", token);
            requestBody.put("PROFUSERNAME", username);
            requestBody.put("PROFPASSWORD", newPasswordEntered);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest changePasswordRequest = new JsonObjectRequest(Request.Method.POST,
                uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        Toast.makeText(SettingsActivity.this, "Password changed successfully!"
                                , Toast.LENGTH_SHORT).show();
                        resetPasswordEditTexts();

                        // update the password in shared preferences
                        SharedPreferences authSharedPreferences
                                = getSharedPreferences(getString(R.string.authentication_shared_preference_file_name),
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = authSharedPreferences.edit();
                        editor.putString(getString(R.string.saved_password_key), newPasswordEntered);
                        editor.apply();

                        currentPassword = newPasswordEntered;


                    } else if (response.getString("status").equals("failed")) {
                        int errorCode = response.getInt("error_code");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(SettingsActivity.this
                                , errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Log.d(TAG, error.toString());
                Toast.makeText(SettingsActivity.this
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();

            }
        });

        requestQueue.add(changePasswordRequest);
    }

    private void changeStudentPassword(final String newPasswordEntered, RequestQueue requestQueue) {
        // Student
        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.student_prefix))
                .appendPath(getString(R.string.change_password_endpoint))
                .build();

        Log.d(TAG, uri.toString());

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("_token", token);
            requestBody.put("STUDUSERNAME", username);
            requestBody.put("STUDPASSWORD", newPasswordEntered);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest changePasswordRequest = new JsonObjectRequest(Request.Method.POST,
                uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        Toast.makeText(SettingsActivity.this, "Password changed successfully!"
                                , Toast.LENGTH_SHORT).show();
                        resetPasswordEditTexts();

                        // update the password in shared preferences
                        SharedPreferences authSharedPreferences
                                = getSharedPreferences(getString(R.string.authentication_shared_preference_file_name),
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = authSharedPreferences.edit();
                        editor.putString(getString(R.string.saved_password_key), newPasswordEntered);
                        editor.apply();

                        currentPassword = newPasswordEntered;

                    } else if (response.getString("status").equals("failed")) {
                        int errorCode = response.getInt("error_code");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(SettingsActivity.this
                                , errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Log.d(TAG, error.toString());
                Toast.makeText(SettingsActivity.this
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();

            }
        });

        requestQueue.add(changePasswordRequest);
    }

    private void resetPasswordEditTexts() {
        currentPasswordTextInput.getEditText().getText().clear();
        currentPasswordTextInput.clearFocus();

        newPasswordTextInput.getEditText().getText().clear();
        newPasswordTextInput.clearFocus();
    }
}
