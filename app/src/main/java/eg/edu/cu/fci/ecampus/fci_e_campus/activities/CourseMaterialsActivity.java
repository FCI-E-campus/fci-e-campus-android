package eg.edu.cu.fci.ecampus.fci_e_campus.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import eg.edu.cu.fci.ecampus.fci_e_campus.Adapters.MaterialsAdapter;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Material;

public class CourseMaterialsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_materials);

        String courseTitle = getIntent().getStringExtra("course_title");
        setTitle(courseTitle + " - Materials");

        Material x = new Material();
        x.setName("Lecture 1");
        x.setUploaderUsername("Noura Abdelhameed");
        x.setDescription("5 November");

        Material y = new Material();
        y.setName("Lecture 1");
        y.setUploaderUsername("Noura Abdelhameed");
        y.setDescription("5 November");

        Material z = new Material();
        z.setName("Lecture 1");
        z.setUploaderUsername("Noura Abdelhameed");
        z.setDescription("5 November");

        ArrayList<Material> materials = new ArrayList<>();
        materials.add(x);
        materials.add(y);
        materials.add(z);

        MaterialsAdapter adapter = new MaterialsAdapter(this, materials);
        ListView listView = findViewById(R.id.material_list_view);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
