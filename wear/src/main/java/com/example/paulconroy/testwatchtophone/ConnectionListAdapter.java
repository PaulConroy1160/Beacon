package com.example.paulconroy.testwatchtophone;

/**
 * Created by paulconroy on 13/01/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

/**
 * Created by paulconroy on 17/12/2015.
 */
public class ConnectionListAdapter extends ArrayAdapter<Connection> {


    private Typeface typeFace;

    public ConnectionListAdapter(Context context, int resource,
                                 List<Connection> connections) {
        super(context, resource, connections);
        typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/muli.ttf");

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(
                    R.layout.connection_item, parent, false);
        }


        Connection connection = getItem(position);
        if (connection != null) {
            TextView messageLabel = (TextView) convertView
                    .findViewById(R.id.messagelabel);

            //Bitmap alteredPic = getRoundedCornerBitmap(connection.getPic());
            //userPicLabel.setImageBitmap(alteredPic);
            messageLabel.setText(connection.getUsername().toUpperCase());
            messageLabel.setTypeface(typeFace);
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
}
