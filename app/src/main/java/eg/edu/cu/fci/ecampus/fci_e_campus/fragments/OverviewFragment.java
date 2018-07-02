package eg.edu.cu.fci.ecampus.fci_e_campus.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import eg.edu.cu.fci.ecampus.fci_e_campus.adapters.OverviewAdapter;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.OverviewSlot;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.APIUtils;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.network.RequestQueueSingleton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OverviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverviewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView todayListView;
    private ListView tomorrowListView;
    private ProgressBar todayProgressBar;
    private ProgressBar tomorrowProgressBar;
    private TextView todayEmptyStateTextView;
    private TextView tomorrowEmptyStateTextView;
    private OverviewAdapter todayAdapter;
    private OverviewAdapter tomorrowAdapter;
    private ArrayList<OverviewSlot> todaySlots;
    private ArrayList<OverviewSlot> tomorrowSlots;

    private String token, username, userType;

    private void readUserDataFromSharedPreference() {
        SharedPreferences sharedPref = getContext().getSharedPreferences(
                getString(R.string.authentication_shared_preference_file_name),
                Context.MODE_PRIVATE);

        token = sharedPref.getString(getString(R.string.saved_token_key), null);
        username = sharedPref.getString(getString(R.string.saved_username_key), null);
        userType = sharedPref.getString(getString(R.string.saved_user_type_key), null);
    }

    private OnFragmentInteractionListener mListener;

    public OverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OverviewFragment newInstance(String param1, String param2) {
        OverviewFragment fragment = new OverviewFragment();
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

        View view = inflater.inflate(R.layout.fragment_overview, container, false);


        todayProgressBar = view.findViewById(R.id.today_progress_bar);
        tomorrowProgressBar = view.findViewById(R.id.tomorrow_progress_bar);
        todayEmptyStateTextView = view.findViewById(R.id.today_empty_title_text);
        tomorrowEmptyStateTextView = view.findViewById(R.id.tomorrow_empty_title_text);

        todayEmptyStateTextView.setVisibility(View.INVISIBLE);
        tomorrowEmptyStateTextView.setVisibility(View.INVISIBLE);

        todayListView = view.findViewById(R.id.today_list_overview);
        tomorrowListView = view.findViewById(R.id.tomorrow_list_overview);

        //Check Connectivity
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
        if (!isConnected) { //Not Connected to Internet
            todayProgressBar.setVisibility(View.GONE);
            tomorrowProgressBar.setVisibility(View.GONE);
            todayEmptyStateTextView.setText(R.string.no_internet);
            tomorrowEmptyStateTextView.setText(R.string.no_internet);
            return view;
        }
        //Connected to Internet

        /*
        final String[] values1 = new String[]{"Lecture:Algorithms",
                "Lab:Database",
                "Lecture:Compilers"
        };
        final String[] values2 = new String[]{"Lab:Machine Learning",
                "Section:Statistics",
                "Lab:Analysis",
                "Lecture:Concepts"
        };
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values1);

        ListView listView1 = view.findViewById(R.id.list1_overview);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), values1[i], Toast.LENGTH_SHORT).show();
            }
        });
        listView1.setAdapter(adapter1);


        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values2);
        ListView listView2 = view.findViewById(R.id.list2_overview);
        listView2.setAdapter(adapter2);*/
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getSlots();
        //prepare();
    }

    private void getSlots() {
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(getContext()).getRequestQueue();
        Uri uri;
        if (userType.equals(getString(R.string.student_user_type))) {
            uri = Uri.parse(getString(R.string.base_url))
                    .buildUpon()
                    .appendPath(getString(R.string.student_prefix))
                    .appendPath(getString(R.string.overview_endpoint))
                    .build();
        } else if (userType.equals(getString(R.string.ta_user_type))) {
            uri = Uri.parse(getString(R.string.base_url))
                    .buildUpon()
                    .appendPath(getString(R.string.ta_prefix))
                    .appendPath(getString(R.string.overview_endpoint))
                    .build();
        } else {
            uri = Uri.parse(getString(R.string.base_url))
                    .buildUpon()
                    .appendPath(getString(R.string.professor_prefix))
                    .appendPath(getString(R.string.overview_endpoint))
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
        JsonObjectRequest overviewRequest = new JsonObjectRequest(Request.Method.POST
                , uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        Gson gson = new Gson();
                        todaySlots = new ArrayList<>();
                        tomorrowSlots = new ArrayList<>();
                        todaySlots = gson.fromJson(response.getJSONObject("result").getJSONArray("today").toString(), new TypeToken<ArrayList<OverviewSlot>>() {
                        }.getType());
                        tomorrowSlots = gson.fromJson(response.getJSONObject("result").getJSONArray("tomorrow").toString(), new TypeToken<ArrayList<OverviewSlot>>() {
                        }.getType());
                        todayAdapter = new OverviewAdapter(getActivity(), todaySlots);
                        tomorrowAdapter = new OverviewAdapter(getActivity(), tomorrowSlots);
                        todayListView.setAdapter(todayAdapter);
                        tomorrowListView.setAdapter(tomorrowAdapter);
                        todayProgressBar.setVisibility(View.GONE);
                        tomorrowProgressBar.setVisibility(View.GONE);
                        todayEmptyStateTextView.setVisibility(View.INVISIBLE);
                        tomorrowEmptyStateTextView.setVisibility(View.INVISIBLE);
                        if (todaySlots.size() < 1) {
                            todayEmptyStateTextView.setVisibility(View.VISIBLE);
                        }
                        if (tomorrowSlots.size() < 1) {
                            tomorrowEmptyStateTextView.setVisibility(View.VISIBLE);
                        }
                    } else if (response.getString("status").equals("failed")) {
                        int errorCode = response.getInt("error_code");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(getContext()
                                , errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext()
                            , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                todayProgressBar.setVisibility(View.GONE);
                tomorrowProgressBar.setVisibility(View.GONE);
                Toast.makeText(getContext()
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(overviewRequest);
    }

    private void prepare() {
        OverviewSlot slot1 = new OverviewSlot();
        slot1.setName("Compilers Lab");
        slot1.setTimeString("08:00:00");
        slot1.setPlace("Lab 3");
        OverviewSlot slot2 = new OverviewSlot();
        slot2.setName("Database Lecture");
        slot2.setTimeString("10:00:00");
        slot2.setPlace("Hall 1");
        OverviewSlot slot3 = new OverviewSlot();
        slot3.setName("Algorithms Lab");
        slot3.setTimeString("11:15:00");
        slot3.setPlace("Lab 7");
        OverviewSlot slot4 = new OverviewSlot();
        slot4.setName("Math Section");
        slot4.setTimeString("08:00:00");
        slot4.setPlace("Hall 265");
        OverviewSlot slot5 = new OverviewSlot();
        slot5.setName("Network Lab");
        slot5.setTimeString("12:30:00");
        slot5.setPlace("Lab 8");
        todaySlots = new ArrayList<>();
        tomorrowSlots = new ArrayList<>();
        todaySlots.add(slot1);
        todaySlots.add(slot2);
        todaySlots.add(slot3);
        tomorrowSlots.add(slot4);
        tomorrowSlots.add(slot5);
        todayAdapter = new OverviewAdapter(getActivity(), todaySlots);
        tomorrowAdapter = new OverviewAdapter(getActivity(), tomorrowSlots);
        todayListView.setAdapter(todayAdapter);
        tomorrowListView.setAdapter(tomorrowAdapter);
        todayProgressBar.setVisibility(View.GONE);
        tomorrowProgressBar.setVisibility(View.GONE);
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
