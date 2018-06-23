package eg.edu.cu.fci.ecampus.fci_e_campus.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.fragments.AnnouncementFragment;
import eg.edu.cu.fci.ecampus.fci_e_campus.fragments.CalendarFragment;
import eg.edu.cu.fci.ecampus.fci_e_campus.fragments.MyCoursesFragment;
import eg.edu.cu.fci.ecampus.fci_e_campus.fragments.OverviewFragment;


public class OverviewActivity extends AppCompatActivity implements CalendarFragment.OnFragmentInteractionListener,
        OverviewFragment.OnFragmentInteractionListener, MyCoursesFragment.OnFragmentInteractionListener {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


        NavigationView navigationView = findViewById(R.id.nav_view);

        setupDrawerContent(navigationView);

        OverviewFragment fragment = new OverviewFragment();
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_frame, fragment).commit();
        setTitle("Overview");
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_overview:
                fragmentClass = OverviewFragment.class;
                break;
            case R.id.nav_calendar:
                fragmentClass = CalendarFragment.class;
                break;
            case R.id.nav_announcements:
                fragmentClass = AnnouncementFragment.class;
                break;
            case R.id.nav_my_courses:
                fragmentClass = MyCoursesFragment.class;
                break;
            default:
                fragmentClass = CalendarFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawerLayout.closeDrawers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void logout() {
        // remove auth data fro shared preferences
        SharedPreferences authSharedPreferences
                = getSharedPreferences(getString(R.string.authentication_shared_preference_file_name),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = authSharedPreferences.edit();
        editor.clear();
        editor.apply();

        // redirect to welcome activity
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
