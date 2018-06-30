package eg.edu.cu.fci.ecampus.fci_e_campus.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import eg.edu.cu.fci.ecampus.fci_e_campus.adapters.CommentsAdapter;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.adapters.ForumAdapter;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Comment;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Forum;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.APIUtils;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.network.RequestQueueSingleton;

public class ForumPostActivity extends AppCompatActivity {

    private String token, username, userType, courseCode, courseTitle;
    private Forum forum;
    private CommentsAdapter commentsAdapter;
    private ArrayList<Comment> comments;
    private ProgressBar progressBar;
    private ListView commentsListView;
    private TextView emptyStateTextView;
    private ImageView addCommentImageView;
    private EditText commentEditText;

    private void readUserDataFromSharedPreference() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.authentication_shared_preference_file_name),
                Context.MODE_PRIVATE);

        token = sharedPref.getString(getString(R.string.saved_token_key), null);
        username = sharedPref.getString(getString(R.string.saved_username_key), null);
        userType = sharedPref.getString(getString(R.string.saved_user_type_key), null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_post);

        readUserDataFromSharedPreference();

        forum = (Forum) getIntent().getSerializableExtra("forum");

        TextView headerTextView = findViewById(R.id.forum_post_header);
        headerTextView.setText(forum.getHeader());

        TextView dateTextView = findViewById(R.id.forum_post_date);
        dateTextView.setText(forum.getConvertedDate());

        TextView bodyTextView = findViewById(R.id.forum_post_body);
        bodyTextView.setText(forum.getBody());

        TextView usernameTextView = findViewById(R.id.forum_post_username);
        usernameTextView.setText("by. " + forum.getAuthorUsername());

        TextView isAnsweredTextView = findViewById(R.id.forum_post_answered);
        if (forum.isAnswered() == 1) {
            isAnsweredTextView.setText(R.string.forum_answered);
        } else {
            isAnsweredTextView.setText(R.string.forum_not_answered);
        }

        final ImageView answeredImageView = findViewById(R.id.owner_post_answered);
        answeredImageView.setVisibility(View.GONE);
        if (forum.isAnswered() == 1) {
            answeredImageView.setImageResource(R.drawable.ic_not_answered);
        } else {
            answeredImageView.setImageResource(R.drawable.ic_answered_forum);
        }

        if (username.equals(forum.getAuthorUsername())) {
            isAnsweredTextView.setVisibility(View.GONE);
            answeredImageView.setVisibility(View.VISIBLE);
        }
        answeredImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (forum.isAnswered() == 1) {
                    forum.setAnswered(0);
                    notAnswerPost();
                    answeredImageView.setImageResource(R.drawable.ic_answered_forum);
                } else {
                    forum.setAnswered(1);
                    answerPost();
                    answeredImageView.setImageResource(R.drawable.ic_not_answered);
                }
            }
        });

        progressBar = findViewById(R.id.forum_progress_bar);
        emptyStateTextView = findViewById(R.id.forum_empty_title_text);
        emptyStateTextView.setVisibility(View.GONE);
        commentsListView = findViewById(R.id.forum_post_comments_list_view);
        commentEditText = findViewById(R.id.forum_post_comment);
        addCommentImageView = findViewById(R.id.forum_add_comment_button);
        addCommentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!commentEditText.getText().toString().trim().equals("")) {
                    addComment();
                }
            }
        });

        courseCode = getIntent().getStringExtra("course_code");
        courseTitle = getIntent().getStringExtra("course_title");
        setTitle(courseTitle.toUpperCase() + " - Forum");

        getPostComments();

    }

    private void getPostComments() {
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();
        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.post_prefix))
                .appendPath(getString(R.string.post_comments_endpoint))
                .build();

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("_token", token);
            requestBody.put("course_code", courseCode);
            requestBody.put("POSTID", forum.getPostID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest commentsRequest = new JsonObjectRequest(Request.Method.POST
                , uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        Gson gson = new Gson();
                        comments = new ArrayList<>();
                        comments = gson.fromJson(response.getJSONArray("result").toString(), new TypeToken<ArrayList<Comment>>() {
                        }.getType());
                        commentsAdapter = new CommentsAdapter(ForumPostActivity.this, comments);
                        progressBar.setVisibility(View.GONE);
                        commentsListView.setAdapter(commentsAdapter);
                        emptyStateTextView.setVisibility(View.INVISIBLE);
                        if (comments.size() < 1) {
                            emptyStateTextView.setVisibility(View.VISIBLE);
                        }
                    } else if (response.getString("status").equals("failed")) {
                        progressBar.setVisibility(View.GONE);
                        int errorCode = response.getInt("error_code");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(ForumPostActivity.this
                                , errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ForumPostActivity.this
                            , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ForumPostActivity.this
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(commentsRequest);
    }

    private void answerPost() {
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();
        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.post_prefix))
                .appendPath(getString(R.string.answer_post_endpoint))
                .build();

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("_token", token);
            requestBody.put("post_id", forum.getPostID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("request", requestBody.toString());
        JsonObjectRequest answerRequest = new JsonObjectRequest(Request.Method.POST
                , uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {

                    } else if (response.getString("status").equals("failed")) {
                        progressBar.setVisibility(View.GONE);
                        int errorCode = response.getInt("error_msg");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(ForumPostActivity.this
                                , errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ForumPostActivity.this
                            , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
                    Log.e("error", e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Log.e("error", error.toString());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ForumPostActivity.this
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(answerRequest);
    }

    private void notAnswerPost() {
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();
        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.post_prefix))
                .appendPath(getString(R.string.not_answer_post_endpoint))
                .build();

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("_token", token);
            requestBody.put("post_id", forum.getPostID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("request", requestBody.toString());
        JsonObjectRequest notAnswerRequest = new JsonObjectRequest(Request.Method.POST
                , uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {

                    } else if (response.getString("status").equals("failed")) {
                        progressBar.setVisibility(View.GONE);
                        int errorCode = response.getInt("error_msg");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(ForumPostActivity.this
                                , errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ForumPostActivity.this
                            , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
                    Log.e("error", e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Log.e("error", error.toString());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ForumPostActivity.this
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(notAnswerRequest);
    }

    private void addComment() {
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();
        Uri uri = Uri.parse(getString(R.string.base_url))
                .buildUpon()
                .appendPath(getString(R.string.post_prefix))
                .appendPath(getString(R.string.add_comment_endpoint))
                .build();

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("_token", token);
            requestBody.put("author_username", username);
            Log.e("username", username);
            if (userType.equals(getString(R.string.student_user_type))) {
                requestBody.put("author_type", "stud");
            } else if (userType.equals(getString(R.string.professor_user_type))) {
                requestBody.put("author_type", "prof");
            } else {
                requestBody.put("author_type", "ta");
            }
            requestBody.put("course_code", courseCode);
            requestBody.put("post_id", forum.getPostID());
            requestBody.put("comment_text", commentEditText.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("request", requestBody.toString());
        JsonObjectRequest addCommentRequest = new JsonObjectRequest(Request.Method.POST
                , uri.toString(), requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        commentEditText.setText("");
                        getPostComments();
                    } else if (response.getString("status").equals("failed")) {
                        progressBar.setVisibility(View.GONE);
                        int errorCode = response.getInt("error_msg");
                        String errorMessage = APIUtils.getErrorMsg(errorCode);
                        Toast.makeText(ForumPostActivity.this
                                , errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ForumPostActivity.this
                            , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
                    Log.e("error", e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Log.e("error", error.toString());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ForumPostActivity.this
                        , "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(addCommentRequest);
    }

    /*private void prepareListData() {
        Date date = new Date(100, 40, 20);
        Comment comment = new Comment("You have to detect any other data to set up your date good",
                date, "ahmedotto");
        comments = new ArrayList<>();

        comments.add(comment);
        comments.add(comment);
        comments.add(comment);
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
        return true;
    }
}
