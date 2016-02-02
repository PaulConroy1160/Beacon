package com.example.paulconroy.testwatchtophone;

/**
 * Created by paulconroy on 13/01/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


import com.example.paulconroy.testwatchtophone.Model.Message;
import com.example.paulconroy.testwatchtophone.Model.Model;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by paulconroy on 17/12/2015.
 */
public class ChatListAdapter extends ArrayAdapter<Message> {

    private ParseUser user;
    private Model mModel;

    public ChatListAdapter(Context context, int resource,
                           List<Message> objects) {
        super(context, resource, objects);
        user = ParseUser.getCurrentUser();
        mModel = Model.getInstance();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(
                    R.layout.chat_message_item, parent, false);
        }

        Message message = getItem(position);
        if (message != null) {
            ImageView receiverLabel = (ImageView) convertView
                    .findViewById(R.id.receiverLabel);
            TextView messageLabel = (TextView) convertView
                    .findViewById(R.id.messagelabel);
            ImageView senderLabel = (ImageView) convertView
                    .findViewById(R.id.senderLabel);

            messageLabel.setText(message.getMessage());

            if (message.getTo().equalsIgnoreCase(user.getUsername()))

            {
                receiverLabel.setVisibility(View.VISIBLE);
                receiverLabel.setImageBitmap(mModel.getTargetPlayerProfilePic());
                senderLabel.setVisibility(View.GONE);

            } else {
                senderLabel.setVisibility(View.VISIBLE);
                senderLabel.setImageBitmap(mModel.getUserProfile());
                receiverLabel.setVisibility(View.GONE);
                //receiverLabel.setVisibility(View.GONE);
            }
        }

        return convertView;
    }
}
