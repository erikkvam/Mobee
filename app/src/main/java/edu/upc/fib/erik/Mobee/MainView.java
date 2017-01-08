package edu.upc.fib.erik.Mobee;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

public class MainView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TitleViewFragment.OnFragmentInteractionListener,
        AddMovieFragment.OnFragmentInteractionListener, ActorSearchFragment.OnFragmentInteractionListener,
        AboutHelpFragment.OnFragmentInteractionListener, RecyclerViewFragment.OnFragmentInteractionListener {

    private FilmData filmData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        filmData = new FilmData(this);
        filmData.open();
        //initMovieData();
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToFragment(new AddMovieFragment());
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_activity);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null) {changeToFragment(new TitleViewFragment());}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);

        // Configure the search info and add any event listeners...

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_activity);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.title_view:
                changeToFragment(new TitleViewFragment());
                break;
            case R.id.recycler_view:
                changeToFragment(new RecyclerViewFragment());
                break;
            case R.id.actor_search:
                changeToFragment(new ActorSearchFragment());
                break;
            case R.id.add_movie:
                changeToFragment(new AddMovieFragment());
                break;
            case R.id.about_help:
                changeToFragment(new AboutHelpFragment());
                break;
        }

        item.setChecked(true);
        setTitle(item.getTitle());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_activity);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeToFragment(Fragment input) {
        try {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, input);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    List<Film> getAllFilms() {
        return filmData.getAllFilms();
    }

    private void initMovieData() {
        filmData.createFilm("Titol", "director", "pais", 1995, "protagonista", 5);
    }

    @Override
    protected void onResume() {
        filmData.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        filmData.close();
        super.onPause();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
