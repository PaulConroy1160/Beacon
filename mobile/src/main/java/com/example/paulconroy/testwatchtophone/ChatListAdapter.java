package com.example.paulconroy.testwatchtophone;

/**
 * Created by paulconroy on 13/01/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    private Bitmap profilePic;
    private RelativeLayout messageBorder;

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

        messageBorder = (RelativeLayout) convertView.findViewById(R.id.messageBorder);
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
                profilePic = mModel.getTargetPlayerProfilePic();
                if (profilePic != null) {
                    profilePic = getRoundedCornerBitmap(profilePic);
                    receiverLabel.setImageBitmap(profilePic);
                    senderLabel.setVisibility(View.GONE);
                    messageBorder.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                    messageLabel.setBackgroundColor(Color.parseColor("#1abc9c"));
                    messageLabel.setTextColor(Color.parseColor("#ffffff"));
                }

            } else {
                senderLabel.setVisibility(View.VISIBLE);
                profilePic = mModel.getUserProfile();
                if (profilePic != null) {
                    profilePic = getRoundedCornerBitmap(profilePic);
                    senderLabel.setImageBitmap(profilePic);
                    receiverLabel.setVisibility(View.GONE);
                    messageBorder.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                    messageLabel.setBackgroundColor(Color.parseColor("#E08283"));
                    messageLabel.setTextColor(Color.parseColor("#ffffff"));
                }
                //receiverLabel.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
