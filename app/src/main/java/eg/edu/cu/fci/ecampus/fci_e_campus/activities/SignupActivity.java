package eg.edu.cu.fci.ecampus.fci_e_campus.activities;

import android.content.Intent;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.APIUtils;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = SignupActivity.class.getSimpleName();

    @BindString(R.string.student_user_type)
    String studentUserType;
    @BindString(R.string.teacher_user_type)
    String teacherUserType;

    @BindView(R.id.ti_first_name)
    TextInputLayout firstNameTextInput;
    @BindView(R.id.ti_last_name)
    TextInputLayout lastNameTextInput;
    @BindView(R.id.ti_email)
    TextInputLayout emailTextInput;
    @BindView(R.id.ti_phone_number)
    TextInputLayout phoneNumberTextInput;
    @BindView(R.id.ti_date_of_birth)
    TextInputLayout dateOfBirthTextInput;
    @BindView(R.id.ti_username)
    TextInputLayout usernameTextInput;
    @BindView(R.id.ti_password)
    TextInputLayout passwordTextInput;
    @BindView(R.id.ti_faculty_Id)
    TextInputLayout facultyIdTextInput;
    @BindView(R.id.major_dept_radio_group)
    RadioGroup majorDeptRadioGroup;
    @BindView(R.id.minor_dept_radio_group)
    RadioGroup minorDeptRadioGroup;
    @BindView(R.id.minor_dept)
    LinearLayout minorView;
    @BindView(R.id.signup_teacher_type)
    LinearLayout teacherTypeView;
    @BindView(R.id.signup_teacher_type_radio_group)
    RadioGroup teacherTypeRadioGroup;
    @BindView(R.id.major_department_tv)
    TextView majorDeptTextView;

    private String userType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        userType = getIntent().getStringExtra(WelcomeActivity.EXTRA_USER_TYPE);

        TextView loginLink = findViewById(R.id.link_login);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                intent.putExtra(WelcomeActivity.EXTRA_USER_TYPE, userType);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });


        initializeActivityForUser();

        Button signupButton = findViewById(R.id.btn_signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

    }

    private void initializeActivityForUser() {
        Log.d(TAG, userType);
        if (userType.equals(studentUserType)) {
            // student
            teacherTypeView.setVisibility(View.GONE);
        } else if (userType.equals(teacherUserType)) {
            // teacher
            facultyIdTextInput.setVisibility(View.GONE);
            minorView.setVisibility(View.GONE);
            majorDeptTextView.setText("Department");
        }
    }

    private void signup() {
        if (userType.equals(getString(R.string.student_user_type))) {
            studentSignup();
        } else if (teacherTypeRadioGroup.getCheckedRadioButtonId() == R.id.signup_type_professor) {
            Log.d(TAG, "Professor Signup");
            userType = getString(R.string.professor_user_type);
            // professor
            profSignup();
        } else if (teacherTypeRadioGroup.getCheckedRadioButtonId() == R.id.signup_type_ta) {
            Log.d(TAG, "TA Signup");
            userType = getString(R.string.ta_user_type);
            // ta
            taSignup();
        }
    }

    private void studentSignup() {
        RequestQueue requestQueue = Volley.newRequestQueue(SignupActivity.this);
        JSONObject requestBody = new JSONObject();
        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.student_prefix))
                .appendPath(getString(R.string.signup_endpoint))
                .build();

        Log.d(TAG, uri.toString());

        try {
            // username
            requestBody.put("STUDUSERNAME", usernameTextInput.getEditText().getText().toString());

            // first name
            requestBody.put("FIRSTNAME", firstNameTextInput.getEditText().getText().toString());

            // last name
            requestBody.put("LASTNAME", lastNameTextInput.getEditText().getText().toString());

            // email
            requestBody.put("EMAIL", emailTextInput.getEditText().getText().toString());

            // phone number
            requestBody.put("PHONENUMBER", phoneNumberTextInput.getEditText().getText().toString());

            // date of birth
            requestBody.put("DATEOFBIRTH", dateOfBirthTextInput.getEditText().getText().toString());

            // major dept.
            String majorDept = null;
            int majorDeptRadioButton = majorDeptRadioGroup.getCheckedRadioButtonId();
            if (majorDeptRadioButton == R.id.major_general) {
                majorDept = getString(R.string.general_major_dept_id);
            } else if (majorDeptRadioButton == R.id.major_cs) {
                majorDept = getString(R.string.cs_dept_id);
            } else if (majorDeptRadioButton == R.id.major_is) {
                majorDept = getString(R.string.is_dept_id);
            } else if (majorDeptRadioButton == R.id.major_it) {
                majorDept = getString(R.string.it_dept_id);
            } else if (majorDeptRadioButton == R.id.major_ds) {
                majorDept = getString(R.string.ds_dept_id);
            }
            requestBody.put("DEPTID", majorDept);

            // minor dept.
            String minorDept = null;
            int minorDeptRadioButton = minorDeptRadioGroup.getCheckedRadioButtonId();
            if (minorDeptRadioButton == R.id.minor_general) {
                minorDept = getString(R.string.general_minor_dept_id);
            } else if (minorDeptRadioButton == R.id.minor_cs) {
                minorDept = getString(R.string.cs_dept_id);
            } else if (minorDeptRadioButton == R.id.minor_is) {
                minorDept = getString(R.string.is_dept_id);
            } else if (minorDeptRadioButton == R.id.minor_it) {
                minorDept = getString(R.string.it_dept_id);
            } else if (minorDeptRadioButton == R.id.minor_ds) {
                minorDept = getString(R.string.ds_dept_id);
            }
            requestBody.put("DEP_DEPTID", minorDept);

            // password
            requestBody.put("STUDPASSWORD", passwordTextInput.getEditText().getText().toString());

            // faculty id
            requestBody.put("FACULTYID", facultyIdTextInput.getEditText().getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, requestBody.toString());
        JsonObjectRequest signupRequest = new JsonObjectRequest(Request.Method.POST
                , uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getString("status").equals("success")) {
                        Toast.makeText(SignupActivity.this
                                , "Account created Successfully! Please login.", Toast.LENGTH_SHORT).show();

                        // redirect to login activity
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        intent.putExtra(WelcomeActivity.EXTRA_USER_TYPE, userType);
                        startActivity(intent);
                        finish();
                    } else if (response.getString("status").equals("failed")) {
                        int errorCode = response.getInt("error_code");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(SignupActivity.this
                                , errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(SignupActivity.this
                            , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Log.d(TAG, error.toString());
                Toast.makeText(SignupActivity.this
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();

            }
        });

        requestQueue.add(signupRequest);
    }

    private void profSignup() {
        RequestQueue requestQueue = Volley.newRequestQueue(SignupActivity.this);
        JSONObject requestBody = new JSONObject();
        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.professor_prefix))
                .appendPath(getString(R.string.signup_endpoint))
                .build();

        Log.d(TAG, uri.toString());

        try {
            // username
            requestBody.put("PROFUSERNAME", usernameTextInput.getEditText().getText().toString());

            // first name
            requestBody.put("FIRSTNAME", firstNameTextInput.getEditText().getText().toString());

            // last name
            requestBody.put("LASTNAME", lastNameTextInput.getEditText().getText().toString());

            // email
            requestBody.put("EMAIL", emailTextInput.getEditText().getText().toString());

            // phone number
            requestBody.put("PHONENUMBER", phoneNumberTextInput.getEditText().getText().toString());

            // date of birth
            requestBody.put("DATEOFBIRTH", dateOfBirthTextInput.getEditText().getText().toString());

            // password
            requestBody.put("PROFPASSWORD", passwordTextInput.getEditText().getText().toString());

            // major dept.
            String majorDept = null;
            int majorDeptRadioButton = majorDeptRadioGroup.getCheckedRadioButtonId();
            if (majorDeptRadioButton == R.id.major_general) {
                majorDept = getString(R.string.general_major_dept_id);
            } else if (majorDeptRadioButton == R.id.major_cs) {
                majorDept = getString(R.string.cs_dept_id);
            } else if (majorDeptRadioButton == R.id.major_is) {
                majorDept = getString(R.string.is_dept_id);
            } else if (majorDeptRadioButton == R.id.major_it) {
                majorDept = getString(R.string.it_dept_id);
            } else if (majorDeptRadioButton == R.id.major_ds) {
                majorDept = getString(R.string.ds_dept_id);
            }
            requestBody.put("DEPTID", Integer.parseInt(majorDept));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, requestBody.toString());
        JsonObjectRequest signupRequest = new JsonObjectRequest(Request.Method.POST
                , uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getString("status").equals("success")) {
                        Toast.makeText(SignupActivity.this
                                , "Account created Successfully! Check your mail box for activation e-mail."
                                , Toast.LENGTH_SHORT).show();

                        // redirect to activation activity
                        Intent intent = new Intent(SignupActivity.this, ActivationActivity.class);
                        intent.putExtra(getString(R.string.saved_username_key), usernameTextInput.getEditText().getText().toString());
                        intent.putExtra(getString(R.string.saved_password_key), passwordTextInput.getEditText().getText().toString());
                        intent.putExtra(WelcomeActivity.EXTRA_USER_TYPE, userType);
                        startActivity(intent);
                        finish();
                    } else if (response.getString("status").equals("failed")) {
                        int errorCode = response.getInt("error_code");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(SignupActivity.this
                                , errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("Damn", e.toString());
                    Toast.makeText(SignupActivity.this
                            , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                error.printStackTrace();
                Toast.makeText(SignupActivity.this
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(signupRequest);
    }

    private void taSignup() {
        RequestQueue requestQueue = Volley.newRequestQueue(SignupActivity.this);
        JSONObject requestBody = new JSONObject();
        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.ta_prefix))
                .appendPath(getString(R.string.signup_endpoint))
                .build();

        Log.d(TAG, uri.toString());

        try {
            // username
            requestBody.put("TAUSERNAME", usernameTextInput.getEditText().getText().toString());

            // first name
            requestBody.put("FIRSTNAME", firstNameTextInput.getEditText().getText().toString());

            // last name
            requestBody.put("LASTNAME", lastNameTextInput.getEditText().getText().toString());

            // email
            requestBody.put("EMAIL", emailTextInput.getEditText().getText().toString());

            // phone number
            requestBody.put("PHONENUMBER", phoneNumberTextInput.getEditText().getText().toString());

            // date of birth
            requestBody.put("DATEOFBIRTH", dateOfBirthTextInput.getEditText().getText().toString());

            // password
            requestBody.put("TAPASSWORD", passwordTextInput.getEditText().getText().toString());

            // major dept.
            String majorDept = null;
            int majorDeptRadioButton = majorDeptRadioGroup.getCheckedRadioButtonId();
            if (majorDeptRadioButton == R.id.major_general) {
                majorDept = getString(R.string.general_major_dept_id);
            } else if (majorDeptRadioButton == R.id.major_cs) {
                majorDept = getString(R.string.cs_dept_id);
            } else if (majorDeptRadioButton == R.id.major_is) {
                majorDept = getString(R.string.is_dept_id);
            } else if (majorDeptRadioButton == R.id.major_it) {
                majorDept = getString(R.string.it_dept_id);
            } else if (majorDeptRadioButton == R.id.major_ds) {
                majorDept = getString(R.string.ds_dept_id);
            }
            requestBody.put("DEPTID", Integer.parseInt(majorDept));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, requestBody.toString());
        JsonObjectRequest signupRequest = new JsonObjectRequest(Request.Method.POST
                , uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getString("status").equals("success")) {
                        Toast.makeText(SignupActivity.this
                                , "Account created Successfully! Check your mail box for activation e-mail."
                                , Toast.LENGTH_SHORT).show();

                        // redirect to activation activity
                        Intent intent = new Intent(SignupActivity.this, ActivationActivity.class);
                        intent.putExtra(getString(R.string.saved_username_key), usernameTextInput.getEditText().getText().toString());
                        intent.putExtra(getString(R.string.saved_password_key), passwordTextInput.getEditText().getText().toString());
                        intent.putExtra(WelcomeActivity.EXTRA_USER_TYPE, userType);
                        startActivity(intent);
                        finish();
                    } else if (response.getString("status").equals("failed")) {
                        int errorCode = response.getInt("error_code");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(SignupActivity.this
                                , errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("Damn", e.toString());
                    Toast.makeText(SignupActivity.this
                            , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                error.printStackTrace();
                Toast.makeText(SignupActivity.this
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(signupRequest);
    }
}
