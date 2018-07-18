package com.newwebinfotech.rishabh.parkingapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.newwebinfotech.rishabh.parkingapp.model.ChatPeople;
import com.newwebinfotech.rishabh.parkingapp.model.RecentChatList;

import java.util.ArrayList;
import java.util.List;

public class DBOperation {
	String DB_TABLE;
	private int DB_VERSION = 1;
	private static final String DATABASE_NAME = "parkingDB";
	static String[] DATABASE_CREATE;
	private Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBOperation(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}


	public class DatabaseHelper extends SQLiteOpenHelper {

		private static final String TABLE_RECENT_CHAT = "my_recent_chats";
		private static final String TABLE_PEOPLE_CHAT = "my_chats";

		// Common column names for my_recentchatlist
		private static final String RECENT_CHAT_ID = "recent_chat_id";
		private static final String CHAT_PERSON_NAME = "chat_person_name";
		private static final String CHAT_PERSON_PIC = "chat_person_pic";
		private static final String PERSON_LAST_CHAT = "person_last_chat";
		private static final String PERSON_PHONE_NO = "person_phone_no";
		private static final String PERSON_ENTITY_TYPE = "person_entity_type";
		private static final String RECENT_CHAT_READ_UNREAD_STATUS = "recent_chat_status";
		private static final String RECENT_CHAT_TIME = "recent_chat_time";
		private static final String RECENT_CHAT_DELIVER = "recent_chat_deliver";


		// Common column names for chatlist
		private static final String TO_CHAT_ID = "id";
		private static final String TO_CHAT_PERSON_NAME = "person_name";
		private static final String TO_CHAT_PERSON_EMAIL = "person_email";
		private static final String TO_CHAT_PERSON_PHONE_NO = "person_phone_no";
		private static final String TO_CHAT_PERSON_CHAT_MESSAGE = "person_chat_message";
		private static final String TO_CHAT_PERSON_CHAT_TO_FROM = "to_or_from";
		private static final String TO_CHAT_PERSON_CHAT_TYPE = "chat_type";
		private static final String TO_CHAT_PERSON_ENTITY_TYPE = "person_entity";
		private static final String TO_CHAT_MESSAGE_TIME = "person_chat_message_time";
		private static final String TO_CHAT_MESSAGE_TIME_FOR_DELETE = "person_chat_message_time_for_delete";
		private static final String IN_GROUP_SENDER_MESSAGE_NAME="sender_message_name";
		private static final String  TO_CHAT_PERSON_CHAT_TO_DELIVER ="sender_message_deliver";
		private static final String  TO_CHAT_DATE ="to_chat_date";









		// query to create RECENT CHAT table
		private static final String CREATE_TABLE_RECENT_CHAT = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_RECENT_CHAT + "("
				+ RECENT_CHAT_ID + " INTEGER PRIMARY KEY,"
				+ CHAT_PERSON_NAME + " TEXT,"
				+ CHAT_PERSON_PIC + " TEXT,"
				+  PERSON_LAST_CHAT + " TEXT,"
				+  PERSON_PHONE_NO + " TEXT,"
				+ PERSON_ENTITY_TYPE + " TEXT,"
				+ RECENT_CHAT_READ_UNREAD_STATUS + " TEXT,"
				+ RECENT_CHAT_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP,"+ RECENT_CHAT_DELIVER + " TEXT, UNIQUE(" + PERSON_PHONE_NO + ") ON CONFLICT REPLACE)";

