package com.example.jakubkalinowski.contractfoxandroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.jakubkalinowski.contractfoxandroid.Navigation_Fragments.ContractorScheduleFragment;
import com.example.jakubkalinowski.contractfoxandroid.Navigation_Fragments.Home;
import com.example.jakubkalinowski.contractfoxandroid.Navigation_Fragments.MyProfile;
import com.example.jakubkalinowski.contractfoxandroid.Navigation_Fragments.ProfileEdit;
import com.example.jakubkalinowski.contractfoxandroid.Navigation_Fragments.RecentConversationsListFragment;
import com.example.jakubkalinowski.contractfoxandroid.homePage_Fragments.BackYard;
import com.example.jakubkalinowski.contractfoxandroid.homePage_Fragments.Exterior;
import com.example.jakubkalinowski.contractfoxandroid.homePage_Fragments.Interior;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,

        MyProfile.OnFragmentInteractionListener, ProfileEdit.OnFragmentInteractionListener, Home.OnFragmentInteractionListener,

        Exterior.OnFragmentInteractionListener, Interior.OnFragmentInteractionListener,
        BackYard.OnFragmentInteractionListener {

    private FirebaseAuth.AuthStateListener mAuthListener; //signed_in state listener object
    DrawerLayout drawer;
    private static DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance()
            .getReference();
    public static FragmentTransaction ft;
    boolean notContractor = false ;
    NavigationView navigationView;
    Menu nav_Menu;
    public static String currentUserId ;
    static String currentUserFirstName  ="kladimer";
    LinearLayout tab1, tab2, tab3, tab4 ;
    Button exteriorButton , interiorButton, backyardButton , searchButton;
    EditText searchBar;

    //TabHost th;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drawer);



        navigationView = (NavigationView) findViewById(R.id.nav_view);
        searchButton = (Button) findViewById(R.id.mainSearchButton);
        searchButton.setOnClickListener(searchListerner);
        searchBar = (EditText) findViewById(R.id.searchBar_ID);
        nav_Menu = navigationView.getMenu();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // if (c)

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("contractorhere", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("contractorhere", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                currentUserId = user.getUid();
                if (user != null) {
                    mFirebaseDatabaseReference
                            .child("users").child(user.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.d("contractorhere" , " before if");
                                    if (dataSnapshot.child("contractorOption").getValue().equals(true)){
                                        //need null handlers here
                                      //  Contractor m = dataSnapshot.getValue(Contractor.class);

                                        Log.d("contractorhere" , " true now");
                                    }
                                    else{
                                      //  Homeowner m = (Homeowner)dataSnapshot.getValue(Homeowner.class);
                                        Log.d("contractorhere" , " in else now");
                                        notContractor = true ;
                                        nav_Menu.findItem(R.id.nav_myprofile).setVisible(false);
                                        nav_Menu.findItem(R.id.contractor_availability_).setVisible(false);

                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                } else {


                }



        //The Drawer will display different items depending on the user being a contractor, or homeowner.




        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displayFragment(R.id.homee);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();


        if( notContractor){
            nav_Menu.findItem(R.id.nav_myprofile).setVisible(false);
            nav_Menu.findItem(R.id.contractor_availability_).setVisible(false);


        }

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            currentUserId = user.getUid();
//
//        } else {
//            // No user is signed in
//            Log.i("ladimmm" ,"not signed in !!");
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        displayFragment(item.getItemId());

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void displayFragment(int viewId) {

        Fragment fragment = null;
        String title = "";

        switch (viewId) {
            case R.id.nav_profile_edit:
                fragment = new ProfileEdit();
                title = "Edit Profile";
                break;

            case R.id.nav_myprofile:
                fragment = new MyProfile();
                title = "My Profile";
                break;
            case R.id.messages:
                Toast.makeText(getApplicationContext(), "Messages", Toast.LENGTH_LONG).show(); //testing/debugging
                fragment = new RecentConversationsListFragment();
                title = "recentConversationsListFragment";
                break;

            case R.id.homee:
                fragment = new Home();
                title = "Home";
                break;
            case R.id.contractor_availability_:
                //fragment work here
                fragment = new ContractorScheduleFragment();
                title = "contractorschedulefragmentTag";
                break;

            case R.id.log_out:
                // Log out action here
                Toast.makeText(getApplicationContext(), "Log Out ", Toast.LENGTH_LONG).show(); //testing/debugging
                FirebaseAuth.getInstance().signOut();
                this.finish();
                break;
        }


            if (fragment != null) {
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.displayArea_ID, fragment, title);
                ft.commit();
            }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


    }

    //Onclick listener for search
    View.OnClickListener searchListerner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // search logic takes place/deligated here.

            Intent i = new Intent(getApplicationContext(), SearchViewListActivity.class);
            //think of a clever way to reuse code here.
            // i.putExtra(searchBar.getText().toString() ,true);
            i.putExtra("serachedItem", searchBar.getText().toString());
            startActivity(i);
        }
    };

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public String passedData(String s1, String s2) {
        return null;
    }


}
