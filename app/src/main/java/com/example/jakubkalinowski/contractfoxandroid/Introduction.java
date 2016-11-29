package com.example.jakubkalinowski.contractfoxandroid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Introduction extends AppCompatActivity {

    ViewPager viewPager;
    CustomSwipeAdapter adapter;

    Button signUp;
//    TextView textView;
//    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new CustomSwipeAdapter(this);
        viewPager.setAdapter(adapter);

        signUp = (Button)findViewById(R.id.signUp);
//        textView = (TextView) findViewById(R.id.title_ID);
//        textView2 = (TextView) findViewById(R.id.text2_ID);

        signUp.setTextColor(Color.WHITE);
//        textView.setTextColor(Color.RED);
//        textView2.setTextColor(Color.DKGRAY);

        DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference(); //Commented out for testing
        //DatabaseReference myRef = mFirebaseDatabaseReference.getReference();
        //myRef.child("test1").child("test2").setValue("Hello, World Bitch");
        //Toast.makeText(getApplicationContext(), "SIGN UP Button is clicked"+myRef.toString(), Toast.LENGTH_LONG).show();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent navigation = new Intent(Intent.ACTION_VIEW, Uri
//                        .parse("http://maps.google.com/maps?saddr="
//                                +"-121.86" + ","
//                                + "37.33" + "&daddr="
//                                + "-122.0704" + "," + "37.0366"));

                // Intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                //startActivity(navigation);

                Intent i = new Intent(Introduction.this, LoginActivity.class);
//                Intent i = new Intent(Introduction.this, ContractorProfileActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}


