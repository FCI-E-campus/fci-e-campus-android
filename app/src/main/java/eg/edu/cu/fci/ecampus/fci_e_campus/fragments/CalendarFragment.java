package eg.edu.cu.fci.ecampus.fci_e_campus.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
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
import com.google.android.gms.common.util.ArrayUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

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
import eg.edu.cu.fci.ecampus.fci_e_campus.adapters.DayScheduleAdapter;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Announcement;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Slot;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Task;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.network.RequestQueueSingleton;


public class CalendarFragment extends Fragment {

    private static final String TAG = CalendarFragment.class.getSimpleName();

    @BindView(R.id.rv_sunday) RecyclerView sundayRecyclerView;
    @BindView(R.id.tv_msg_empty_sunday) TextView emptySundayMsgTextView;
    DayScheduleAdapter sundayAdapter;
    List<Slot> sundaySlots;

    @BindView(R.id.rv_monday) RecyclerView mondayRecyclerView;
    @BindView(R.id.tv_msg_empty_monday) TextView emptyMondayMsgTextView;
    DayScheduleAdapter mondayAdapter;
    List<Slot> mondaySlots;

    @BindView(R.id.rv_tuesday) RecyclerView tuesdayRecyclerView;
    @BindView(R.id.tv_msg_empty_tuesday) TextView emptyTuesdayMsgTextView;
    DayScheduleAdapter tuesdayAdapter;
    List<Slot> tuesdaySlots;

    @BindView(R.id.rv_wednesday) RecyclerView wednesdayRecyclerView;
    @BindView(R.id.tv_msg_empty_wednesday) TextView emptyWednesdayMsgTextView;
    DayScheduleAdapter wednesdayAdapter;
    List<Slot> wednesdaySlots;

    @BindView(R.id.rv_thursday) RecyclerView thursdayRecyclerView;
    @BindView(R.id.tv_msg_empty_thursday) TextView emptyThursdayMsgTextView;
    DayScheduleAdapter thursdayAdapter;
    List<Slot> thursdaySlots;

    @BindView(R.id.nsc_schedules) NestedScrollView schedulesNestedScrollView;

    @BindView(R.id.pb_calendar) ProgressBar loadingSchedulesProgrsessBar;

