package eg.edu.cu.fci.ecampus.fci_e_campus.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import eg.edu.cu.fci.ecampus.fci_e_campus.R;

public class CourseActivity extends AppCompatActivity {

    private String courseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        courseName = getIntent().getStringExtra("course_code");
        setTitle(courseName);
        Button viewSchedule = findViewById(R.id.view_schedule_button);
        viewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseActivity.this, CourseScheduleActivity.class);
                startActivity(intent);
            }
        });

        Button viewMaterials = findViewById(R.id.view_resources_button);
        viewMaterials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseActivity.this, CourseMaterialsActivity.class);
                startActivity(intent);
            }
        });

        Button viewTasks = findViewById(R.id.view_tasks_button);
        viewTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseActivity.this, CourseTasksActivity.class);
                intent.putExtra("course_title",courseName);
                startActivity(intent);
            }
        });
        Button openForum = findViewById(R.id.open_forum_button);
        openForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseActivity.this, CourseForumActivity.class);
                intent.putExtra("course_title",courseName);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
