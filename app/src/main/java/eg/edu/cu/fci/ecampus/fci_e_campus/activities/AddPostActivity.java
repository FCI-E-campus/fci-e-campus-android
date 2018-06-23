package eg.edu.cu.fci.ecampus.fci_e_campus.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import eg.edu.cu.fci.ecampus.fci_e_campus.R;

public class AddPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
