package se.liu.tdp021.gravityGame;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class HighscoreDbAdapter {
	private Context context;
	private SQLiteDatabase database;
	private HighscoreDbHelper dbHelper;
	
	private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "highscores";
    
    public static final String KEY_USERNAME = "username";
    public static final String KEY_COLLECTIBLES = "collectibles";
    public static final String KEY_LEVELID = "level_id";
    public static final String KEY_FLIPS = "flips";
    public static final String KEY_USERNAME_ID = "username_id";
    public static final String KEY_HIGHSCORE_ID = "highscore_id";
    
    
    private static final String TABLE_HIGHSCORES = "highscores";
    private static final String HIGHSCORES_TABLE_CREATE =
    		"create table " + TABLE_HIGHSCORES + "(" + KEY_HIGHSCORE_ID + " integer primary key autoincrement, "
    				+ KEY_LEVELID + " text not null, " + KEY_USERNAME_ID + " integer not null, " +
    				KEY_COLLECTIBLES + " integer not null, " + KEY_FLIPS + " integer not null);";
    

    private static final String TABLE_USERNAMES = "usernames";
    private static final String USERNAMES_TABLE_CREATE =
    		"create table " + TABLE_USERNAMES + "(" + KEY_USERNAME_ID + " integer primary key autoincrement, "
    				+ KEY_USERNAME + " text not null);";
    
    
	private static class HighscoreDbHelper extends SQLiteOpenHelper {

	    HighscoreDbHelper(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }

	    @Override
	    public void onCreate(SQLiteDatabase db) {
	        db.execSQL(HIGHSCORES_TABLE_CREATE);
	        db.execSQL(USERNAMES_TABLE_CREATE);
	        Log.w(HighscoreDbHelper.class.getName(), "created tables");
	        
	    }

	    // Method is called during an upgrade of the database, e.g. if you increase
		// the database version
		@Override
		public void onUpgrade(SQLiteDatabase database, int oldVersion,
				int newVersion) {
			Log.w(HighscoreDbHelper.class.getName(),
					"Upgrading database from version " + oldVersion + " to "
							+ newVersion + ", which will destroy all old data");
			database.execSQL("DROP TABLE IF EXISTS " + TABLE_USERNAMES);
			database.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGHSCORES);
			onCreate(database);
		}

	}
	
	public HighscoreDbAdapter(Context context) {
		this.context = context;
	}

	public HighscoreDbAdapter open() throws SQLException {
		dbHelper = new HighscoreDbHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}
	

	public long addHighscore(String level_id, int username_id, int flips, int collectibles) {
		ContentValues initialValues = highscoreContentValues(level_id,
				username_id,flips,collectibles);

		return database.insert("highscores", null, initialValues);
	}
	
	private ContentValues highscoreContentValues(String level_id, int username_id, 
			int flips, int collectibles) {
		
		ContentValues values = new ContentValues();
		values.put(KEY_LEVELID, level_id);
		values.put(KEY_USERNAME_ID, username_id);
		values.put(KEY_FLIPS, flips);
		values.put(KEY_COLLECTIBLES, collectibles);
		return values;
	}
	
	public long addUsername(String username) {
		ContentValues initialValues = usernameContentValues(username);
		return database.insert(TABLE_USERNAMES, null, initialValues);
    }
	
	private ContentValues usernameContentValues(String username) {
		ContentValues values = new ContentValues();
		values.put(KEY_USERNAME, username);
		return values;
	}
	
	public Cursor levelHighscores(String level_id) {
		return database.rawQuery("select h." + KEY_HIGHSCORE_ID + " AS _id,h." + KEY_FLIPS + ",h." + 
				KEY_COLLECTIBLES + ",u.username from usernames u,highscores h where u.username_id = h.username_id AND h.level_id='" + 
				level_id + "' ORDER BY h." + KEY_COLLECTIBLES + " DESC,h." + KEY_FLIPS, null);
	}
	
	public HashMap<String,String> levelHighscore(String level_id) {
		HashMap<String,String> highscore = new HashMap<String,String>();
		Cursor c = levelHighscores(level_id);
		if (c.moveToFirst()) {
			String collectibles = c.getString(c.getColumnIndex(HighscoreDbAdapter.KEY_COLLECTIBLES));
			highscore.put(HighscoreDbAdapter.KEY_COLLECTIBLES, collectibles);
			
			String username = c.getString(c.getColumnIndex(HighscoreDbAdapter.KEY_USERNAME));
			highscore.put(HighscoreDbAdapter.KEY_USERNAME, username);
			
			String key_flips = c.getString(c.getColumnIndex(HighscoreDbAdapter.KEY_FLIPS));
			highscore.put(HighscoreDbAdapter.KEY_FLIPS, key_flips);
		}
		return highscore;
	}


	public int usernameToId(String username) {
		Cursor cursor = database.query(TABLE_USERNAMES,
									new String[] {KEY_USERNAME_ID},
									KEY_USERNAME + "='" + username + "'", 
									null,null, null,null);
		
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			int username_id = cursor.getInt(0);
			cursor.close();
			return username_id;
		}
		else 
			return -1;
	}
	
	public boolean usernameExist(String username) {
		Cursor cursor = database.query(TABLE_USERNAMES,
										new String[] {"1"},
										KEY_USERNAME + "='"  + username + "'", 
										null,null, null,null);
		
		boolean exists = (cursor.getCount() > 0);
		cursor.close();

	    return exists;
    }
}

