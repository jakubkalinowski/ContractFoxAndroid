package com.example.jakubkalinowski.contractfoxandroid;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;

import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.jakubkalinowski.contractfoxandroid.dummy.DummyContent;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.actions.SearchIntents;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

///**
// * An activity representing a list of Items. This activity
// * has different presentations for handset and tablet-size devices. On
// * handsets, the activity presents a list of items, which when touched,
// * lead to a {@link SearchViewDetailActivity} representing
// * item details. On tablets, the activity presents the list of items and
// * item details side-by-side using two vertical panes.
// */
public class SearchViewListActivity extends AppCompatActivity {

    //[Firebase_variable]**
    private FirebaseAuth mAuth;
    boolean contractorFound = false ;
    RadioButton distanceRadio ,reviewRadio ;
    //  private FirebaseAuth.AuthStateListener mAuthListener; //signed_in state listener object
    private DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance()
            .getReference();

    public static HashMap < Integer, String>  map = new HashMap<>();
    public static String[] data = {"company 1", "company 2","Ladimer"};
    public static List<String> ITEMSs = Arrays.asList(data);
    public static List<String> ITEMS = ITEMSs;
    int count = 0;
    List<Float> ratingsList = new ArrayList<>();
    String generalSerach;

    private static LruCache<String, List<String>> cachedMemory ;

    public static List<String> companyNames  =  new ArrayList<>();

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    static FloatingActionButton fab;
    private boolean mTwoPane;
    List<String> listFromCache;
    EditText searchBar ;
    //static List<String> names = new ArrayList<>();
    Button searchButton;
    String searchedContent ;

    RatingBar stars ;
    String searchedItem ;
    ProgressBar progressBar ;
    boolean flag = false;


    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference imageStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchview_list);
        savedInstanceState = getIntent().getExtras();
        flag = savedInstanceState.getBoolean("flag");

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("ProfilePictures");

        //gets the name of the clicked item from the previous activity
        searchBar = (EditText) findViewById(R.id.searchBarInList_ID);
        distanceRadio = (RadioButton) findViewById(R.id.distanceRadio);
        reviewRadio = (RadioButton) findViewById(R.id.reviewRadio);

        distanceRadio.setOnClickListener( distanceListener);
        reviewRadio.setOnClickListener(reviewListener);

        if(!flag) { // search is coming from google voice
            Intent searchedIntent = getIntent();

            if (SearchIntents.ACTION_SEARCH.equals(searchedIntent.getAction())) {
                generalSerach = searchedIntent.getStringExtra(SearchManager.QUERY);
                for (String s : SearchHelper.houseSections ){
                    if (generalSerach.toLowerCase().contains(s)){
                        searchedItem = s ;
                        contractorFound =true ;
                        break;
                    }//else{
                       // Toast.makeText(getApplicationContext() , "No results were found!", Toast.LENGTH_LONG).show();
                   // }
                }
                if( !contractorFound){
                    Toast.makeText(getApplicationContext() , "No results were found!", Toast.LENGTH_LONG).show();
                }
            }
        }else{ //search is coming from specific list, or serach bar is DrawerActivity
          generalSerach   = savedInstanceState.getString("serachedItem");
            for (String s : SearchHelper.houseSections ){
                if (generalSerach.toLowerCase().contains(s)){
                    searchedItem = s ;
                    break;
                }else{
                    Toast.makeText(getApplicationContext() , "No results were found!", Toast.LENGTH_LONG).show();
                }
            }
         //   Log.i("nothingin" , searchedItem);
            if(searchedItem == null){

                Log.i("nothingin" , "it is null here");
            }
        }


        progressBar = (ProgressBar)findViewById(R.id.progress_ID);
        //fire base stuff. This is where we get the info from firebase
        mAuth = FirebaseAuth.getInstance();
      //
        //  progressBar.setVisibility(View.VISIBLE);

        //// cache stuff below :
        final int maxMemorySzie = (int) Runtime.getRuntime().maxMemory() / 1024;
        final int cacheSize = maxMemorySzie / 10 ;
        cachedMemory = new LruCache<String, List<String>>(cacheSize){


            @Override
            protected int sizeOf(String key, List<String> value) {
                return super.sizeOf(key, value);
            }
        };
