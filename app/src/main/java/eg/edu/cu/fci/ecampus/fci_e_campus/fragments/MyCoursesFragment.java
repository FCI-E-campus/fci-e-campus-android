package eg.edu.cu.fci.ecampus.fci_e_campus.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.activities.CourseActivity;
import eg.edu.cu.fci.ecampus.fci_e_campus.activities.JoinCourseActivity;
import eg.edu.cu.fci.ecampus.fci_e_campus.adapters.CoursesAdapter;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.CourseShow;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.APIUtils;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.network.RequestQueueSingleton;

import static android.app.Activity.RESULT_OK;

public class MyCoursesFragment extends Fragment {

    private ListView coursesListView;
    private ProgressBar progressBar;
    private TextView emptyStateTextView;
    private TextView yourCoursesTextView;
    private CoursesAdapter courseAdapter;
    private ArrayList<CourseShow> courses;

    private String token, username, userType;

    private void readUserDataFromSharedPreference() {
        SharedPreferences sharedPref = getContext().getSharedPreferences(
                getString(R.string.authentication_shared_preference_file_name),
                Context.MODE_PRIVATE);

        token = sharedPref.getString(getString(R.string.saved_token_key), null);
        username = sharedPref.getString(getString(R.string.saved_username_key), null);
        userType = sharedPref.getString(getString(R.string.saved_user_type_key), null);
    }

    public MyCoursesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment MyCoursesFragment.
     */
    public static MyCoursesFragment newInstance(String param1, String param2) {
        MyCoursesFragment fragment = new MyCoursesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readUserDataFromSharedPreference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_courses, container, false);

        progressBar = view.findViewById(R.id.my_courses_progress_bar);
        emptyStateTextView = view.findViewById(R.id.my_courses_empty_title_text);
        yourCoursesTextView = view.findViewById(R.id.your_course_text_view);

        //Check Connectivity
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
        if (!isConnected) { //Not Connected to Internet
            progressBar.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_internet);
            yourCoursesTextView.setVisibility(View.GONE);
            return view;
        }
        //Connected to Internet

        //Floating Button to Join courses for Student Only !!
        FloatingActionButton fab = view.findViewById(R.id.fab_my_courses);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), JoinCourseActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        if (!userType.equals(getString(R.string.student_user_type))) {//If the user is Prof or TA
            fab.setVisibility(View.GONE);
        }

        emptyStateTextView.setVisibility(View.INVISIBLE);
        yourCoursesTextView.setVisibility(View.INVISIBLE);
        coursesListView = view.findViewById(R.id.list_my_courses);
        coursesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), CourseActivity.class);
                intent.putExtra("course_code", courses.get(i).getCode());
                intent.putExtra("course_title", courses.get(i).getTitle());
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getCourses();
    }

    private void getCourses() {
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(getContext()).getRequestQueue();
        Uri uri;
        if (userType.equals(getString(R.string.student_user_type))) {
            uri = Uri.parse(getString(R.string.base_url))
                    .buildUpon()
                    .appendPath(getString(R.string.course_prefix))
                    .appendPath(getString(R.string.show_courses_for_student_endpoint))
                    .build();
        } else if (userType.equals(getString(R.string.ta_user_type))) {
            uri = Uri.parse(getString(R.string.base_url))
                    .buildUpon()
                    .appendPath(getString(R.string.course_prefix))
                    .appendPath(getString(R.string.show_courses_for_ta_endpoint))
                    .build();
        } else {
            uri = Uri.parse(getString(R.string.base_url))
                    .buildUpon()
                    .appendPath(getString(R.string.course_prefix))
                    .appendPath(getString(R.string.show_courses_for_prof_endpoint))
                    .build();
        }

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("_token", token);
            if (userType.equals(getString(R.string.student_user_type))) {
                requestBody.put("STUDUSERNAME", username);
            } else if (userType.equals(getString(R.string.ta_user_type))) {
                requestBody.put("TAUSERNAME", username);
            } else {
                requestBody.put("PROFUSERNAME", username);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest allCoursesRequest = new JsonObjectRequest(Request.Method.POST
                , uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        Gson gson = new Gson();
                        courses = new ArrayList<>();
                        courses = gson.fromJson(response.getJSONArray("result").toString(), new TypeToken<ArrayList<CourseShow>>() {
                        }.getType());
                        courseAdapter = new CoursesAdapter(getActivity(), courses);
                        progressBar.setVisibility(View.GONE);
                        coursesListView.setAdapter(courseAdapter);
                        yourCoursesTextView.setVisibility(View.VISIBLE);
                        emptyStateTextView.setVisibility(View.INVISIBLE);
                        if (courses.size() < 1) {
                            emptyStateTextView.setVisibility(View.VISIBLE);
                            yourCoursesTextView.setVisibility(View.GONE);
                        }
                    } else if (response.getString("status").equals("failed")) {
                        int errorCode = response.getInt("error_msg");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(getContext()
                                , errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("Hahaha", e.toString());
                    Toast.makeText(getContext()
                            , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Log.e("Hahaha", error.toString());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext()
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(allCoursesRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                getCourses();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
