package eg.edu.cu.fci.ecampus.fci_e_campus.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eg.edu.cu.fci.ecampus.fci_e_campus.Adapters.TasksAdapter;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;

public class CourseTasksActivity extends AppCompatActivity {

    TasksAdapter listAdapter;
    ExpandableListView expListView;
    ArrayList<String> listDataHeader;
    HashMap<String, ArrayList<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_tasks);

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
        ArrayList<String> top250 = new ArrayList<>();
        top250.add("Node.js Phase of the Project");

        ArrayList<String> nowShowing = new ArrayList<>();
        nowShowing.add("Algorithms Assignment 1 about 3 Problems to be solved");

        ArrayList<String> comingSoon = new ArrayList<>();
        comingSoon.add("Phase Lexical Analysis of the Compilers Project");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }
}