//
        try{
            listFromCache = getBitMapFromMemory(searchedItem);
        }catch(Exception e){
            e.printStackTrace();

        }

        if( listFromCache != null){
            companyNames = listFromCache ;

            Log.i("cacheladi", "cache is not empty");
            progressBar.setVisibility(View.INVISIBLE);
            View recyclerView = findViewById(R.id.searchview_list);
            assert recyclerView != null;

            setupRecyclerView((RecyclerView) recyclerView);
        }else{
            Log.i("cacheladi", "cache is empty");
            searchDB(false);
            //cache the list of company names with the searched content as key.

        }//end of else. if cahce is empty

        //hides the keyboard upon opening the activity.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        searchButton = (Button) findViewById(R.id.searchButtonInList);

        stars = (RatingBar) findViewById(R.id.ratingStars_ID1);
       // stars.setIsIndicator(true);

        savedInstanceState = getIntent().getExtras();
        if(savedInstanceState != null){
          if(generalSerach != null){
              searchBar.setText(generalSerach);
          }else{
              searchBar.setText(searchedItem);
          }
    }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        fab = (FloatingActionButton) findViewById(R.id.fab);


//        if (findViewById(R.id.contractor_profile) != null) {
        if (findViewById(R.id.searchview_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).

            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

    }//onCreate

    public void searchDB(final boolean haveTheRatingList){

        progressBar.setVisibility(View.VISIBLE);
                    mFirebaseDatabaseReference
                    .child("users").addListenerForSingleValueEvent (new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Iterable<DataSnapshot> dataSnapshotsList = dataSnapshot.getChildren();

                    for (DataSnapshot snapshot : dataSnapshotsList) {

                        //first lets see if member is contractor
                        if (snapshot.child("contractorOption").getValue().equals(true)  ) {

                         //   Log.d("letsseewhat", Double.toHexString(snapshot.child("overAllrating").getValue(Double.class)));
                            Iterable<DataSnapshot> skillList = snapshot.child("skillSet").getChildren();

                            //second for loop for checking if skill is there in the skillSet
                            for( DataSnapshot skill :  skillList){
                                if(skill.getValue().toString().equalsIgnoreCase(searchedItem)){
                                    if(! haveTheRatingList){
                                        ratingsList.add(snapshot.child("overAllrating").getValue(Float.class));
                                    }

                                    map.put(count , snapshot.getKey());
                                    count++;
                                    companyNames.add(snapshot.child("firstName").getValue().toString());
                                }
                            }
                        }
                    }
                    // Log.d("checkk-", Integer.toString(companyNames.size()));//dubugging help
                    //setting up recycler view
                    setBitMapToMemory(searchedItem , companyNames );
                    progressBar.setVisibility(View.INVISIBLE);
                    View recyclerView = findViewById(R.id.searchview_list);
                    assert recyclerView != null;
                    Log.d("checkk-", "recycler set");
                    setupRecyclerView((RecyclerView) recyclerView);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        companyNames.clear();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void printLsit() {
        for(String n : companyNames){
            Log.d("checkk-", n);

        }
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(companyNames, companyNames , ratingsList));
    }

    /////////////////////////////////////////inner class//////////////////////////////////////
    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        // public CardView cardView;

        private final List<String> mValues;
        private final List<String> mValues2;
        private final List<Float> rate;
        private final List<CheckBox> radios = new ArrayList<>();

        public SimpleItemRecyclerViewAdapter(List<String> items, List<String> items2 ,List<Float> ratings) {
            mValues = items;
            mValues2 = items2 ;
            rate = ratings ;
        }


        public  void clearList(){
            mValues.clear();
            mValues2.clear();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.searchview_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            // holder.mItem = mValues.get(position);
            holder.companyName.setText(mValues.get(position));
            Log.d("i-d-", " numberCalled");
            radios.add(holder.radioButton);
            // holder.numebrOfReviews.setText("6006"); // testing here to see if i can access. just putting name of contractor here.
            holder.numebrOfReviews.setText(mValues2.get(position));
            holder.stars.setRating(rate.get(position));
           // holder.image.( (ImageView) storageRef.child(map.get(position)).child("profilepic.jpeg")   );

            imageStorage = storage.getReference("ProfilePictures/" +
                    map.get(position)+"/profilepic.jpeg");
//            StorageReference imageRef = storageRef.child(map.get(position)).child("profilepic.jpeg");
            Glide.with(SearchViewListActivity.this).using(new FirebaseImageLoader()).load(imageStorage).into(holder.image);

            SearchViewListActivity.fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("i-d-", " yayyy");
                    String [] ids = new String[5];
                    int count2 = 0;
                    for(int i = 0; i <radios.size(); i++){
                        Log.d("i-d-", "in radio list");
                        if( radios.get(i).isChecked() ) {
                            Log.d("i-d-", " were in");
                            //  Log.d("i-d-", holder.companyName.getText().toString());
                            ids[count2] = map.get(i);
                            count2++ ;
                        }
                    }

                    Intent i = new Intent(SearchViewListActivity.this , EstimateActivity.class);

                    i.putExtra("id" , ids);
                    startActivity(i);

                }
            });

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        //ADDED FOR TESTING

                        //this is for tablet view, which will not work now.

                        //REMOVED FOR TESTING
                        Bundle arguments = new Bundle();
                        arguments.putString(Estimate.ARG_ITEM_ID, holder.mItem.id);
                        Estimate fragment = new Estimate();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.contractor_profile, fragment) // TESTING
                                .replace(R.id.activity_contractor_profile, fragment) // TESTING
                                .commit();

                    } else {

                        // Context context = v.getContext();
                        Intent intent = new Intent(getApplicationContext(), ContractorProfileActivity.class);

                        //intent.putExtra(holder.companyName.getText(), 0 );
                        Log.d("xyz-pos", String.valueOf(holder.getAdapterPosition()  ) );//debugging help
                        Log.d("xyz-id",  map.get( holder.getAdapterPosition()) );


                        intent.putExtra("id", map.get( holder.getAdapterPosition())  );
                        intent.putExtra("overAllrating" , ratingsList.get(holder.getAdapterPosition()));
                        startActivity(intent);

                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        ////////////////////////////////////////////////////////
        public  class ViewHolder extends RecyclerView.ViewHolder {

            //define an initialize all the textviewss here. even Stars.

            public final View mView;
            public final TextView companyName;
            public final TextView numebrOfReviews;
            public final CheckBox radioButton ;
            public final RatingBar stars ;
            public final ImageView image ;
            //public final TextView mContentView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                companyName = (TextView) view.findViewById(R.id.companyName_ID);
                radioButton = (CheckBox) view.findViewById(R.id.radio_ID);
                image = (ImageView) view.findViewById(R.id.imageForCont);
                stars = (RatingBar) view.findViewById(R.id.ratingStars_ID1);
                numebrOfReviews = (TextView) view.findViewById(R.id.numberOfReviewers); //testing here to see if i can access.
                // mContentView = (TextView) view.findViewById(R.id.content);
            }

            public void getRadio(){
            }
        }
        ////////////////////////////////////////////////////////////////////// innner-inner class
    }// ////////////////end of inner class///////////////////////////////////////////

    View.OnClickListener distanceListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getApplicationContext() , "Filtering results by distance ..." ,Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener reviewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getApplicationContext() , "Filtering results by review ..." ,Toast.LENGTH_SHORT).show();

            Collections.sort(ratingsList);
            Collections.reverse(ratingsList);
          companyNames.clear();
            searchDB(true);
        }
    };

    public List<String> getBitMapFromMemory (String key){
        return cachedMemory.get(key);
    }

    public void setBitMapToMemory (String key , List<String> bitmap){
        if(getBitMapFromMemory(key) == null){
            cachedMemory.put(key , bitmap);
        }

    }
}

