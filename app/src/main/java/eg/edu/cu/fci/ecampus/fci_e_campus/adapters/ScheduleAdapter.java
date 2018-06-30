package eg.edu.cu.fci.ecampus.fci_e_campus.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;

import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Slot;

/**
 * Created by ahmed on 6/24/2018.
 */

public class ScheduleAdapter extends ArrayAdapter<Slot> {
    public ScheduleAdapter(Activity context, ArrayList<Slot> slots) {
        super(context, 0, slots);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.schedule_list_item, parent, false);
        }
        Slot currentSlot = getItem(position);

        TextView slotType = listItemView.findViewById(R.id.slot_type);
        slotType.setText(currentSlot.getType()+"");


        TextView slotTime = listItemView.findViewById(R.id.slot_time);
        try {
            slotTime.setText(currentSlot.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView slotLocation = listItemView.findViewById(R.id.slot_location);
        slotLocation.setText(currentSlot.getLocation());

        TextView slotGroup = listItemView.findViewById(R.id.slot_group);
        slotGroup.setText(currentSlot.getGroupNumber());

        return listItemView;
    }
}
