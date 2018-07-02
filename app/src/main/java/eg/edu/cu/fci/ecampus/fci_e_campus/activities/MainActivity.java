package eg.edu.cu.fci.ecampus.fci_e_campus.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.fragments.AllTasksFragment;
import eg.edu.cu.fci.ecampus.fci_e_campus.fragments.AnnouncementFragment;
import eg.edu.cu.fci.ecampus.fci_e_campus.fragments.MapFragment;
import eg.edu.cu.fci.ecampus.fci_e_campus.fragments.MyCoursesFragment;
import eg.edu.cu.fci.ecampus.fci_e_campus.fragments.OverviewFragment;
import eg.edu.cu.fci.ecampus.fci_e_campus.fragments.ScheduleFragment;
import eg.edu.cu.fci.ecampus.fci_e_campus.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,
        OverviewFragment.OnFragmentInteractionListener, MyCoursesFragment.OnFragmentInteractionListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.drawer_layout) DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // hide all tasks item from navigationView if TA or Prof (not student)
        String userType = getUserTypeFromSharedPref();
        Log.d(TAG, userType);
        if (!userType.equals(getString(R.string.student_user_type))) {
            MenuItem navAllTasks = navigationView.getMenu().findItem(R.id.nav_all_tasks);
            navAllTasks.setVisible(false);
            navAllTasks.setEnabled(false);
        }

        // open the overview fragment (the default fragment)
        OverviewFragment fragment = new OverviewFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment).commit();
        setTitle(getString(R.string.title_activity_overview));
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_overview:
                fragment = new OverviewFragment();
                break;
            case R.id.nav_schedule:
                fragment = new ScheduleFragment();
                break;
            case R.id.nav_all_tasks:
                fragment = new AllTasksFragment();
                break;
            case R.id.nav_announcements:
                fragment = new AnnouncementFragment();
                break;
            case R.id.nav_my_courses:
                fragment = new MyCoursesFragment();
                break;
            case R.id.nav_map:
                fragment = new MapFragment();
                break;
            case R.id.nav_settings:
                fragment = new SettingsFragment();
                break;
            default:
                fragment = new OverviewFragment();
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        // Set action bar title
        setTitle(item.getTitle());

        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    private String getUserTypeFromSharedPref() {
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.authentication_shared_preference_file_name),
                Context.MODE_PRIVATE);

        return sharedPref.getString(getString(R.string.saved_user_type_key), null);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
