package eg.edu.cu.fci.ecampus.fci_e_campus.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eg.edu.cu.fci.ecampus.fci_e_campus.Adapters.TasksAdapter;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Task;

public class CourseTasksActivity extends AppCompatActivity {

    private TasksAdapter listAdapter;
    private ExpandableListView expListView;
    private ArrayList<String> listDataHeader;
    private HashMap<String, Task> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_tasks);
        String courseTitle = getIntent().getStringExtra("course_title");
        setTitle(courseTitle + " - Tasks");
        // get the listview
        expListView = findViewById(R.id.tasks_expandable_list_view);

        // preparing list data
        prepareListData();

        listAdapter = new TasksAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add("ERP Project - Phase 3");
        listDataHeader.add("Assignment-1");
        listDataHeader.add("Compilers Project - Phase 2");

        // Adding child data
        Task top250 = new Task();
        top250.setDescription("Node.js Phase of the Project");
        top250.setCreatorUsername("5 Nov");

        Task nowShowing = new Task();
        nowShowing.setDescription("Algorithms Assignment 1 about 3 Problems to be solved");
        nowShowing.setCreatorUsername("7 Nov");

        Task comingSoon = new Task();
        comingSoon.setDescription("Phase Lexical Analysis of the Compilers Project");
        comingSoon.setCreatorUsername("10 Nov");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
