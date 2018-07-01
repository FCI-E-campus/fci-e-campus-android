package eg.edu.cu.fci.ecampus.fci_e_campus.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.adapters.DayScheduleAdapter;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Slot;


public class CalendarFragment extends Fragment {

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentRootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this, fragmentRootView);

        configureDaysRecyclerViews();

        return fragmentRootView;
    }

    private void configureDaysRecyclerViews() {

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