    private String token;
    private String username;
    private String userType;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment CalendarFragment.
     */
    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readUserDataFromSharedPreferences();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentRootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this, fragmentRootView);

        configureRecyclerViews();

        schedulesNestedScrollView.setVisibility(View.INVISIBLE);
        loadingSchedulesProgrsessBar.setVisibility(View.VISIBLE);
        return fragmentRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (userType.equals(getString(R.string.student_user_type))) {
            // student
            fetchStudentSchedule();
        } else if (userType.equals(getString(R.string.ta_user_type))) {
            // ta
            fetchTaSchedule();
        } else if (userType.equals(getString(R.string.professor_user_type))) {
            fetchProfSchedule();
        }
    }

    private void fetchStudentSchedule() {
        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.student_prefix))
                .appendPath(getString(R.string.show_student_schedule_endpoint))
                .build();

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("_token", token);
            requestBody.put("STUDUSERNAME", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest getScheduleRequest = new JsonObjectRequest(Request.Method.POST,
                uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingSchedulesProgrsessBar.setVisibility(View.INVISIBLE);
                parseScheduleResponse(response);
                schedulesNestedScrollView.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());

                loadingSchedulesProgrsessBar.setVisibility(View.INVISIBLE);
                showErrorToastMsg();
            }
        });

        RequestQueueSingleton.getInstance(getContext()).addToRequestQueue(getScheduleRequest);
    }

    private void fetchProfSchedule() {

    }

    private void fetchTaSchedule() {

    }

    private void parseScheduleResponse(JSONObject response) {
        Log.d(TAG, response.toString());

        try {
            if (response.getString("status").equals("success")) {
                JSONArray result = response.getJSONArray("result");
                for (int i = 0; i<result.length(); i++) {
                    JSONObject courseScheduleJsonObj = result.getJSONObject(i);
                    JSONArray labsJsonArray = courseScheduleJsonObj.getJSONArray("labs");
                    JSONArray lecturesJsonArray = courseScheduleJsonObj.getJSONArray("lecture");

                    Gson gson = new Gson();
                    Slot [] labsSlots = gson.fromJson(labsJsonArray.toString(), Slot[].class);
                    Slot [] lecturesSlots = gson.fromJson(lecturesJsonArray.toString(), Slot[].class);

                    List<Slot> slots = new ArrayList<>();
                    slots.addAll(Arrays.asList(labsSlots));
                    slots.addAll(Arrays.asList(lecturesSlots));

                    showSlotsInRecyclerViews(slots);
                }
            }
            else {
                showErrorToastMsg();
            }

        } catch (JSONException e) {
            e.printStackTrace();

            showErrorToastMsg();
        }
    }

    private void showSlotsInRecyclerViews(List<Slot> slots) {
        for (Slot s: slots) {
            switch (s.getDay()) {
                case "sunday":
                    sundaySlots.add(s);
                    break;
                case "monday":
                    mondaySlots.add(s);
                    break;
                case "tuesday":
                    tuesdaySlots.add(s);
                    break;
                case "wednesday":
                    wednesdaySlots.add(s);
                    break;
                case "thursday":
                    thursdaySlots.add(s);
                    break;
                default:
                        Log.d(TAG, "Slot with an identified day.");
            }
        }

        if (sundaySlots.size() > 0) {
            sundayAdapter.notifyDataSetChanged();
            emptySundayMsgTextView.setVisibility(View.INVISIBLE);
        }
        if (mondaySlots.size() > 0) {
            mondayAdapter.notifyDataSetChanged();
            emptyMondayMsgTextView.setVisibility(View.INVISIBLE);
        }
        if (tuesdaySlots.size() > 0) {
            tuesdayAdapter.notifyDataSetChanged();
            emptyTuesdayMsgTextView.setVisibility(View.INVISIBLE);
        }
        if (wednesdaySlots.size() > 0) {
            wednesdayAdapter.notifyDataSetChanged();
            emptyWednesdayMsgTextView.setVisibility(View.INVISIBLE);
        }
        if (thursdaySlots.size() > 0) {
            thursdayAdapter.notifyDataSetChanged();
            emptyThursdayMsgTextView.setVisibility(View.INVISIBLE);
        }
    }

    private void showErrorToastMsg() {
        Toast.makeText(getContext()
                , "Error loading your courses schedules!", Toast.LENGTH_SHORT).show();
    }

    private void configureRecyclerViews() {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL);

        // Sunday
        sundayRecyclerView.setHasFixedSize(false);
        LinearLayoutManager sundayLayoutManager = new LinearLayoutManager(getContext());
        sundayRecyclerView.setLayoutManager(sundayLayoutManager);
        sundaySlots = new ArrayList<>();
        sundayAdapter = new DayScheduleAdapter(sundaySlots);
        sundayRecyclerView.setAdapter(sundayAdapter);
        sundayRecyclerView.addItemDecoration(dividerItemDecoration);
        sundayRecyclerView.setNestedScrollingEnabled(false);

        // Monday
        mondayRecyclerView.setHasFixedSize(false);
        LinearLayoutManager mondayLayoutManager = new LinearLayoutManager(getContext());
        mondayRecyclerView.setLayoutManager(mondayLayoutManager);
        mondaySlots = new ArrayList<>();
        mondayAdapter = new DayScheduleAdapter(mondaySlots);
        mondayRecyclerView.setAdapter(mondayAdapter);
        mondayRecyclerView.addItemDecoration(dividerItemDecoration);
        mondayRecyclerView.setNestedScrollingEnabled(false);

        // Tuesday
        tuesdayRecyclerView.setHasFixedSize(false);
        LinearLayoutManager tuesdayLayoutManager = new LinearLayoutManager(getContext());
        tuesdayRecyclerView.setLayoutManager(tuesdayLayoutManager);
        tuesdaySlots = new ArrayList<>();
        tuesdayAdapter = new DayScheduleAdapter(tuesdaySlots);
        tuesdayRecyclerView.setAdapter(tuesdayAdapter);
        tuesdayRecyclerView.addItemDecoration(dividerItemDecoration);
        tuesdayRecyclerView.setNestedScrollingEnabled(false);

        // Wednesday
        wednesdayRecyclerView.setHasFixedSize(false);
        LinearLayoutManager wednesdayLayoutManager = new LinearLayoutManager(getContext());
        wednesdayRecyclerView.setLayoutManager(wednesdayLayoutManager);
        wednesdaySlots = new ArrayList<>();
        wednesdayAdapter = new DayScheduleAdapter(wednesdaySlots);
        wednesdayRecyclerView.setAdapter(wednesdayAdapter);
        wednesdayRecyclerView.addItemDecoration(dividerItemDecoration);
        wednesdayRecyclerView.setNestedScrollingEnabled(false);

        // Thursday
        thursdayRecyclerView.setHasFixedSize(false);
        LinearLayoutManager thursdayLayoutManager = new LinearLayoutManager(getContext());
        thursdayRecyclerView.setLayoutManager(thursdayLayoutManager);
        thursdaySlots = new ArrayList<>();
        thursdayAdapter = new DayScheduleAdapter(thursdaySlots);
        thursdayRecyclerView.setAdapter(thursdayAdapter);
        thursdayRecyclerView.addItemDecoration(dividerItemDecoration);
        thursdayRecyclerView.setNestedScrollingEnabled(false);
    }

    private void readUserDataFromSharedPreferences() {
        SharedPreferences sharedPref = getContext().getSharedPreferences(
                getString(R.string.authentication_shared_preference_file_name),
                Context.MODE_PRIVATE);

        token = sharedPref.getString(getString(R.string.saved_token_key), null);
        username = sharedPref.getString(getString(R.string.saved_username_key), null);
        userType = sharedPref.getString(getString(R.string.saved_user_type_key), null);

        Log.d(TAG, username);
        Log.d(TAG, token);
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
