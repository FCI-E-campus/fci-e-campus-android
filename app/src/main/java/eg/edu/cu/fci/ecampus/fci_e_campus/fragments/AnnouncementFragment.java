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
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.Adapters.AnnouncementsAdapter;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Announcement;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.network.RequestQueueSingleton;

public class AnnouncementFragment extends Fragment {

    private static final String TAG = AnnouncementFragment.class.getSimpleName();

    @BindView(R.id.rv_announcements) RecyclerView announcementsRecyclerView;
    @BindView(R.id.pb_announcements) ProgressBar loadingProgressBar;
    @BindView(R.id.tv_no_announcements_msg) TextView noAnnouncementsMsg;

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

        Announcement [] announcements = new Announcement[0];
        announcementsAdapter = new AnnouncementsAdapter(announcements);
        announcementsRecyclerView.setAdapter(announcementsAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                layoutManager.getOrientation());
        announcementsRecyclerView.addItemDecoration(dividerItemDecoration);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.announcements_prefix))
                .appendPath(getString(R.string.show_announcements_endpoint))
                .build();

        JSONObject requestBody = new JSONObject();
        String token = getAuthenticationToken();
        try {
            requestBody.put("_token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest getAnnouncementsRequest = new JsonObjectRequest(Request.Method.POST,
                uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                loadingProgressBar.setVisibility(View.INVISIBLE);

                try {
                    if (response.getString("status").equals("success")) {
                        Gson gson = new Gson();
                        Log.d(TAG, response.getJSONArray("result").toString());
                        Announcement [] announcements = gson
                                .fromJson(response.getJSONArray("result").toString()
                                        , Announcement[].class);

                        if (announcements.length != 0) {
                            announcementsAdapter.swapAnnouncements(announcements);
                        }
                        else {
                            noAnnouncementsMsg.setVisibility(View.VISIBLE);
                        }

                    }
                    else {
                        Toast.makeText(AnnouncementFragment.this.getContext()
                                , "Error loading announcements!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AnnouncementFragment.this.getContext()
                            , "Error loading announcements!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());

                loadingProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(AnnouncementFragment.this.getContext()
                        , "Error loading announcements!", Toast.LENGTH_SHORT).show();
            }
        });

        loadingProgressBar.setVisibility(View.VISIBLE);
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
