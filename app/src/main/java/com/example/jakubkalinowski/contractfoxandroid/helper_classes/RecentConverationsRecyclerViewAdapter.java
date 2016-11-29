package com.example.jakubkalinowski.contractfoxandroid.helper_classes;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jakubkalinowski.contractfoxandroid.Model.ChatSession;
import com.example.jakubkalinowski.contractfoxandroid.R;
import com.google.android.gms.vision.text.Text;

import java.util.List;

/**
 * Created by MD on 11/19/2016.
 */

public class RecentConverationsRecyclerViewAdapter extends
        RecyclerView.Adapter<RecentConverationsRecyclerViewAdapter.ConversationSessionViewHolder> {

    //list view for
    private List<ChatSession> recentConversationsList;

    public RecentConverationsRecyclerViewAdapter(List<ChatSession> recentConversationsList) {
        this.recentConversationsList = recentConversationsList;
    }

    public class ConversationSessionViewHolder extends RecyclerView.ViewHolder{
        private TextView conversationLastMessage, conversationProjectTitle,
                conversationLastMessageDate, conversationLastMessageTime, conversationRecipientName;
        private CardView conversationCardView;
        public ConversationSessionViewHolder(View itemView) {
            super(itemView);
            conversationLastMessage = (TextView) itemView.findViewById
                    (R.id.last_messae_recieved_recentchatList);
            conversationProjectTitle = (TextView) itemView.findViewById
                    (R.id.chat_project_title_recentchatList);
            conversationLastMessageDate = (TextView) itemView.findViewById(R.id.
                    date_last_message_recieved);
            conversationLastMessageTime = (TextView) itemView.findViewById(R.id.
            conversations_view_time_last_message_recieved);
            conversationRecipientName = (TextView) itemView.findViewById(R.id.
            conversations_view_recipient_name);
        }
    }



    @Override
    public ConversationSessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.recent_messages_list_layout_each_row, parent, false);
        return new ConversationSessionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ConversationSessionViewHolder holder, int position) {
        ChatSession conversation = recentConversationsList.get(position);
        holder.conversationRecipientName.setText(conversation.getRecipientNameForCurrentUser());
        holder.conversationProjectTitle.setText(conversation.getProjectTitle());
        holder.conversationLastMessage.setText(conversation.getLastMessageRecieved());
        holder.conversationLastMessageDate.setText
                (conversation.getLastMessageRecievedTimestampDateFormattedString());
        holder.conversationLastMessageTime.setText
                (conversation.getLastMessageRecievedTimestampTimeFormattedString());
    }

    @Override
    public int getItemCount() {
        return recentConversationsList.size();
    }
}

