package com.newwebinfotech.rishabh.parkingapp.model;
public class ChatPeople {


	private String TABLE_PEOPLE;
	int DATABASE_VERSION ;
	private int ID;
	private String PERSON_NAME;
	private String PERSON_EMAIL;
	private String PERSON_PHONE_NO;
	private String PERSON_CHAT_MESSAGE;
	private String PERSON_CHAT_TO_FROM;
	private String PERSON_CHAT_TYPE;
	private String PERSON_ENTITY_TYPE;
	private String CHAT_MESSAGE_TIME;
	private String TO_CHAT_MESSAGE_TIME_FOR_DELETE;
	private String GROUP_SENDER_MESSAGE_NAME;
	private String TO_CHAT_PERSON_CHAT_TO_DELIVER;
	private String TO_CHAT_DATE;


	
	public String getTO_CHAT_MESSAGE_TIME_FOR_DELETE() {
		return TO_CHAT_MESSAGE_TIME_FOR_DELETE;
	}

	public void setTO_CHAT_MESSAGE_TIME_FOR_DELETE(
			String tO_CHAT_MESSAGE_TIME_FOR_DELETE) {
		TO_CHAT_MESSAGE_TIME_FOR_DELETE = tO_CHAT_MESSAGE_TIME_FOR_DELETE;
	}

	public String getGROUP_SENDER_MESSAGE_NAME() {
		return GROUP_SENDER_MESSAGE_NAME;
	}

	public void setGROUP_SENDER_MESSAGE_NAME(String gROUP_SENDER_MESSAGE_NAME) {
		GROUP_SENDER_MESSAGE_NAME = gROUP_SENDER_MESSAGE_NAME;
	}

	public String getCHAT_MESSAGE_TIME() {
		return CHAT_MESSAGE_TIME;
	}

	public void setCHAT_MESSAGE_TIME(String cHAT_MESSAGE_TIME) {
		CHAT_MESSAGE_TIME = cHAT_MESSAGE_TIME;
	}

	public String getCHAT_MESSAGE_DELIVER() {
		return TO_CHAT_PERSON_CHAT_TO_DELIVER;
	}

	public void setCHAT_MESSAGE_DELIVER(String tO_CHAT_PERSON_CHAT_TO_DELIVER) {
		TO_CHAT_PERSON_CHAT_TO_DELIVER = tO_CHAT_PERSON_CHAT_TO_DELIVER;
	}

	public String getPERSON_CHAT_TYPE() {
		return PERSON_CHAT_TYPE;
	}

	public void setPERSON_CHAT_TYPE(String pERSON_CHAT_TYPE) {
		PERSON_CHAT_TYPE = pERSON_CHAT_TYPE;
	}



	public String getPERSON_CHAT_TO_FROM() {
		return PERSON_CHAT_TO_FROM;
	}

	public void setPERSON_CHAT_TO_FROM(String pERSON_CHAT_TO_FROM) {
		PERSON_CHAT_TO_FROM = pERSON_CHAT_TO_FROM;
	}

	public String getPERSON_ENTITY_TYPE() {
		return PERSON_ENTITY_TYPE;
	}

	public void setPERSON_ENTITY_TYPE(String pERSON_ENTITY_TYPE) {
		PERSON_ENTITY_TYPE = pERSON_ENTITY_TYPE;
	}

	public String getPERSON_CHAT_MESSAGE() {
		return PERSON_CHAT_MESSAGE;
	}

	public void setPERSON_CHAT_MESSAGE(String pERSON_CHAT_MESSAGE) {
		PERSON_CHAT_MESSAGE = pERSON_CHAT_MESSAGE;
	}

	public String getTABLE_PEOPLE() {
		return TABLE_PEOPLE;
	}

	public void setTABLE_PEOPLE(String tABLE_PEOPLE) {
		TABLE_PEOPLE = tABLE_PEOPLE;
	}

	public int getDATABASE_VERSION() {
		return DATABASE_VERSION;
	}

	public void setDATABASE_VERSION(int dATABASE_VERSION) {
		DATABASE_VERSION = dATABASE_VERSION;
	}

	public String getPERSON_NAME() {
		return PERSON_NAME;
	}

	public void setPERSON_NAME(String pERSON_NAME) {
		PERSON_NAME = pERSON_NAME;
	}

	public String getPERSON_EMAIL() {
		return PERSON_EMAIL;
	}

	public void setPERSON_EMAIL(String pERSON_EMAIL) {
		PERSON_EMAIL = pERSON_EMAIL;
	}

	//	public String getPERSON_DEVICE_ID() {
	//		return PERSON_DEVICE_ID;
	//	}
	//
	//	public void setPERSON_DEVICE_ID(String pERSON_DEVICE_ID) {
	//		PERSON_DEVICE_ID = pERSON_DEVICE_ID;
	//	}

	public String getPERSON_PHONE_NO() {
		return PERSON_PHONE_NO;
	}

	public void setPERSON_PHONE_NO(String pERSON_PHONE_NO) {
		PERSON_PHONE_NO = pERSON_PHONE_NO;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getTableName() {
		return TABLE_PEOPLE;
	}

	public int getDatabaseVersion() {
		return DATABASE_VERSION;
	}

	public int getID() {
		return ID;
	}

	public String getFilename() {
		return PERSON_NAME;
	}

	public String getTO_CHAT_DATE() {
		return TO_CHAT_DATE;
	}

	public void setTO_CHAT_DATE(String TO_CHAT_DATE) {
		this.TO_CHAT_DATE = TO_CHAT_DATE;
	}

	//	public String getDatabaseCreateQuery() {
	//		final String DATABASE_CREATE = "create table IF NOT EXISTS "
	//				+ TABLE_PEOPLE + " (" + ID + " INTEGER PRIMARY KEY, "
	//				+ PERSON_NAME + " TEXT NOT NULL, " + PERSON_CHAT_MESSAGE
	//				+ " TEXT NOT NULL, " + PERSON_EMAIL + " TEXT NOT NULL, "
	//				+ PERSON_PHONE_NO + " TEXT NOT NULL, " + PERSON_CHAT_TO_FROM
	//				+ " TEXT NOT NULL ," + PERSON_CHAT_TYPE
	//				+ " TEXT NOT NULL ,"  + PERSON_ENTITY_TYPE
	//				+ " TEXT NOT NULL)";
	//		System.out.println(DATABASE_CREATE);
	//		return DATABASE_CREATE;
	//	}
}