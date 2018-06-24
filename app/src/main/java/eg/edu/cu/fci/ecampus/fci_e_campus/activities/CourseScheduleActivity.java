package eg.edu.cu.fci.ecampus.fci_e_campus.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Time;
import java.util.ArrayList;

import eg.edu.cu.fci.ecampus.fci_e_campus.Adapters.ScheduleAdapter;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Slot;

public class CourseScheduleActivity extends AppCompatActivity {

    private ScheduleAdapter scheduleAdapter;
    private ArrayList<Slot> slots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_schedule);

        ListView listView = findViewById(R.id.schedule_list_view);
        prepareData();
        scheduleAdapter = new ScheduleAdapter(this, slots);
        listView.setAdapter(scheduleAdapter);

    }

    private void prepareData() {
        Time time = new Time(10, 30, 0);
        Slot slot = new Slot("Sunday", time, 1, 2, "Hall 1", "Cs120");
        slots = new ArrayList<>();
        slots.add(slot);
        slots.add(slot);
        slots.add(slot);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
