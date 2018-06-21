package eg.edu.cu.fci.ecampus.fci_e_campus.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Material;

/**
 * Created by ahmed on 6/21/2018.
 */

public class MaterialsAdapter extends ArrayAdapter<Material> {
    public MaterialsAdapter(Activity context, ArrayList<Material> materials) {
        super(context, 0, materials);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.materials_list_item, parent, false);
        }
        Material currentMaterial = getItem(position);

        TextView nameTextView = listItemView.findViewById(R.id.material_name);
        nameTextView.setText(currentMaterial.getName());

        TextView uploaderTextView = listItemView.findViewById(R.id.material_uploader);
        uploaderTextView.setText(currentMaterial.getUploaderUsername());

        TextView dateTextView = listItemView.findViewById(R.id.material_date);
        dateTextView.setText(currentMaterial.getDescription());

        return listItemView;
    }
}
