package eg.edu.cu.fci.ecampus.fci_e_campus.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Vector;

import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.activities.CourseActivity;
import eg.edu.cu.fci.ecampus.fci_e_campus.activities.JoinCourseActivity;
import eg.edu.cu.fci.ecampus.fci_e_campus.activities.LoginActivity;
import eg.edu.cu.fci.ecampus.fci_e_campus.activities.OverviewActivity;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.APIUtils;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.network.CustomJsonRequest;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.network.RequestQueueSingleton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyCoursesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyCoursesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyCoursesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Vector<String> coursesTitle;
    private Vector<String> coursesCode;
    private ProgressBar progressBar;
    private TextView emptyStateTextView;

    private OnFragmentInteractionListener mListener;

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
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyCoursesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyCoursesFragment newInstance(String param1, String param2) {
        MyCoursesFragment fragment = new MyCoursesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        readUserDataFromSharedPreference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_courses, container, false);

        progressBar = view.findViewById(R.id.my_courses_progress_bar);
        emptyStateTextView = view.findViewById(R.id.my_courses_empty_view);
        TextView yourCoursesTextView = view.findViewById(R.id.your_course_text_view);

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
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
        if (!userType.equals(getString(R.string.student_user_type))) {//If the user is Prof or TA
            fab.setVisibility(View.GONE);
        }
        coursesTitle = new Vector<>();
        coursesCode = new Vector<>();
        fillCourses();
        progressBar.setVisibility(View.GONE);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, coursesTitle);
        ListView listView1 = view.findViewById(R.id.list_my_courses);
        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), CourseActivity.class);
                intent.putExtra("course_name", coursesCode.get(i));
                startActivity(intent);
            }
        });
        return view;
    }

    private void getCourses() {
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(getContext()).getRequestQueue();
        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.course_prefix))
                .appendPath(getString(R.string.show_courses_for_student_endpoint))
                .build();

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("_token", "9Jdy4m4cVxa9YihZLakbHeBWII2gaYm0fgFNFEdj");
            Log.e("_token:", token);
            requestBody.put("STUDUSERNAME", username);
            Log.e("username:", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonRequest getCoursesRequest = new CustomJsonRequest(Request.Method.POST
                , uri.toString(), requestBody, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject course = response.getJSONObject(i);
                        coursesTitle.add(course.getString("COURSETITLE"));
                        coursesCode.add(course.getString("COURSECODE"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext()
                        , error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(getCoursesRequest);
    }

    private void fillCourses() {
        coursesCode.add("CS123");
        coursesCode.add("CS456");
        coursesCode.add("IS123");
        coursesCode.add("IS456");

        coursesTitle.add("Compilers");
        coursesTitle.add("Algorithms");
        coursesTitle.add("Analysis");
        coursesTitle.add("Database");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
