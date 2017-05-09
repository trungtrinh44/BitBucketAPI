package trung.bitbucket.view.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import trung.bitbucket.R;
import trung.bitbucket.interfaces.PresenterViewOps;
import trung.bitbucket.interfaces.ViewOps;
import trung.bitbucket.model.Repositories;
import trung.bitbucket.model.Repository;
import trung.bitbucket.model.UserInfo;
import trung.bitbucket.presenter.MainPresenter;
import trung.bitbucket.view.fragments.CreateNewRepoFragment;
import trung.bitbucket.view.fragments.RepoListFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ViewOps, RepoListFragment.OnListFragmentInteractionListener,
        CreateNewRepoFragment.OnFragmentInteractionListener {
    public static final String USER_INFO = "UserInfo";
    public static final String TOKEN = "myToken";
    private UserInfo userInfo;
    private PresenterViewOps presenter;
    private FragmentManager manager;
    private DrawerLayout drawer;
    private Fragment currentFragment;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            if (sharedPreferences.contains(USER_INFO)) {
                userInfo = new Gson().fromJson(sharedPreferences.getString(USER_INFO, ""), UserInfo.class);
                Log.d("SharePreferences", userInfo.username + " " + userInfo.displayName);
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                finish();
                startActivity(intent);
                return;
            }
        } else {
            userInfo = (UserInfo) extras.getSerializable(USER_INFO);
            sharedPreferences.edit().putString(USER_INFO, new Gson().toJson(userInfo)).apply();
            sharedPreferences.edit().putString(TOKEN, extras.getString(TOKEN)).apply();
        }
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", userInfo.username + " " + userInfo.displayName);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        manager = getSupportFragmentManager();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        presenter = new MainPresenter(this);
        if (sharedPreferences.contains(TOKEN))
            presenter.setToken(sharedPreferences.getString(TOKEN, ""));
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.repos);
        if (savedInstanceState == null) {
            Log.d("MainActivity", "create new repolistfragment");
            Fragment fragment = RepoListFragment.newInstance(userInfo);
            manager.beginTransaction().replace(R.id.content_main, fragment).commit();
        }
        View headerView = navigationView.getHeaderView(0);
        ((TextView) headerView.findViewById(R.id.userDisplayname)).setText(userInfo.displayName);
        ((TextView) headerView.findViewById(R.id.username)).setText(userInfo.username);
        Glide.with(this).load(userInfo.links.avatar.href).into((ImageView) headerView.findViewById(R.id.userIcon));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStack();
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else super.onBackPressed();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        if (item.getItemId() != R.id.logout) {
            if (item.isChecked()) {
                switch (item.getItemId()) {
                    case R.id.repos:
                        fragment = RepoListFragment.newInstance(userInfo);
                        break;
                }
                manager.beginTransaction().replace(R.id.content_main, fragment).commit();
                item.setChecked(true);
                drawer.closeDrawer(GravityCompat.START);
            }
        } else {
            drawer.closeDrawer(GravityCompat.START);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Log Out").setMessage("Do you want to log out ?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
                    sharedPreferences.edit().remove(TOKEN).remove(USER_INFO).apply();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    finish();
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.create().show();
        }
        return true;
    }

    @Override
    public void loginSuccessful(UserInfo userInfo, String token) {

    }

    public void callCreateNewRepo(PresenterViewOps presenter) {
        Fragment fragment = CreateNewRepoFragment.newInstance(this, presenter);
        manager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void returnRepositories(Repositories repos) {

    }

    @Override
    public void repoCreated(Repository repository) {

    }

    @Override
    public void storeToken(String token) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(TOKEN, token).apply();
        Log.d("storeToken", token);
    }

    @Override
    public void repoDeleted(Repository repo) {

    }

    @Override
    public void deleteRepo(Repository repo) {

    }

    @Override
    public void onListFragmentInteraction(Repository item) {

    }

    @Override
    public void onFragmentInteraction() {
        if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStack();
        }
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
}
