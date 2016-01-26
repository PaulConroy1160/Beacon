package com.example.paulconroy.testwatchtophone;

/**
 * Created by paulconroy on 13/01/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


import com.example.paulconroy.testwatchtophone.Model.Message;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by paulconroy on 17/12/2015.
 */
public class ChatListAdapter extends ArrayAdapter<Message> {

    private ParseUser user;

    public ChatListAdapter(Context context, int resource,
                           List<Message> objects) {
        super(context, resource, objects);
        user = ParseUser.getCurrentUser();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(
                    R.layout.chat_message_item, parent, false);
        }

        Message message = getItem(position);
        if (message != null) {
            TextView receiverLabel = (TextView) convertView
                    .findViewById(R.id.receiverLabel);
            TextView messageLabel = (TextView) convertView
                    .findViewById(R.id.messagelabel);
            TextView senderLabel = (TextView) convertView
                    .findViewById(R.id.senderLabel);

            messageLabel.setText(message.getMessage());
            senderLabel.setText(message.getFrom());

            if (message.getTo().equalsIgnoreCase(user.getUsername()))

            {
                receiverLabel.setText(message.getTo());

            } else {

                receiverLabel.setText("");
                receiverLabel.setVisibility(View.GONE);
            }
        }

        return convertView;
    }
}
