package eg.edu.cu.fci.ecampus.fci_e_campus.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import eg.edu.cu.fci.ecampus.fci_e_campus.R;

public class WelcomeActivity extends AppCompatActivity {

    final static String EXTRA_USER_TYPE = "USER_TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button studentButton = findViewById(R.id.student_button);
        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                intent.putExtra(EXTRA_USER_TYPE, getString(R.string.student_user_type));
                startActivity(intent);
            }
        });

        Button teacherButton = findViewById(R.id.teacher_button);
        teacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                intent.putExtra(EXTRA_USER_TYPE, getString(R.string.teacher_user_type));
                startActivity(intent);
            }
        });
    }

}
