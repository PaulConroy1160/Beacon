package com.example.paulconroy.testwatchtophone;

/**
 * Created by paulconroy on 13/01/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paulconroy.testwatchtophone.Model.Connection;
import com.example.paulconroy.testwatchtophone.Model.Message;
import com.example.paulconroy.testwatchtophone.Model.Model;
import com.example.paulconroy.testwatchtophone.Model.User;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Database.DB;

/**
 * Created by paulconroy on 17/12/2015.
 */
public class ConnectionListAdapter extends ArrayAdapter<Connection> {

    private ParseObject connection;
    private Typeface typeFace;
    private DB db;
    private Context ctx;
    private Model mModel;
    private String usernameTarget;
    private ParseUser user;


    public ConnectionListAdapter(Context context, int resource,
                                 List<Connection> connections) {
        super(context, resource, connections);
        ctx = context;
        mModel = Model.getInstance();
        user = ParseUser.getCurrentUser();
        typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/muli.ttf");

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(
                    R.layout.connection_item, parent, false);
        }


        final Connection connection = getItem(position);
        if (connection != null) {
            ImageView userPicLabel = (ImageView) convertView
                    .findViewById(R.id.userPicLabel);
            TextView messageUserLabel = (TextView) convertView
                    .findViewById(R.id.messageUserlabel);
            TextView messageLabel = (TextView) convertView
                    .findViewById(R.id.messagelabel);
            final ImageButton deleteConnectionBTN = (ImageButton) convertView.findViewById(R.id.connDeleteBTN);

            deleteConnectionBTN.setVisibility(View.INVISIBLE);

            getLastMessage(connection.getUserName());
            Bitmap alteredPic = getRoundedCornerBitmap(connection.getPic());
            userPicLabel.setImageBitmap(alteredPic);
            messageUserLabel.setText(connection.getUserName());
            messageUserLabel.setTypeface(typeFace);
            messageLabel.setText(getLastMessage(connection.getUserName()));
            messageLabel.setTypeface(typeFace);

            deleteConnectionBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeConnection(connection);
                    isEnabled(position);
                    Log.d("connection", "removed");

                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mModel.setTargetConnection(connection.getUserName());
                    startChat();
                }
            });


            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d("log click on user", connection.getUserName());
                    showDeleteConn(deleteConnectionBTN);

                    //indicates no further processing needed.
                    return true;
                }
            });




//            senderLabel.setText(message.getFrom());


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

    public String getLastMessage(String username) {
        db = new DB(ctx);

        List<Message> messageList = db.getAllMessages();
        mModel.setTargetConnection(username);
        messageList = mModel.sortMessage(messageList, "");
        String lastMessage = "";
        if (messageList.size() != 0) {
            lastMessage = messageList.get(messageList.size() - 1).getMessage().toString();
        } else {
            lastMessage = "Start a conversation!";
        }

        return lastMessage;
    }

    public void showDeleteConn(final ImageButton btn) {
        btn.setVisibility(View.VISIBLE);

        Animation anim = AnimationUtils.loadAnimation(ctx, R.anim.fade_in);
        btn.setAnimation(anim);
        anim.start();

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        btn.clearAnimation();
                        Animation anim = AnimationUtils.loadAnimation(ctx, R.anim.fade_out);
                        btn.setAnimation(anim);
                        anim.start();
                        btn.setVisibility(View.INVISIBLE);
                    }
                }, 2000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    public void removeConnection(Connection conn) {
        deleteConnectionFromParse(conn);

    }

    public void removeDeleteConn(final ImageButton btn) {


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // this code will be executed after 2 seconds
                Animation anim = AnimationUtils.loadAnimation(ctx, R.anim.fade_out);
                btn.setAnimation(anim);
                anim.start();
                btn.setVisibility(View.INVISIBLE);
            }
        }, 3000);

    }

    public void deleteConnectionFromParse(final Connection conn) {
        Log.d("inside", "setUserInformation");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Connections");
        //need to change to user
        query.whereEqualTo("user1", user.getUsername());
        query.whereEqualTo("user2", conn.getUserName());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (!objects.isEmpty()) {
                        objects.get(0).deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                mModel.removeConnection(conn);
                                mModel.setOnConnectionListChange(true);
                            }
                        });
                    } else {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Connections");
                        //need to change to user
                        query.whereEqualTo("user2", user.getUsername());
                        query.whereEqualTo("user1", conn.getUserName());
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null) {
                                    if (!objects.isEmpty()) {
                                        objects.get(0).deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                mModel.removeConnection(conn);
                                                mModel.setOnConnectionListChange(true);
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }


    public void startChat() {
        ConnectionListActivity connActivity = (ConnectionListActivity) ctx;
        Intent i = new Intent(ctx, ChatActivity.class);
        ctx.startActivity(i);
        connActivity.overridePendingTransition(R.anim.open_trans, R.anim.close_trans);
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}

