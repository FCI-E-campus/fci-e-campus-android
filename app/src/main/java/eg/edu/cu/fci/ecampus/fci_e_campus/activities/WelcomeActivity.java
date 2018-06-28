package eg.edu.cu.fci.ecampus.fci_e_campus.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import eg.edu.cu.fci.ecampus.fci_e_campus.R;

public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = WelcomeActivity.class.getSimpleName();

    final static String EXTRA_USER_TYPE = "USER_TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences authSharedPreferences
                = getSharedPreferences(getString(R.string.authentication_shared_preference_file_name),
                Context.MODE_PRIVATE);
        if (authSharedPreferences != null) {
            String token = authSharedPreferences.getString(getString(R.string.saved_token_key), null);
            if (token != null) {
                // user already logged in
                Log.d(TAG, token);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

        Button studentButton = findViewById(R.id.student_button);
        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                intent.putExtra(EXTRA_USER_TYPE, getString(R.string.student_user_type));
                startActivity(intent);
                finish();
            }
        });

        Button teacherButton = findViewById(R.id.teacher_button);
        teacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                intent.putExtra(EXTRA_USER_TYPE, getString(R.string.teacher_user_type));
                startActivity(intent);
                finish();
            }
        });
    }

}
