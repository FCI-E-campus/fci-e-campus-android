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
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.APIUtils;

public class ActivationActivity extends AppCompatActivity {

    @BindView(R.id.activation_key)
    TextInputLayout keyTextInput;
    @BindView(R.id.btn_active)
    Button activateButton;

    String username;
    String password;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);
        ButterKnife.bind(this);
        username = getIntent().getStringExtra(getString(R.string.saved_username_key));
        Log.d("Hello",username);
        password = getIntent().getStringExtra(getString(R.string.saved_password_key));
        Log.d("Hello",password);
        userType = getIntent().getStringExtra(WelcomeActivity.EXTRA_USER_TYPE);
        Log.d("Hello",userType);

        activateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activateAccount();
            }
        });
    }

    private void activateAccount() {
        String key = keyTextInput.getEditText().getText().toString();
        if (key.trim().equals("")) {
            Toast.makeText(this, "Key is Required", Toast.LENGTH_SHORT).show();
            keyTextInput.getEditText().requestFocus();
            return;
        }

        if (userType.equals(getString(R.string.professor_user_type))) {
            profActivation(key);
        } else if (userType.equals(getString(R.string.ta_user_type))) {
            taActivation(key);
        }
    }

    private void profActivation(String key) {
        RequestQueue requestQueue = Volley.newRequestQueue(ActivationActivity.this);
        JSONObject requestBody = new JSONObject();
        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.professor_prefix))
                .appendPath(getString(R.string.activate_endpoint))
                .build();
        try {
            requestBody.put("PROFUSERNAME", username);
            requestBody.put("ActivationCode", key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest activationRequest = new JsonObjectRequest(Request.Method.POST
                , uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        Toast.makeText(ActivationActivity.this
                                , "Activated successful!", Toast.LENGTH_SHORT).show();

                        // save auth data in shared preference
                        SharedPreferences authSharedPreferences
                                = getSharedPreferences(getString(R.string.authentication_shared_preference_file_name),
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = authSharedPreferences.edit();
                        String token = response.getString("token");
                        editor.putString(getString(R.string.saved_token_key), token);
                        editor.putString(getString(R.string.saved_username_key)
                                , username);
                        editor.putString(getString(R.string.saved_password_key)
                                , password);
                        editor.putString(getString(R.string.saved_user_type_key), userType);
                        editor.apply();

                        // redirect to overview activity
                        Intent intent = new Intent(ActivationActivity.this, OverviewActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (response.getString("status").equals("failed")) {
                        int errorCode = response.getInt("error_code");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(ActivationActivity.this
                                , errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(ActivationActivity.this
                            , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Toast.makeText(ActivationActivity.this
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(activationRequest);
    }

    private void taActivation(String key) {
        RequestQueue requestQueue = Volley.newRequestQueue(ActivationActivity.this);
        JSONObject requestBody = new JSONObject();

        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.ta_prefix))
                .appendPath(getString(R.string.activate_endpoint))
                .build();
        try {
            requestBody.put("TAUSERNAME", username);
            requestBody.put("ActivationCode", key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest activationRequest = new JsonObjectRequest(Request.Method.POST
                , uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        Toast.makeText(ActivationActivity.this
                                , "Activated successful!", Toast.LENGTH_SHORT).show();

                        // save auth data in shared preference
                        SharedPreferences authSharedPreferences
                                = getSharedPreferences(getString(R.string.authentication_shared_preference_file_name),
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = authSharedPreferences.edit();
                        String token = response.getString("token");
                        editor.putString(getString(R.string.saved_token_key), token);
                        editor.putString(getString(R.string.saved_username_key)
                                , username);
                        editor.putString(getString(R.string.saved_password_key)
                                , password);
                        editor.putString(getString(R.string.saved_user_type_key), userType);
                        editor.apply();

                        // redirect to overview activity
                        Intent intent = new Intent(ActivationActivity.this, OverviewActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (response.getString("status").equals("failed")) {
                        int errorCode = response.getInt("error_code");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(ActivationActivity.this
                                , errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(ActivationActivity.this
                            , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Toast.makeText(ActivationActivity.this
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(activationRequest);
    }
}
