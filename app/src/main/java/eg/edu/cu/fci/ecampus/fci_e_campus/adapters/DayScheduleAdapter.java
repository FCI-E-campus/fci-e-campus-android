package eg.edu.cu.fci.ecampus.fci_e_campus.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Slot;

public class DayScheduleAdapter extends RecyclerView.Adapter<DayScheduleAdapter.ViewHolder> {

    private static final String TAG = DayScheduleAdapter.class.getSimpleName();

    private List<Slot> daySlots;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day_schedule, parent, false);

        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Slot slot = daySlots.get(position);

        holder.courseCodeTextView.setText(slot.getCourseCode());
        holder.slotTypeTextView.setText(slot.getSlotType());
        holder.startTimeTextView.setText(slot.getStartTimeString());
        holder.groupNumberTextView.setText(slot.getGroupNumber());
        holder.durationTextView.setText(slot.getDuration());
        holder.locationTextView.setText(slot.getLocation());
    }

    @Override
    public int getItemCount() {
        return daySlots.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_course_code) TextView courseCodeTextView;
        @BindView(R.id.tv_slot_type) TextView slotTypeTextView;
        @BindView(R.id.tv_start_time) TextView startTimeTextView;
        @BindView(R.id.tv_group_number) TextView groupNumberTextView;
        @BindView(R.id.tv_duration) TextView durationTextView;
        @BindView(R.id.tv_location) TextView locationTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }
}
