package com.example.jakubkalinowski.contractfoxandroid;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jakubkalinowski.contractfoxandroid.Model.Review;
import com.example.jakubkalinowski.contractfoxandroid.Navigation_Fragments.ContractorScheduleFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContractorProfileActivity extends AppCompatActivity {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    float overAllrating ;
    DrawerActivity drawerActivity = new DrawerActivity();
    public static final String ARG_ITEM_ID = "item_id";

   Fragment fragment = new ContractorScheduleFragment();
    String param = "kj";
    private static final String TAG = "Firebase_TAG!!" ;
    //[Firebase_variable]**
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener; //signed_in state listener object



    private DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance()
            .getReference();

    //private Estimate.OnFragmentInteractionListener mListener;
    private Member m;
    public Boolean option;
    String contractorID ;
    RatingBar ratingForContractor ;
    private String currentAuthenticatedUserID;

    //UI component variables
    private Button estimateButton, messageButton, availabilityButton;
    private TextView address, phoneNumber, companyName, website, emailAddress, fullName, miles;
    private LinearLayout callButton, directionsButton, websiteButton, skillsButton, reviewsButton;

    public String urlAddress;

    final private LinearLayout.LayoutParams etm = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
    //imageView
    private CircleImageView mCircleProfileImageView;
    private Bitmap mProfileImageBitmap;

    public Boolean getOption() {
        return option;
    }

    public void setOption(Boolean option) {
        this.option = option;
    }

    public ContractorProfileActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor_profile);

        //here you get the stuff passed to you from previous activity. which is the ID of the clicked contractor
        savedInstanceState = getIntent().getExtras();
        contractorID = savedInstanceState.getString("id");
        overAllrating = savedInstanceState.getFloat("overAllrating");

        Log.d("xyz-prof", contractorID);// yay it worked.
        //ok jakub here is how you would get anything you want on the clicked user.
        /*
       mFirebaseDatabaseReference.child("usersInChat").child(contractorID). anything you want here after dot ;

       here is a full example :
        mFirebaseDatabaseReference.child("usersInChat").child(contractorID).child("firstName") // will give you firstname
        mFirebaseDatabaseReference.child("usersInChat").child(contractorID).child("phonenNo") // will give you phone num
       so in conclusion, i am pasing you the id of the contraactor from the previous page and
       here you can make a quick call to db to get what you want. no going over a list. you have the id.
         */





        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    // User is signed in

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    mFirebaseDatabaseReference
                            .child("usersInChat").child(user.getUid().toString())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child("contractorOption").getValue().equals(true)) {

                                        //need null handlers here
                                        Contractor m = dataSnapshot.getValue(Contractor.class);

//                                        option = true;
                                        setOption(true);

                                        address.setText(m.getAddress().toString());
                                        phoneNumber.setText(m.getPhoneNo());
                                        companyName.setText(m.getBusinessWebsiteURL());
                                        website.setText(m.getBusinessWebsiteURL());
                                        //miles.setText();
                                        urlAddress = m.getBusinessWebsiteURL();

                                    } else {
                                        Homeowner m = (Homeowner) dataSnapshot.getValue(Homeowner.class);

//                                        option = false;
                                        setOption(false);
                                        address.setText(m.getAddress().toString());
                                        phoneNumber.setText(m.getPhoneNo());
                                        //fullName.setText(m.getFullName().toString());
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };

