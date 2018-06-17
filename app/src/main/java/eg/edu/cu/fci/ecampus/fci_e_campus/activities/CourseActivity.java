package eg.edu.cu.fci.ecampus.fci_e_campus.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import eg.edu.cu.fci.ecampus.fci_e_campus.R;

public class CourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Log.d("onCreate","Herererere");
        setTitle(getIntent().getStringExtra("course_name"));
        Button viewSchedule = findViewById(R.id.view_schedule_button);
        viewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseActivity.this, CourseScheduleActivity.class);
                startActivity(intent);
            }
        });
    }
}
