package DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.paulconroy.testwatchtophone.Connection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulconroy on 14/02/2016.
 */
public class DB extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "beaconDB";

    // Stop Saves table name
    private static final String TABLE_CONNECTIONS = "chatConnections";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";


    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MESSAGES_TABLE = "CREATE TABLE " + TABLE_CONNECTIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USERNAME + " TEXT"
                + ")";
        db.execSQL(CREATE_MESSAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONNECTIONS);

        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    public void addConnection(Connection connection) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, connection.getUsername());


        // Inserting Row
        db.insert(TABLE_CONNECTIONS, null, values);
        db.close(); // Closing database connection
    }


    // Getting All Contacts
    public List<Connection> getAllConnections() {
        List<Connection> connectionList = new ArrayList<Connection>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONNECTIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Connection connection = new Connection();
                connection.setId(Integer.parseInt(cursor.getString(0)));
                connection.setUsername(cursor.getString(1));

                // Adding contact to list
                connectionList.add(connection);
            } while (cursor.moveToNext());
        }

        // return contact list
        return connectionList;
    }

    public void removeAllConnections() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONNECTIONS, null, null);
        db.close();
    }
}
