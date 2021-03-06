package eg.edu.cu.fci.ecampus.fci_e_campus.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.APIUtils;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.DateUtils;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.network.RequestQueueSingleton;

public class AddTaskActivity extends AppCompatActivity {

    private String token, username, userType, courseCode, courseTitle;
    private TextInputLayout taskHeaderTextInput;
    private TextInputLayout taskDescriptionTextInput;
    private TextInputLayout taskWeightTextInput;
    private TextInputLayout dueDateTextInputLayout;
    private TextInputLayout dueTimeTextInputLayout;

    private int year = -1, month = -1, day = -1;
    private String date, time;

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
        setContentView(R.layout.activity_add_task);

        readUserDataFromSharedPreference();

        courseCode = getIntent().getStringExtra("course_code");
        courseTitle = getIntent().getStringExtra("course_title");
        setTitle(courseTitle.toUpperCase() + "- Add Task");

        taskHeaderTextInput = findViewById(R.id.task_header_text_input);
        taskDescriptionTextInput = findViewById(R.id.task_description_text_input);
        taskWeightTextInput = findViewById(R.id.task_weight_text_input);

        ImageButton pickDateImageButton = findViewById(R.id.add_task_pick_date);
        ImageButton pickTimeImageButton = findViewById(R.id.add_task_pick_time);

        dueDateTextInputLayout = findViewById(R.id.add_task_due_date);
        dueTimeTextInputLayout = findViewById(R.id.add_task_due_time);

        Button publishButton = findViewById(R.id.add_task_publish_button);

        pickDateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        pickTimeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });
    }

    private void sendRequest() {
        if (taskHeaderTextInput.getEditText().getText().toString().trim().equals("")) {
            Toast.makeText(AddTaskActivity.this, getString(R.string.task_header_not_valid), Toast.LENGTH_SHORT).show();
            taskHeaderTextInput.getEditText().requestFocus();
            return;
        }
        if (taskDescriptionTextInput.getEditText().getText().toString().trim().equals("")) {
            Toast.makeText(AddTaskActivity.this, getString(R.string.task_description_not_valid), Toast.LENGTH_SHORT).show();
            taskDescriptionTextInput.getEditText().requestFocus();
            return;
        }
        if (taskWeightTextInput.getEditText().getText().toString().trim().equals("")) {
            Toast.makeText(AddTaskActivity.this, getString(R.string.task_weight_not_valid), Toast.LENGTH_SHORT).show();
            taskWeightTextInput.getEditText().requestFocus();
            return;
        }
        if (dueDateTextInputLayout.getEditText().getText().toString().trim().equals("")) {
            Toast.makeText(AddTaskActivity.this, getString(R.string.task_date_not_valid), Toast.LENGTH_SHORT).show();
            dueDateTextInputLayout.getEditText().requestFocus();
            return;
        }
        if (dueTimeTextInputLayout.getEditText().getText().toString().trim().equals("")) {
            Toast.makeText(AddTaskActivity.this, getString(R.string.task_time_not_valid), Toast.LENGTH_SHORT).show();
            dueTimeTextInputLayout.getEditText().requestFocus();
            return;
        }
        addTask();
    }

    private void addTask() {
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();
        Uri uri;
        if (userType.equals(getString(R.string.professor_user_type))) {
            uri = Uri.parse(getString(R.string.base_url))
                    .buildUpon()
                    .appendPath(getString(R.string.professor_prefix))
                    .appendPath(getString(R.string.add_task_endpoint))
                    .build();
        } else {
            uri = Uri.parse(getString(R.string.base_url))
                    .buildUpon()
                    .appendPath(getString(R.string.ta_prefix))
                    .appendPath(getString(R.string.add_task_endpoint))
                    .build();
        }

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("_token", token);
            requestBody.put("COURSECODE", courseCode);
            requestBody.put("TASKNAME", taskHeaderTextInput.getEditText().getText().toString());
            requestBody.put("DESCRIPTION", taskDescriptionTextInput.getEditText().getText().toString());
            Log.e("time", time);
            requestBody.put("DUEDATE", date + " " + time);
            requestBody.put("DATECREATED", DateUtils.getCurrentDate());
            requestBody.put("WEIGHT", taskWeightTextInput.getEditText().getText().toString());
            requestBody.put("USERNAME", username);
            if (userType.equals(getString(R.string.ta_user_type))) {
                requestBody.put("USERTYPE", "ta");
            } else {
                requestBody.put("USERTYPE", "prof");
            }
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
                        Toast.makeText(AddTaskActivity.this, "Task has been added successfully",
                                Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        setResult(RESULT_OK, returnIntent);
                        finish();

                    } else if (response.getString("status").equals("failed")) {
                        int errorCode = response.getInt("error_code");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(AddTaskActivity.this
                                , errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(AddTaskActivity.this
                            , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Toast.makeText(AddTaskActivity.this
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(addTaskRequest);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int Year, int monthOfYear, int dayOfMonth) {
                dueDateTextInputLayout.getEditText()
                        .setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + Year);
                year = Year;
                month = monthOfYear;
                day = dayOfMonth;
                Date d = new Date(year - 1900, month, day);
                date = DateUtils.convertCreatedDate(d);
            }
        }, year == -1 ? calendar.get(Calendar.YEAR) : year, month == -1 ? calendar.get(Calendar.MONTH) : month, day == -1 ? calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH) : day);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minuteOfHour) {
                Date timeDate = new Date();
                try {
                    timeDate = DateUtils.convertSlot(hourOfDay + ":" + minuteOfHour + ":00");
                    time = DateUtils.convertAddTask(timeDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    dueTimeTextInputLayout.getEditText()
                            .setText(DateUtils.convertSlot(timeDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, 12, 0, false);
        timePickerDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
        return true;
    }
}
