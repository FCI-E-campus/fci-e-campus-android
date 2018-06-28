package eg.edu.cu.fci.ecampus.fci_e_campus.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import eg.edu.cu.fci.ecampus.fci_e_campus.adapters.CommentsAdapter;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Comment;

public class ForumPostActivity extends AppCompatActivity {

    private CommentsAdapter commentsAdapter;
    private ArrayList<Comment> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_post);
        boolean isAnswered = getIntent().getBooleanExtra("is_answered", false);
        TextView isAnsweredTextView = findViewById(R.id.forum_post_answered);
        if (isAnswered) {
            isAnsweredTextView.setText(R.string.forum_answered);
        } else {
            isAnsweredTextView.setText(R.string.forum_not_answered);
        }

        ListView listView = findViewById(R.id.forum_post_comments_list_view);
        prepareListData();
        commentsAdapter = new CommentsAdapter(this, comments);
        listView.setAdapter(commentsAdapter);
    }

    private void prepareListData() {
        Date date = new Date(100, 40, 20);
        Comment comment = new Comment("You have to detect any other data to set up your date good",
                date, "ahmedotto");
        comments = new ArrayList<>();

        comments.add(comment);
        comments.add(comment);
        comments.add(comment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
