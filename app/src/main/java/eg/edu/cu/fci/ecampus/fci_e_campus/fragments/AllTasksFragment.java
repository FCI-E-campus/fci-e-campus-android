package eg.edu.cu.fci.ecampus.fci_e_campus.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.adapters.AllTasksAdapter;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Task;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.network.RequestQueueSingleton;

public class AllTasksFragment extends Fragment {

    private static final String TAG = AllTasksFragment.class.getSimpleName();

    @BindView(R.id.rv_all_tasks) RecyclerView allTasksRecyclerView;
    @BindView(R.id.pb_all_tasks) ProgressBar loadingProgressBar;
    @BindView(R.id.tv_no_tasks_msg) TextView noTasksMsgTextView;

    private List<Task> allTasks;
    private AllTasksAdapter allTasksAdapter;

    String token, username;

    public AllTasksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     * @return A new instance of fragment AllTasksFragment.
     */
    public static AllTasksFragment newInstance() {
        AllTasksFragment fragment = new AllTasksFragment();
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
        View fragmentView = inflater.inflate(R.layout.fragment_all_tasks, container, false);
        ButterKnife.bind(this, fragmentView);

        configureAllTasksRecyclerView();

        return fragmentView;
    }

    private void configureAllTasksRecyclerView() {
        allTasksRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        allTasksRecyclerView.setLayoutManager(layoutManager);

        allTasks = new ArrayList<>();
        allTasksAdapter = new AllTasksAdapter(allTasks);
        allTasksRecyclerView.setAdapter(allTasksAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                layoutManager.getOrientation());
        allTasksRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.student_prefix))
                .appendPath(getString(R.string.get_all_tasks_endpoint))
                .build();

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("_token", token);
            requestBody.put("STUDUSERNAME", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest getAllTasksRequest = new JsonObjectRequest(Request.Method.POST, uri.toString()
                , requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingProgressBar.setVisibility(View.INVISIBLE);

                try {
                    if (response.getString("status").equals("success")) {
                        JSONArray result = response.getJSONArray("result");
                        for (int i = 0; i<result.length(); i++) {
                            JSONObject courseTasksJsonObj = result.getJSONObject(i);
                            Iterator<String> keysIter = courseTasksJsonObj.keys();
                            if (keysIter.hasNext()) {
                                String courseCode =  keysIter.next();
                                JSONArray tasksJsonArray = courseTasksJsonObj.getJSONArray(courseCode);
                                Gson gson = new Gson();
                                Task [] tasks = gson.fromJson(tasksJsonArray.toString(), Task[].class);
                                // add the course code to the tasks
                                for (Task t : tasks) {
                                    t.setCourseCode(courseCode);
                                }

                                allTasks.addAll(Arrays.asList(tasks));
                                if (allTasks.size() > 0) {
                                    allTasksAdapter.notifyDataSetChanged();
                                }
                                else {
                                    noTasksMsgTextView.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                    else {
                        Toast.makeText(AllTasksFragment.this.getContext()
                                , getString(R.string.error_loading_all_tasks), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AllTasksFragment.this.getContext()
                            , getString(R.string.error_loading_all_tasks), Toast.LENGTH_SHORT).show();
                }


            }
        }
        , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());

                loadingProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(AllTasksFragment.this.getContext()
                        , getString(R.string.error_loading_all_tasks), Toast.LENGTH_SHORT).show();
            }
        });

        loadingProgressBar.setVisibility(View.VISIBLE);
        RequestQueueSingleton.getInstance(getContext()).addToRequestQueue(getAllTasksRequest);
    }

    private void readUserDataFromSharedPreference() {
        SharedPreferences sharedPref = getContext().getSharedPreferences(
                getString(R.string.authentication_shared_preference_file_name),
                Context.MODE_PRIVATE);

        token = sharedPref.getString(getString(R.string.saved_token_key), null);
        username = sharedPref.getString(getString(R.string.saved_username_key), null);

        Log.d(TAG, token);
        Log.d(TAG, username);
    }
}