		private static final String CREATE_TABLE_PEOPLE_CHAT = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_PEOPLE_CHAT + " (" + TO_CHAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
				+ TO_CHAT_PERSON_NAME + " TEXT NOT NULL, " + TO_CHAT_PERSON_CHAT_MESSAGE
				+ " TEXT, " + TO_CHAT_PERSON_EMAIL + " TEXT, "
				+ TO_CHAT_PERSON_PHONE_NO + " TEXT, " + TO_CHAT_PERSON_CHAT_TO_FROM
				+ " TEXT," + TO_CHAT_PERSON_CHAT_TYPE
				+ " TEXT,"  + TO_CHAT_PERSON_ENTITY_TYPE
				+ " TEXT," + TO_CHAT_MESSAGE_TIME
				+ " TEXT,"  + TO_CHAT_MESSAGE_TIME_FOR_DELETE
				+ " DATETIME DEFAULT CURRENT_TIMESTAMP," + IN_GROUP_SENDER_MESSAGE_NAME
				+ " TEXT," + TO_CHAT_PERSON_CHAT_TO_DELIVER + " TEXT,"+  TO_CHAT_DATE + " TEXT)";


		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DB_VERSION);

		}




		public void onCreate(SQLiteDatabase db) {
			// creating required tables

			db.execSQL(CREATE_TABLE_RECENT_CHAT);
			db.execSQL(CREATE_TABLE_PEOPLE_CHAT);


			try {
				for (String s : DATABASE_CREATE) {
					db.execSQL(s);
				}
			} catch (Exception e) {

			}
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if(oldVersion<newVersion)
			{
				db.execSQL("ALTER TABLE" + TABLE_RECENT_CHAT + "ADD COLUMN" + TO_CHAT_DATE + "TEXT");
			}
		}


		public int deleteSelectedChatsByIds(String time)
		{
			SQLiteDatabase db = this.getWritableDatabase();

			int id = db.delete(TABLE_PEOPLE_CHAT, TO_CHAT_MESSAGE_TIME_FOR_DELETE + "=?",new String[] { time });
			return id;
		}

		public int addPeopleChatListToDB(ChatPeople peopleChat) {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(TO_CHAT_PERSON_NAME,peopleChat.getPERSON_NAME());
			values.put(TO_CHAT_PERSON_CHAT_MESSAGE,peopleChat.getPERSON_CHAT_MESSAGE());
			values.put(TO_CHAT_PERSON_CHAT_TO_FROM,peopleChat.getPERSON_CHAT_TO_FROM());
			values.put(TO_CHAT_PERSON_CHAT_TYPE,peopleChat.getPERSON_CHAT_TYPE());
			values.put(TO_CHAT_PERSON_PHONE_NO,peopleChat.getPERSON_PHONE_NO());
			values.put(TO_CHAT_PERSON_ENTITY_TYPE,peopleChat.getPERSON_ENTITY_TYPE());
			values.put(TO_CHAT_MESSAGE_TIME, peopleChat.getCHAT_MESSAGE_TIME());
			values.put(TO_CHAT_MESSAGE_TIME_FOR_DELETE, peopleChat.getTO_CHAT_MESSAGE_TIME_FOR_DELETE());
			values.put(IN_GROUP_SENDER_MESSAGE_NAME, peopleChat.getGROUP_SENDER_MESSAGE_NAME());
			values.put(TO_CHAT_PERSON_CHAT_TO_DELIVER, peopleChat.getCHAT_MESSAGE_DELIVER());
			values.put(TO_CHAT_DATE,peopleChat.getTO_CHAT_DATE());
			long todo_id = db.insert(TABLE_PEOPLE_CHAT, null, values);
			return (int)todo_id;
		}
		public int addRecentChatListToDB(RecentChatList recentchat) {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(CHAT_PERSON_NAME, recentchat.getCHAT_PERSON_NAME());
			values.put(CHAT_PERSON_PIC, recentchat.getCHAT_PERSON_PIC());
			values.put(PERSON_LAST_CHAT,recentchat.getPERSON_LAST_CHAT());
			values.put(PERSON_PHONE_NO,recentchat.getPERSON_PHONE_NO());
			values.put(PERSON_ENTITY_TYPE,recentchat.getRECENT_PERSON_ENTITY_TYPE());
			values.put(RECENT_CHAT_READ_UNREAD_STATUS, recentchat.getRECENT_CHAT_READ_UNREAD_STATUS());
			values.put(RECENT_CHAT_TIME, recentchat.getRECENT_CHAT_TIME());
			values.put(RECENT_CHAT_DELIVER, recentchat.getDELIVER());

			// insert row
			long todo_id = db.insert(TABLE_RECENT_CHAT, null, values);
			return (int)todo_id;
		}


		public Cursor getPeopleChatDataFromDB(String personPhoneNO) {

			DBHelper.getWritableDatabase();
			SQLiteDatabase db = this.getReadableDatabase();
			ArrayList<ChatPeople> myChatList = new ArrayList<ChatPeople>();
			ChatPeople peopleTable = new ChatPeople();
			String condition = "";
			if (personPhoneNO != null) {
				condition = TO_CHAT_PERSON_PHONE_NO + " = '"
						+ personPhoneNO + "'";
			}
			String[] dbFields = { TO_CHAT_PERSON_NAME,
					TO_CHAT_PERSON_CHAT_MESSAGE,
					TO_CHAT_PERSON_PHONE_NO,
					TO_CHAT_PERSON_CHAT_TO_FROM,
					TO_CHAT_PERSON_CHAT_TYPE,
					TO_CHAT_PERSON_ENTITY_TYPE,
					TO_CHAT_MESSAGE_TIME,
					TO_CHAT_MESSAGE_TIME_FOR_DELETE,
					IN_GROUP_SENDER_MESSAGE_NAME,
					TO_CHAT_PERSON_CHAT_TO_DELIVER,
					TO_CHAT_DATE};
			Cursor cursor = DBHelper.getTableRow(TABLE_PEOPLE_CHAT,
					dbFields, condition, TO_CHAT_PERSON_NAME + " ASC ",
					null);
			DBHelper.close();
			return cursor;

		}

		public List<String> getAllNames()
		{
			List<String> names=new ArrayList<String>();
			try{

				String selectQuery="SELECT "+ CHAT_PERSON_NAME + " FROM " + TABLE_RECENT_CHAT;
				SQLiteDatabase db=DBHelper.getReadableDatabase();
				Cursor cursor=db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst())
				{
					do {
						names.add(cursor.getString(0));
					} while (cursor.moveToNext());
				}
				cursor.close();
				db.close();
			}catch (Exception e) {
			}
			return names;
		}

		public List<String> getAllUserChats()
		{
			List<String> chats=new ArrayList<String>();
			try{
				String selectQuery="SELECT "+ PERSON_LAST_CHAT + " FROM " + TABLE_RECENT_CHAT;
				SQLiteDatabase db=DBHelper.getReadableDatabase();
				Cursor cursor=db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst())
				{
					do {
						chats.add(cursor.getString(0));
					} while (cursor.moveToNext());
				}
				cursor.close();
				db.close();
			}catch (Exception e) {
			}
			return chats;
		}

		public List<String> getAllUserPics()
		{
			List<String> chats=new ArrayList<String>();
			try{
				String selectQuery="SELECT "+ CHAT_PERSON_PIC + " FROM " + TABLE_RECENT_CHAT;
				SQLiteDatabase db=DBHelper.getReadableDatabase();
				Cursor cursor=db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst())
				{
					do {
						chats.add(cursor.getString(0));
					} while (cursor.moveToNext());
				}
				cursor.close();
				db.close();
			}catch (Exception e) {
			}
			return chats;
		}

		public List<String> getAllUserPhoneNos()
		{
			List<String> chats=new ArrayList<String>();
			try{
				String selectQuery="SELECT "+ PERSON_PHONE_NO + " FROM " + TABLE_RECENT_CHAT;
				SQLiteDatabase db=DBHelper.getReadableDatabase();
				Cursor cursor=db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst())
				{
					do {
						chats.add(cursor.getString(0));
					} while (cursor.moveToNext());
				}
				cursor.close();
				db.close();
			}catch (Exception e) {
			}
			return chats;
		}

		public List<String> getAllUserEntityTypes()
		{
			List<String> chats=new ArrayList<String>();
			try{
				String selectQuery="SELECT "+ PERSON_ENTITY_TYPE + " FROM " + TABLE_RECENT_CHAT;
				SQLiteDatabase db=DBHelper.getReadableDatabase();
				Cursor cursor=db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst())
				{
					do {
						chats.add(cursor.getString(0));
					} while (cursor.moveToNext());
				}
				cursor.close();
				db.close();
			}catch (Exception e) {
			}
			return chats;
		}
		public List<String> getAllUserReadUnreadStatus()
		{
			List<String> chats=new ArrayList<String>();
			try{
				String selectQuery="SELECT "+ RECENT_CHAT_READ_UNREAD_STATUS + " FROM " + TABLE_RECENT_CHAT;
				SQLiteDatabase db=DBHelper.getReadableDatabase();
				Cursor cursor=db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst())
				{
					do {
						chats.add(cursor.getString(0));
					} while (cursor.moveToNext());
				}
				cursor.close();
				db.close();
			}catch (Exception e) {
			}
			return chats;
		}
		public List<String> getAllUserLastChatTime()
		{
			List<String> chats=new ArrayList<String>();
			try{
				String selectQuery="SELECT "+ RECENT_CHAT_TIME + " FROM " + TABLE_RECENT_CHAT;
				SQLiteDatabase db=DBHelper.getReadableDatabase();
				Cursor cursor=db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst())
				{
					do {
						chats.add(cursor.getString(0));
					} while (cursor.moveToNext());
				}
				cursor.close();
				db.close();
			}catch (Exception e) {
			}
			return chats;
		}

		public List<String> getAllUserDeliveryStatus()
		{
			List<String> chats=new ArrayList<String>();
			try{
				String selectQuery="SELECT "+ RECENT_CHAT_DELIVER + " FROM " + TABLE_RECENT_CHAT;
				SQLiteDatabase db=DBHelper.getReadableDatabase();
				Cursor cursor=db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst())
				{
					do {
						chats.add(cursor.getString(0));
					} while (cursor.moveToNext());
				}
				cursor.close();
				db.close();
			}catch (Exception e) {
			}
			return chats;
		}
		public int deleteChatsByPhone(String phoneNo){
			SQLiteDatabase db = this.getWritableDatabase();
			String[] phone = {phoneNo.toString()};
			int id = db.delete(TABLE_PEOPLE_CHAT, TO_CHAT_PERSON_PHONE_NO + "=?",phone);
			return id;
		}
		/***
		 * DELETE ALL RECORDS FROM RECENTCHATS.
		 */
		public int deleteFromRecentChatListByPhone(String phoneNo){
			SQLiteDatabase db = this.getWritableDatabase();
			String[] phone = {phoneNo.toString()};
			//db.execSQL("delete from " + TABLE_RECENT_CHAT + " WHERE " + PERSON_PHONE_NO + " = " + phoneNo);
			int id = db.delete(TABLE_RECENT_CHAT, PERSON_PHONE_NO + "=?",phone);
			return id;
		}

		public void deleteChatFromDB(){
			SQLiteDatabase db = this.getWritableDatabase();

			db.execSQL("delete from " + TABLE_PEOPLE_CHAT);

		}

		public void deleteRecentChatFromDB(){
			SQLiteDatabase db = this.getWritableDatabase();

			db.execSQL("delete from " + TABLE_RECENT_CHAT);

		}

		public String getPeopleChatlastTimeDataFromDB(String personPhoneNO)
		{
			String chat=null;
			Cursor cursor = null;
			String[] d = {personPhoneNO.toString()};
			try{
				String selectQuery="SELECT "+ TO_CHAT_MESSAGE_TIME_FOR_DELETE +" FROM "+ TABLE_PEOPLE_CHAT + " WHERE " + TO_CHAT_PERSON_PHONE_NO + " =?  ORDER BY " + TO_CHAT_MESSAGE_TIME_FOR_DELETE + " DESC LIMIT 1";

				SQLiteDatabase db = DBHelper.getReadableDatabase();


				cursor=db.rawQuery(selectQuery, d);


				if (cursor!=null && cursor.getCount()>0)
				{
					cursor.moveToFirst();
					chat= cursor.getString(cursor.getColumnIndex("person_chat_message_time_for_delete"));
				}
				cursor.close();

				db.close();

			}finally {


			}
			return chat;

		}
		public String getPeopleChatlastDataFromDB(String personPhoneNO) {
			String chat=null;
			Cursor cursor = null;
			String[] d = {personPhoneNO.toString()};
			try{
				String selectQuery="SELECT "+ TO_CHAT_PERSON_CHAT_MESSAGE +" FROM "+ TABLE_PEOPLE_CHAT + " WHERE " + TO_CHAT_PERSON_PHONE_NO + " =?  ORDER BY " + TO_CHAT_MESSAGE_TIME_FOR_DELETE + " DESC LIMIT 1";

				SQLiteDatabase db = DBHelper.getReadableDatabase();


				cursor=db.rawQuery(selectQuery, d);


				if (cursor!=null && cursor.getCount()>0)
				{
					cursor.moveToFirst();
					chat= cursor.getString(cursor.getColumnIndex("person_chat_message"));
				}
				cursor.close();

				db.close();

			}finally {


			}
			return chat;

		}

		public List<RecentChatList> getRefreshedRecentChatListByTimestamp()
		{
			List<RecentChatList> recents = new ArrayList<RecentChatList>();
			SQLiteDatabase db=DBHelper.getReadableDatabase();
			try{
				Cursor cursor = db.query(TABLE_RECENT_CHAT, null, null, null, null, null, RECENT_CHAT_TIME +" DESC");
				int a = cursor.getCount();
				if (cursor.getCount()>0)
				{
					cursor.moveToFirst();
					do {
						recents.add(new RecentChatList(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8)));
					} while (cursor.moveToNext());
				}
				cursor.close();
				db.close();
			}catch (Exception e) {
				Log.i("DBOPERATION", e.getMessage());
			}
			return recents;
		}



		public String getPeopleChatlastTimeFromDB(String personPhoneNO) {
			String chat=null;
			Cursor cursor = null;
			String[] d = {personPhoneNO.toString()};
			try{
				String selectQuery="SELECT "+ TO_CHAT_MESSAGE_TIME_FOR_DELETE +" FROM "+ TABLE_PEOPLE_CHAT + " WHERE " + TO_CHAT_PERSON_PHONE_NO + " =?  ORDER BY " + TO_CHAT_MESSAGE_TIME_FOR_DELETE + " DESC LIMIT 1";

				SQLiteDatabase db = DBHelper.getReadableDatabase();

				cursor=db.rawQuery(selectQuery, d);


				if (cursor!=null && cursor.getCount()>0)
				{
					cursor.moveToFirst();
					chat= cursor.getString(cursor.getColumnIndex("person_chat_message_time_for_delete"));
				}
				cursor.close();

				db.close();

			}finally {


			}
			return chat;

		}

		public String getPeopleChatlastDataToFromStatus(String personPhoneNO) {
			String chat=null;
			Cursor cursor = null;
			String[] d = {personPhoneNO.toString()};
			try{
				String selectQuery="SELECT "+ TO_CHAT_PERSON_CHAT_TO_FROM +" FROM "+ TABLE_PEOPLE_CHAT + " WHERE " + TO_CHAT_PERSON_PHONE_NO + " =?  ORDER BY " + TO_CHAT_MESSAGE_TIME_FOR_DELETE + " DESC LIMIT 1";

				SQLiteDatabase db = DBHelper.getReadableDatabase();


				cursor=db.rawQuery(selectQuery, d);


				if (cursor!=null && cursor.getCount()>0)
				{
					cursor.moveToFirst();
					chat= cursor.getString(cursor.getColumnIndex("to_or_from"));
				}
				cursor.close();

				db.close();

			}finally {


			}
			return chat;
		}

		public long updateRecentChatItemChatMessageByPhone(String phone,String chat){
			SQLiteDatabase db=DBHelper.getWritableDatabase();
			String[] phonearr = {phone.toString()};

			ContentValues values = new ContentValues();
			values.put(PERSON_LAST_CHAT,chat);

			return db.update(TABLE_RECENT_CHAT, values,PERSON_PHONE_NO  + "=?",phonearr);
		}

		public long updateRecentChatItemChatTimeByPhone(String phone,String chat){
			SQLiteDatabase db=DBHelper.getWritableDatabase();
			String[] phonearr = {phone.toString()};

			ContentValues values = new ContentValues();
			values.put(RECENT_CHAT_TIME,chat);

			return db.update(TABLE_RECENT_CHAT, values,PERSON_PHONE_NO  + "=?",phonearr);
		}


		public Cursor getTableRow(String tablename, String[] dbFields,
								  String condition, String order, String limit) throws SQLException {
			DB_TABLE = tablename;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor mCursor = db.query(false, DB_TABLE, dbFields, condition, null,
					null, null, order, limit);

			if (mCursor != null) {
				mCursor.moveToFirst();
			}
			return mCursor;
		}


	}

}// END OF DBOperation Class.
