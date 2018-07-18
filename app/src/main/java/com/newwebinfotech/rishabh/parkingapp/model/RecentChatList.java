package com.newwebinfotech.rishabh.parkingapp.model;

public class RecentChatList {
	int DATABASE_VERSION;
	private String TABLE_NAME;
	private long ID;
	private String CHAT_PERSON_NAME;
	private String CHAT_PERSON_PIC;
	private String PERSON_LAST_CHAT;
	private String PERSON_PHONE_NO;
	private String RECENT_PERSON_ENTITY_TYPE;
	private String RECENT_CHAT_READ_UNREAD_STATUS;
	private String RECENT_CHAT_TIME;
	private String DELIVER_STATUS;



	public RecentChatList() {
		super();
	}







	public RecentChatList(String cHAT_PERSON_NAME, String cHAT_PERSON_PIC,
						  String pERSON_LAST_CHAT, String pERSON_PHONE_NO,
						  String rECENT_PERSON_ENTITY_TYPE,
						  String rECENT_CHAT_READ_UNREAD_STATUS, String rECENT_CHAT_TIME, String rDELIVER_STATUS) {
		super();
		CHAT_PERSON_NAME = cHAT_PERSON_NAME;
		CHAT_PERSON_PIC = cHAT_PERSON_PIC;
		PERSON_LAST_CHAT = pERSON_LAST_CHAT;
		PERSON_PHONE_NO = pERSON_PHONE_NO;
		RECENT_PERSON_ENTITY_TYPE = rECENT_PERSON_ENTITY_TYPE;
		RECENT_CHAT_READ_UNREAD_STATUS = rECENT_CHAT_READ_UNREAD_STATUS;
		RECENT_CHAT_TIME = rECENT_CHAT_TIME;
		DELIVER_STATUS=rDELIVER_STATUS;
	}
	public String getRECENT_CHAT_READ_UNREAD_STATUS() {
		return RECENT_CHAT_READ_UNREAD_STATUS;
	}
	public void setRECENT_CHAT_READ_UNREAD_STATUS(
			String rECENT_CHAT_READ_UNREAD_STATUS) {
		RECENT_CHAT_READ_UNREAD_STATUS = rECENT_CHAT_READ_UNREAD_STATUS;
	}
	public String getRECENT_CHAT_TIME() {
		return RECENT_CHAT_TIME;
	}
	public void setRECENT_CHAT_TIME(String rECENT_CHAT_TIME) {
		RECENT_CHAT_TIME = rECENT_CHAT_TIME;
	}


	public String getDELIVER() {
		return DELIVER_STATUS;
	}
	public void setDELIVER(String rDELIVER_STATUS) {
		DELIVER_STATUS = rDELIVER_STATUS;
	}


	public int getDATABASE_VERSION() {
		return DATABASE_VERSION;
	}
	public void setDATABASE_VERSION(int dATABASE_VERSION) {
		DATABASE_VERSION = dATABASE_VERSION;
	}
	public String getTABLE_NAME() {
		return TABLE_NAME;
	}
	public void setTABLE_NAME(String tABLE_NAME) {
		TABLE_NAME = tABLE_NAME;
	}
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public String getCHAT_PERSON_NAME() {
		return CHAT_PERSON_NAME;
	}
	public void setCHAT_PERSON_NAME(String cHAT_PERSON_NAME) {
		CHAT_PERSON_NAME = cHAT_PERSON_NAME;
	}
	public String getCHAT_PERSON_PIC() {
		return CHAT_PERSON_PIC;
	}
	public void setCHAT_PERSON_PIC(String cHAT_PERSON_PIC) {
		CHAT_PERSON_PIC = cHAT_PERSON_PIC;
	}
	public String getPERSON_LAST_CHAT() {
		return PERSON_LAST_CHAT;
	}
	public void setPERSON_LAST_CHAT(String pERSON_LAST_CHAT) {
		PERSON_LAST_CHAT = pERSON_LAST_CHAT;
	}
	public String getPERSON_PHONE_NO() {
		return PERSON_PHONE_NO;
	}
	public void setPERSON_PHONE_NO(String pERSON_PHONE_NO) {
		PERSON_PHONE_NO = pERSON_PHONE_NO;
	}
	public String getRECENT_PERSON_ENTITY_TYPE() {
		return RECENT_PERSON_ENTITY_TYPE;
	}
	public void setRECENT_PERSON_ENTITY_TYPE(String rECENT_PERSON_ENTITY_TYPE) {
		RECENT_PERSON_ENTITY_TYPE = rECENT_PERSON_ENTITY_TYPE;
	}





}
