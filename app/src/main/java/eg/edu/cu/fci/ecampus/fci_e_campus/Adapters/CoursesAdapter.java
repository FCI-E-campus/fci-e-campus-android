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
import eg.edu.cu.fci.ecampus.fci_e_campus.models.CourseShow;

/**
 * Created by ahmed on 6/25/2018.
 */

public class CoursesAdapter extends ArrayAdapter<CourseShow> {
    public CoursesAdapter(Activity context, ArrayList<CourseShow> courses) {
        super(context, 0, courses);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.course_list_item, parent, false);
        }
        CourseShow currentCourse = getItem(position);

        TextView courseTitle = listItemView.findViewById(R.id.course_title);
        courseTitle.setText(currentCourse.getTitle());

        return listItemView;
    }
}
