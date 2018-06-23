package eg.edu.cu.fci.ecampus.fci_e_campus.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.adapters.AnnouncementsAdapter;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Announcement;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.network.CustomJsonRequest;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.network.RequestQueueSingleton;

public class AnnouncementFragment extends Fragment {

    private static final String TAG = AnnouncementFragment.class.getSimpleName();

    @BindView(R.id.rv_announcements) private RecyclerView announcementsRecyclerView;
    @BindView(R.id.pb_announcements) private ProgressBar loadingProgressBar;
    @BindView(R.id.tv_no_announcements_msg) private TextView noAnnouncementsMsg;

    private Announcement [] announcements;
    private AnnouncementsAdapter announcementsAdapter;

    public AnnouncementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment AnnouncementFragment.
     */
    public static AnnouncementFragment newInstance() {
        AnnouncementFragment fragment = new AnnouncementFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_announcement, container, false);
        ButterKnife.bind(this, fragmentView);

        configureAnnouncementsRecyclerView();

        return fragmentView;
    }

    private void configureAnnouncementsRecyclerView() {
        announcementsRecyclerView.setHasFixedSize(true);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        announcementsRecyclerView.setLayoutManager(layoutManager);

        announcementsAdapter = new AnnouncementsAdapter(announcements);
        announcementsRecyclerView.setAdapter(announcementsAdapter);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.announcements_path))
                .appendPath(getString(R.string.show_announcements_endpoint))
                .build();

        JSONObject requestBody = new JSONObject();
        String token = getAuthenticationToken();
        try {
            requestBody.put("_token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonRequest getAnnouncementsRequest = new CustomJsonRequest(Request.Method.POST,
                uri.toString(), requestBody, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

                RequestQueueSingleton.getInstance(getContext()).addToRequestQueue(getAnnouncementsRequest);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private String getAuthenticationToken() {
        SharedPreferences sharedPref = getContext().getSharedPreferences(
                getString(R.string.authentication_shared_preference_file_name),
                Context.MODE_PRIVATE);

        String token = sharedPref.getString(getString(R.string.saved_token_key), null);

        Log.d(TAG, token);

        return token;
    }
}
