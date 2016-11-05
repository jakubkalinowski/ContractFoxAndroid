package com.example.jakubkalinowski.contractfoxandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContractorProfileActivity extends AppCompatActivity {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

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

    //UI component variables
    private Button estimateButton, messageButton;
    private TextView address, phoneNumber, companyName, website, emailAddress, fullName, miles;
    private LinearLayout callButton, directionsButton, websiteButton, skillsButton, reviewsButton;

    public String urlAddress;

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

        Log.i("id-:", contractorID);// yay it worked.
        //ok jakub here is how you would get anything you want on the clicked user.
        /*
       mFirebaseDatabaseReference.child("users").child(contractorID). anything you want here after dot ;

       here is a full example :
        mFirebaseDatabaseReference.child("users").child(contractorID).child("firstName") // will give you firstname
        mFirebaseDatabaseReference.child("users").child(contractorID).child("phonenNo") // will give you phone num
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
                            .child("users").child(user.getUid().toString())
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

        estimateButton = (Button) findViewById(R.id.aprofile_estimate_button);
        messageButton = (Button) findViewById(R.id.aprofile_message_button);
        callButton = (LinearLayout) findViewById(R.id.acall_button);
        directionsButton = (LinearLayout)findViewById(R.id.adirections_button);
        websiteButton = (LinearLayout)findViewById(R.id.awebsite_button);
        skillsButton = (LinearLayout)findViewById(R.id.askills_button);
        reviewsButton = (LinearLayout)findViewById(R.id.areviews_button);

        estimateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ContractorProfileActivity.this, EstimateActivity.class);
                startActivity(i);
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ContractorProfileActivity.this, MessageActivity.class);
                startActivity(i);
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "8453327029"));
                startActivity(intent);            }
        });

        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ContractorProfileActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });

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
                Intent i = new Intent(ContractorProfileActivity.this, ReviewsActivity.class);
                startActivity(i);
            }
        });

    }

}
