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
import eg.edu.cu.fci.ecampus.fci_e_campus.models.OverviewSlot;

/**
 * Created by ahmed on 7/1/2018.
 */

public class OverviewAdapter extends ArrayAdapter<OverviewSlot> {

    public OverviewAdapter(Activity context, ArrayList<OverviewSlot> slots) {
        super(context, 0, slots);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (convertView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.overview_list_item, parent, false);
        }
        OverviewSlot currentSlot = getItem(position);

        TextView nameTextView = listItemView.findViewById(R.id.slot_name_text_view);
        nameTextView.setText(currentSlot.getName());

        TextView timeTextView = listItemView.findViewById(R.id.slot_time_text_view);
        try {
            timeTextView.setText(currentSlot.getConvertedTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView placeTextView = listItemView.findViewById(R.id.slot_place_text_view);
        placeTextView.setText(currentSlot.getPlace());

        return listItemView;
    }
}
