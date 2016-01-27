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

import com.example.paulconroy.testwatchtophone.Model.Connection;
import com.example.paulconroy.testwatchtophone.Model.Message;
import com.example.paulconroy.testwatchtophone.Model.User;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by paulconroy on 17/12/2015.
 */
public class ConnectionListAdapter extends ArrayAdapter<Connection> {

    private ParseObject connection;

    public ConnectionListAdapter(Context context, int resource,
                                 List<Connection> connections) {
        super(context, resource, connections);

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(
                    R.layout.connection_item, parent, false);
        }

        Connection connection = getItem(position);
        if (connection != null) {
            TextView userNameLabel = (TextView) convertView
                    .findViewById(R.id.userNameLabel);
            TextView messageLabel = (TextView) convertView
                    .findViewById(R.id.messagelabel);

            userNameLabel.setText(connection.getUserName());
            messageLabel.setText("START A CONVERSATION!");
//            senderLabel.setText(message.getFrom());


        }

        return convertView;
    }
}
