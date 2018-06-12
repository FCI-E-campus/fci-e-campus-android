package eg.edu.cu.fci.ecampus.fci_e_campus.fragments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import eg.edu.cu.fci.ecampus.fci_e_campus.R;

public class CourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        setTitle(getIntent().getStringExtra("course_name"));
    }
}
