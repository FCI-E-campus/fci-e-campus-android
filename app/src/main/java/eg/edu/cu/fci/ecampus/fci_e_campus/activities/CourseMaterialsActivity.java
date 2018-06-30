package eg.edu.cu.fci.ecampus.fci_e_campus.activities;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import eg.edu.cu.fci.ecampus.fci_e_campus.Adapters.MaterialsAdapter;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Material;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.APIUtils;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.network.RequestQueueSingleton;

public class CourseMaterialsActivity extends AppCompatActivity {

    private String token, username, userType, courseCode, courseTitle;
    private ArrayList<Material> materials;
    private MaterialsAdapter materialsAdapter;
    private ProgressBar progressBar;
    private ListView materialsListView;
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
        setContentView(R.layout.activity_course_materials);

        readUserDataFromSharedPreference();

        progressBar = findViewById(R.id.materials_progress_bar);
        emptyStateTextView = findViewById(R.id.materials_empty_title_text);
        emptyStateTextView.setVisibility(View.GONE);
        materialsListView = findViewById(R.id.material_list_view);

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
        setTitle(courseTitle.toUpperCase() + " - Materials");

        //getMaterials();
        prepare();
        materialsAdapter = new MaterialsAdapter(CourseMaterialsActivity.this, materials);
        progressBar.setVisibility(View.GONE);
        materialsListView.setAdapter(materialsAdapter);
        materialsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                checkForPermission(materials.get(i).getName(), materials.get(i).getLink(), materials.get(i).getDescription());
            }
        });
    }

    public void getMaterials() {
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();
        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.material_prefix))
                .appendPath(getString(R.string.show_official_materials_endpoint))
                .build();

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("_token", token);
            requestBody.put("COURSECODE", courseCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest materialsRequest = new JsonObjectRequest(Request.Method.POST
                , uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        Gson gson = new Gson();
                        materials = new ArrayList<>();
                        materials = gson.fromJson(response.getJSONArray("result").toString(), new TypeToken<ArrayList<Material>>() {
                        }.getType());
                        materialsAdapter = new MaterialsAdapter(CourseMaterialsActivity.this, materials);
                        progressBar.setVisibility(View.GONE);
                        materialsListView.setAdapter(materialsAdapter);
                        materialsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                checkForPermission(materials.get(i).getName(), materials.get(i).getLink(), materials.get(i).getDescription());
                            }
                        });
                        emptyStateTextView.setVisibility(View.INVISIBLE);
                        if (materials.size() < 1) {
                            emptyStateTextView.setVisibility(View.VISIBLE);
                        }
                    } else if (response.getString("status").equals("failed")) {
                        progressBar.setVisibility(View.GONE);
                        int errorCode = response.getInt("error_code");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(CourseMaterialsActivity.this
                                , errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CourseMaterialsActivity.this
                            , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CourseMaterialsActivity.this
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(materialsRequest);
    }

    public void prepare() {
        materials = new ArrayList<>();
        Material temp = new Material();
        temp.setName("Lecture 1");
        temp.setUploaderUsername("Nora Abdelhameed");
        temp.setDateString("2018-04-15 12:00:00");
        temp.setLink("https://firebasestorage.googleapis.com/v0/b/push-notification-575cf.appspot.com" +
                "/o/photos%2F143803?alt=media&token=d39b8510-b45b-441d-9c7e-a0d6e50eb458");
        Material temp1 = new Material();
        temp1.setName("Lecture 2");
        temp1.setUploaderUsername("Nora Abdelhameed");
        temp1.setDateString("2018-04-15 12:00:00");
        materials.add(temp);
        materials.add(temp1);
    }

    public String lastName;
    public String lastUrl;
    public String lastDescription;

    public void checkForPermission(String name, String url, String description) {
        // Here, thisActivity is the current activity
        lastName = name;
        lastUrl = url;
        lastDescription = description;
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            download(name, url, description);
        }
    }

    public void download(String name, String url, String description) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription(description);
        request.setTitle(name);
        // in order for this if to run, you must use the android 3.2 to compile your app
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name);

        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    download(lastName, lastUrl, lastDescription);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
