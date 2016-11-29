package com.example.jakubkalinowski.contractfoxandroid.Navigation_Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jakubkalinowski.contractfoxandroid.Activity.ChatListActivity;
import com.example.jakubkalinowski.contractfoxandroid.Model.ChatSession;
import com.example.jakubkalinowski.contractfoxandroid.R;
import com.example.jakubkalinowski.contractfoxandroid.helper_classes.RecentConverationsRecyclerViewAdapter;
import com.example.jakubkalinowski.contractfoxandroid.helper_classes.RecyclerViewItemTouchHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecentConversationsListFragment extends Fragment {
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.

    //---- VARIABLES RELATING TO FIREBASE [START]-----------//
    private DatabaseReference mDatabaseReference;
    private String mCurrentUserID;
    //---- VARIABLES RELATING TO FIREBASE [END]-----------//


    //-----VARIABLES LIKE LIST, ADAPTER ETC RELATING TO RECYCELRVIEW [START]------//
    private List<ChatSession> mChatSessionList = new ArrayList<>();
    private RecentConverationsRecyclerViewAdapter mConversationListAdapter;
    private RecyclerView mConversationsListRecyclerView;
    //-----VARIABLES LIKE LIST, ADAPTER ETC RELATING TO RECYCELRVIEW [END]------//


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.recent_conversation_list_layout, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //------INITIALIZING FIREBASE REFERENCE VARIABLES [START]----------//
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            mCurrentUserID = user.getUid();

        } else {
            // No user is signed in
        }
        //------INITIALIZING FIREBASE REFERENCE VARIABLES [END]----------//

        //-----INITIALIZE AND SET RECYCLERVIEW_RELATED VARIABLES [START]---------//
        mConversationsListRecyclerView = (RecyclerView) view.findViewById
                (R.id.recent_conversationsList_recycler_view);
        mConversationListAdapter = new RecentConverationsRecyclerViewAdapter(mChatSessionList);
        mConversationsListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mConversationsListRecyclerView.setAdapter(mConversationListAdapter);
        //-----INITIALIZE AND SET RECYCLERVIEW_RELATED VARIABLES [END]---------//


        //-------ATTACHING CHILD EVENT LISTENER BECAUSE WE'RE DEALING WITH LISTS[START]----//
        mDatabaseReference.child("users/" + mCurrentUserID + "/chatSessions")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final String conversationKey = dataSnapshot.getKey();
                        mDatabaseReference.child("chatSessions/" + conversationKey).
                                addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        final ChatSession listChatSessionItem = dataSnapshot.
                                                getValue(ChatSession.class);
                                        if(listChatSessionItem != null){
                                            Map<String, Boolean> usersInChat = listChatSessionItem.getUsersInChat();
                                            usersInChat.remove(mCurrentUserID);
                                            Map.Entry<String,Boolean> entry=usersInChat.entrySet().iterator().next();
                                            final String recipientKey = entry.getKey();
                                            mDatabaseReference.child("users").child(recipientKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    String recipientFirstName = dataSnapshot.child("firstName").
                                                            getValue(String.class);
                                                    String recipientUserId = dataSnapshot.getKey();
                                                    listChatSessionItem.setRecipientNameForCurrentUser(recipientFirstName);
                                                    listChatSessionItem.setRecipientUserId(recipientUserId);
                                                    mChatSessionList.add(listChatSessionItem);
                                                    mConversationListAdapter.notifyDataSetChanged();
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        //READ FAILED, CATCH ERROR HERE
                                    }
                                });
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        //-------ATTACHING CHILD EVENT LISTENER BECAUSE WE'RE DEALING WITH LISTS[END]----//
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

        mConversationsListRecyclerView.addOnItemTouchListener
                (new RecyclerViewItemTouchHelper(getActivity(),
                        new RecyclerViewItemTouchHelper.
                                OnItemClickListener() {

                            @Override
                            public void onClick(View view, int position) {

                                //Options, edit, view, delete
                                mChatSessionList.get(position).getRecipientNameForCurrentUser();
                                Toast.makeText(getActivity(),
                                        mChatSessionList.get(position).getRecipientNameForCurrentUser(),
                                        Toast.LENGTH_SHORT).show();

                                Intent chatListIntent = new Intent(getActivity(), ChatListActivity.class);
                                chatListIntent.putExtra("chatSessionId", mChatSessionList.
                                        get(position).firebasePushId);
                                chatListIntent.putExtra("recipientId", mChatSessionList.
                                        get(position).getRecipientUserId());
                                chatListIntent.putExtra("recipientName", mChatSessionList.
                                        get(position).getRecipientNameForCurrentUser());
                                getActivity().startActivity(chatListIntent);
                            }

                            @Override
                            public void onLongClick(View view, int position) {
//                                Toast.makeText(getActivity(),
//                                        String.valueOf(singleDayServicesList.get(position).getSessionDate_Milliseconds_Key()),
//                                        Toast.LENGTH_SHORT).show();


                            }
                        },
                        mConversationsListRecyclerView));
    }
}