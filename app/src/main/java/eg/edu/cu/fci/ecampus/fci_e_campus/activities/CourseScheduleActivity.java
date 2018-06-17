package eg.edu.cu.fci.ecampus.fci_e_campus.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import eg.edu.cu.fci.ecampus.fci_e_campus.R;

public class CourseScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_schedule);

        LinearLayout parentLayout = findViewById(R.id.course_schedule_activity);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.one_day_layout, null);
        parentLayout.addView(rowView, parentLayout.getChildCount()-1);

        TextView day = rowView.findViewById(R.id.day_text_view);
        day.setText("Tomorrow");
    }
}
