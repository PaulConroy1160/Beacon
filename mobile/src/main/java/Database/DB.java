package Database;

/**
 * Created by paulconroy on 13/01/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.paulconroy.testwatchtophone.Model.Message;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by paulconroy on 23/12/2015.
 */
public class DB extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 6;

    // Database Name
    private static final String DATABASE_NAME = "beaconDB";

    // Stop Saves table name
    private static final String TABLE_MESSAGES = "chatMessages";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TO = "receiver";
    private static final String KEY_FROM = "sender";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_TIME = "time";

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MESSAGES_TABLE = "CREATE TABLE " + TABLE_MESSAGES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TO + " TEXT,"
                + KEY_FROM + " TEXT," + KEY_MESSAGE + " TEXT," + KEY_TIME + " TEXT" + ")";
        db.execSQL(CREATE_MESSAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);

        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    public void addMessage(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TO, message.getTo());
        values.put(KEY_FROM, message.getFrom());
        values.put(KEY_MESSAGE, message.getMessage());
        values.put(KEY_TIME, message.getTime());

        // Inserting Row
        db.insert(TABLE_MESSAGES, null, values);
        db.close(); // Closing database connection
    }


    // Getting All Contacts
    public List<Message> getAllMessages() {
        List<Message> messageList = new ArrayList<Message>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MESSAGES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Message message = new Message();
                message.setId(Integer.parseInt(cursor.getString(0)));
                message.setTo(cursor.getString(1));
                message.setFrom(cursor.getString(2));
                message.setMessage(cursor.getString(3));
                message.setTime(cursor.getString(4));
                // Adding contact to list
                messageList.add(message);
            } while (cursor.moveToNext());
        }

        // return contact list
        return messageList;
    }


//    // Deleting single contact
//    public void deleteSave(StopSave save) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_SAVES, KEY_ID + " = ?",
//                new String[] { String.valueOf(save.getId()) });
//        db.close();
//    }
}

