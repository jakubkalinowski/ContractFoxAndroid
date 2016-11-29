package com.example.jakubkalinowski.contractfoxandroid;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.net.Uri;

import android.support.v4.app.Fragment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.jakubkalinowski.contractfoxandroid.Navigation_Fragments.Messages;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class MessageQue extends AppCompatActivity implements
        Messages.OnFragmentInteractionListener {
    DatabaseReference messageReferencesDatabaseReference;
    Button b;
    boolean back = true ;
    FragmentTransaction ft ;
    EditText search ;
    LinearLayout parent ;
    FrameLayout frame ;
    public static String receiversName ;
    public static String clickedContacsID ;
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter adapter;
    Fragment fragment =   new Messages();

    List<String> lsitOfIDsForContacs = new ArrayList<>();
    static  List <String > messageContacts =  new ArrayList<>();

    @Override
    public void onBackPressed() {
        if(back){
            super.onBackPressed();
        }else{
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            //  frame.removeAllViews();

            parent.setVisibility(View.VISIBLE);
            back = true ;
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_que);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        Log.i("displaynam" , DrawerActivity.currentUserFirstName);
        messageReferencesDatabaseReference = FirebaseDatabase.getInstance()
                .getReference().child("messageReferences");


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_messages);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //dummy data added to list for testing
        //messageContacts.add("JAkub Kalinowski");

        messageReferencesDatabaseReference.child(DrawerActivity.currentUserId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageContacts.clear();
                Map <String , String> messageNameListMap = (Map)dataSnapshot.getValue();

                Iterator it = messageNameListMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    //System.out.println(pair.getKey() + " = " + pair.getValue());
                    Log.d("pairValue", pair.getValue().toString());
                    messageContacts.add(pair.getValue().toString());
                    lsitOfIDsForContacs.add(pair.getKey().toString());

                }



                adapter = new MyRecyclerViewAdapter(MessageQue.this, messageContacts);
                mRecyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        parent = (LinearLayout) findViewById(R.id.alllllllll);
        frame = (FrameLayout) findViewById(R.id.msgContainer);
        search = (EditText)findViewById(R.id.searchNames) ;
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // search.setMinHeight(100);
            }
        });

        b = (Button)findViewById(R.id.butotnTest);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                parent.setVisibility(View.INVISIBLE);
//
//                TextView tv = (TextView) view ;
//
//
//                receiversName = tv.getText().toString();
//                back = false; //sets the value to false, to fix the back button issue.
//
//                ft = getSupportFragmentManager().beginTransaction();
//                Log.i("fragment", "not null");
//                ft.add(R.id.msgContainer, fragment);
//                ft.commit();
//
//            }
//        });

//        mRecyclerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TextView text = (TextView) view;
//
//                String contactName = text.getText().toString();
//
//                Log.i("thisName", contactName);
//            }
//        });


    }//onCreate



    @Override
    public void onFragmentInteraction(Uri uri) {

    }



    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.CustomViewHolder> {
        private List<String> feedItemList;
        private Context mContext;
        MessageQue mq = new MessageQue();

        public MyRecyclerViewAdapter(Context context, List<String> feedItemList) {
            this.feedItemList = feedItemList;
            this.mContext = context;
        }

        //  @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row_messages, null);
            CustomViewHolder viewHolder = new CustomViewHolder(view);
            return viewHolder;
        }

        //@Override
        public void onBindViewHolder(CustomViewHolder customViewHolder, final int position) {
            //  FeedItem feedItem = feedItemList.get(i);

//        //Render image using Picasso library
//        if (!TextUtils.isEmpty(feedItem.getThumbnail())) {
//            Picasso.with(mContext).load(feedItem.getThumbnail())
//                    .error(R.drawable.placeholder)
//                    .placeholder(R.drawable.placeholder)
//                    .into(customViewHolder.imageView);
//        }

            //Setting text view title
            customViewHolder.textView.setText(MessageQue.messageContacts.get(position));
            //on click listener upon clicking each contact name.
            customViewHolder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mq.onRestart();
                }
            });


            customViewHolder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    parent.setVisibility(View.INVISIBLE);
                    //gets the names of the contact
                    TextView tv = (TextView) view ;
                    receiversName = tv.getText().toString();

                    clickedContacsID = lsitOfIDsForContacs.get(position);

                    back = false; //sets the value to false, to fix the back button issue.

                    ft = getSupportFragmentManager().beginTransaction();
                    Log.i("fragment", "not null");
                    ft.add(R.id.msgContainer, fragment);
                    ft.commit();
                }
            });

        }

        // @Override
        public int getItemCount() {
            return (null != feedItemList ? feedItemList.size() : 0);
        }

        class CustomViewHolder extends RecyclerView.ViewHolder {
            protected ImageView imageView;
            protected TextView textView;

            public CustomViewHolder(View view) {
                super(view);
                this.imageView = (ImageView) view.findViewById(R.id.thumbnail1);
                this.textView = (TextView) view.findViewById(R.id.nameOfperson);
            }
        }
    }



}
