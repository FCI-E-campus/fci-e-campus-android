package eg.edu.cu.fci.ecampus.fci_e_campus.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import eg.edu.cu.fci.ecampus.fci_e_campus.Adapters.ForumAdapter;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Forum;

public class CourseForumActivity extends AppCompatActivity {

    private ForumAdapter forumAdapter;
    private ArrayList<Forum> forums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_forum);

        String courseTitle = getIntent().getStringExtra("course_title");
        setTitle(courseTitle + " - Forum");

        ListView listView = findViewById(R.id.course_forum_list_view);
        prepareListData();
        forumAdapter = new ForumAdapter(this, forums);
        listView.setAdapter(forumAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CourseForumActivity.this, ForumPostActivity.class);
                intent.putExtra("is_answered", forums.get(i).isAnswered());
                startActivity(intent);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab_course_forum);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseForumActivity.this, AddPostActivity.class);
                startActivity(intent);
            }
        });
    }

    private void prepareListData() {
        Date date = new Date(100, 0, 15);
        Forum forum1 = new Forum("Lexical Analyzer", "some long text here, some long text here, some long text here, some long text here, some long text here, some long text here," +
                " some long text here, some long text here, some long text here", date, "ahmedotto", true);
        Forum forum2 = new Forum("Lexical Analyzer", "some long text here, some long text here, some long text here, some long text here, some long text here, some long text here," +
                " some long text here, some long text here, some long text here", date, "ahmedotto", false);
        Forum forum3 = new Forum("Lexical Analyzer", "some long text here, some long text here, some long text here, some long text here, some long text here, some long text here," +
                " some long text here, some long text here, some long text here", date, "ahmedotto", true);
        forums = new ArrayList<>();
        forums.add(forum1);
        forums.add(forum2);
        forums.add(forum3);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