//        if (savedInstanceState == null) {
//            // Create the detail fragment and add it to the activity
//            // using a fragment transaction.
//            Bundle arguments = new Bundle();
//            arguments.putString(com.example.jakubkalinowski.contractfoxandroid.Contractor_Fragments.Estimate.ARG_ITEM_ID,
//                    getIntent().getStringExtra(com.example.jakubkalinowski.contractfoxandroid.Contractor_Fragments.Estimate.ARG_ITEM_ID));
//            com.example.jakubkalinowski.contractfoxandroid.Contractor_Fragments.Estimate fragment = new Estimate();
//            fragment.setArguments(arguments);
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.activity_contractor_profile, fragment)
//                    .commit();
//        }

        availabilityButton = (Button) findViewById(R.id.availability);
        estimateButton = (Button) findViewById(R.id.aprofile_estimate_button);
        //messageButton = (Button) findViewById(R.id.aprofile_message_button);
        callButton = (LinearLayout) findViewById(R.id.acall_button);
        directionsButton = (LinearLayout)findViewById(R.id.adirections_button);
        websiteButton = (LinearLayout)findViewById(R.id.awebsite_button);
        skillsButton = (LinearLayout)findViewById(R.id.askills_button);
        reviewsButton = (LinearLayout)findViewById(R.id.areviews_button);
        ratingForContractor = (RatingBar) findViewById(R.id.ratingBar) ;
        ratingForContractor.setRating(overAllrating);


        availabilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // FrameLayout fl = (FrameLayout) findViewById(R.id.displayArea_ID);
                Fragment fragment = new ContractorScheduleFragment();
              //  FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.displayArea_ID, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        estimateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ContractorProfileActivity.this, EstimateActivity.class);
                String [] id = {contractorID };
                i.putExtra("id", id) ;
                startActivity(i);
            }
        });
//
//        messageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent i = new Intent(ContractorProfileActivity.this, MessageActivity.class);
////                startActivity(i);
//            }
//        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "8453327029"));
                startActivity(intent);            }
        });

//        directionsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(ContractorProfileActivity.this, MapsActivity.class);
//                startActivity(i);
//            }
//        });

        websiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(urlAddress));
                startActivity(intent);
            }
        });

        skillsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ContractorProfileActivity.this, SkillSetActivity.class);
                startActivity(i);
            }
        });

        reviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onCreateReviewDialog();
            }
        });

    }


    public Dialog onCreateReviewDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ContractorProfileActivity.this);
        alertDialog.setTitle("Write a Review");
        alertDialog.setMessage("");
        LinearLayout linear = new LinearLayout(getApplicationContext());
//        TextView textEmail = new TextView(getApplicationContext());
//        textEmail.setText("Email: ");
        final RatingBar rb = new RatingBar(getApplicationContext());
        rb.setRating(0);



        final EditText description = new EditText(getApplicationContext());
        description.setHint("Description");
        description.setMinHeight(150);
       // description.setBackgroundResource(R.drawable.border);
        description.setHintTextColor(0xFFBCBCBC);
        description.setTextColor(0xFFBCBCBC);

        rb.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));

       // description.setLayoutParams(etm);


//        Button send = new Button(getApplicationContext());
//        send.setText("Submit");
        alertDialog.setNegativeButton("Submit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String desc = description.getText().toString(); //descritption
                        int numOfStars = (int) rb.getRating(); //nummber of stars
                        //database work goes here.
                        double starsDouble = (double) numOfStars;

                        saveReviewInDB(desc , starsDouble);

                    }
                });
        //send.setLayoutParams(etm);
        linear.setOrientation(LinearLayout.VERTICAL);
        linear.addView(description);
        linear.addView(rb);
        // linear.addView(send);
        alertDialog.setView(linear);
        return alertDialog.show();
    }

    // You can put the DB code here.
    private void saveReviewInDB(String description , double numOfStars) {

        String currentReviewerUserId = DrawerActivity.currentUserId ; //this is the current user id.
       // contractorID is a string variable available in this activity. it is being passed from previous activity.
        //
        String firebasePushKey = mFirebaseDatabaseReference.child("contractor_reviews").push().
                getKey();
        HashMap<String, Object> dateMap= new HashMap<String, Object>();
        dateMap.put("date", ServerValue.TIMESTAMP);


        Review review = new Review(currentReviewerUserId,contractorID,dateMap, null,
                description,numOfStars, firebasePushKey);

        mFirebaseDatabaseReference.child("contractor_reviews").child(contractorID).child(firebasePushKey)
                .setValue(review);

        //ok so every contractor needs to haave an overall rating attribute in db. Just one number.
        mFirebaseDatabaseReference.child("users").child(contractorID).child("overAllrating").setValue(numOfStars) ;
        //each contractor id is the parent key and the childs are firebase push key with containing child object

        //current user id--> reviwer
        //reviewee id -->
        //timestamp
        //description and number of starts

    }


}
