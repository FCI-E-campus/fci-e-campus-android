package eg.edu.cu.fci.ecampus.fci_e_campus.activities;

import android.app.Activity;
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
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.adapters.TasksAdapter;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.APIUtils;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.DateUtils;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.network.RequestQueueSingleton;

public class AddMaterialActivity extends AppCompatActivity {

    private String token, username, userType, courseCode, courseTitle;
    private TextInputLayout materialHeaderTextInput;
    private TextInputLayout materialDescriptionTextInput;
    private ImageButton pickFileImageButton;
    private TextInputLayout fileNameTextInputLayout;
    private Button publishButton;
    private RadioGroup materialTypeRadioGroup;
    private Uri filePath;
    private String uriString;
    private String materialType;
    private StorageReference storageReference;

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
        setContentView(R.layout.activity_add_material);

        readUserDataFromSharedPreference();

        storageReference = FirebaseStorage.getInstance().getReference();

        courseCode = getIntent().getStringExtra("course_code");
        courseTitle = getIntent().getStringExtra("course_title");
        setTitle(courseTitle.toUpperCase() + "- Add Material");

        materialTypeRadioGroup = findViewById(R.id.material_type_radio_group);
        materialHeaderTextInput = findViewById(R.id.material_header_text_input);
        materialDescriptionTextInput = findViewById(R.id.material_description_text_input);
        pickFileImageButton = findViewById(R.id.add_material_pick);
        fileNameTextInputLayout = findViewById(R.id.add_material_file_path);
        publishButton = findViewById(R.id.add_material_publish_button);

        pickFileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilePickerBuilder.getInstance().setMaxCount(1)
                        .setActivityTheme(R.style.LibAppTheme)
                        .pickFile(AddMaterialActivity.this);
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
        if (materialHeaderTextInput.getEditText().getText().toString().trim().equals("")) {
            Toast.makeText(AddMaterialActivity.this, getString(R.string.material_header_not_valid), Toast.LENGTH_SHORT).show();
            materialHeaderTextInput.getEditText().requestFocus();
            return;
        }
        if (materialDescriptionTextInput.getEditText().getText().toString().trim().equals("")) {
            Toast.makeText(AddMaterialActivity.this, getString(R.string.material_description_not_valid), Toast.LENGTH_SHORT).show();
            materialDescriptionTextInput.getEditText().requestFocus();
            return;
        }
        if (fileNameTextInputLayout.getEditText().getText().toString().trim().equals("")) {
            Toast.makeText(AddMaterialActivity.this, getString(R.string.material_file_not_valid), Toast.LENGTH_SHORT).show();
            fileNameTextInputLayout.getEditText().requestFocus();
            return;
        }
        uploadFile();
    }

    private void uploadFile() {
        Toast.makeText(AddMaterialActivity.this, "Please wait until the file to be uploaded", Toast.LENGTH_SHORT).show();
        StorageReference reference = storageReference.child("files").child(filePath.getLastPathSegment());
        reference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(AddMaterialActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                        uriString = uri.toString();
                        addMaterial();
                    }
                });
            }
        });
    }

    private void addMaterial() {
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();
        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.material_prefix))
                .appendPath(getString(R.string.upload_material_endpoint))
                .build();

        final JSONObject requestBody = new JSONObject();
        try {
            if (materialTypeRadioGroup.getCheckedRadioButtonId() == R.id.add_material_type_lecture) {
                materialType = "lec";
            } else if (materialTypeRadioGroup.getCheckedRadioButtonId() == R.id.add_material_type_lab) {
                materialType = "lab";
            } else {
                materialType = "sec";
            }
            requestBody.put("_token", token);
            requestBody.put("COURSECODE", courseCode);
            requestBody.put("USERNAME", username);
            requestBody.put("MATERIALNAME", materialHeaderTextInput.getEditText().getText().toString());
            requestBody.put("MATERIALDESCRIPTION", materialDescriptionTextInput.getEditText().getText().toString());
            requestBody.put("MATERIALFILEPATH", uriString);
            requestBody.put("MATERIALTYPE", materialType);
            requestBody.put("DATE", DateUtils.getCurrentTime());
            if (userType.equals(getString(R.string.ta_user_type))) {
                requestBody.put("USERTYPE", "ta");
            } else {
                requestBody.put("USERTYPE", "prof");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest materialsRequest = new JsonObjectRequest(Request.Method.POST
                , uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        Intent returnIntent = new Intent();
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    } else if (response.getString("status").equals("failed")) {
                        int errorCode = response.getInt("error_code");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(AddMaterialActivity.this
                                , errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(AddMaterialActivity.this
                            , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Toast.makeText(AddMaterialActivity.this
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(materialsRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_DOC:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    filePath = Uri.parse("file://" + data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS).get(0));
                    fileNameTextInputLayout.getEditText().setText(filePath.getLastPathSegment());
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
