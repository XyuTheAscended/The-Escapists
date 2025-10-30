package com.model;

public abstract class DataConstants {
	protected static final String USER_FILE_NAME = "src/main/java/data/users.json";
	protected static final String USER_FILE_NAME_JSON = "/data/users.json";
	protected static final String USER_ID = "id";
	protected static final String USER_USER_NAME = "userName";
	protected static final String USER_FIRST_NAME = "firstName";
	protected static final String USER_LAST_NAME = "lastName";
	protected static final String USER_AGE = "age";
	protected static final String USER_PHONE_NUMBER = "phoneNumber";

	protected static final String BOOK_FILE_NAME = "src/main/java/data/books.json";
	protected static final String BOOK_FILE_NAME_JSON = "/data/books.json";
	protected static final String BOOK_ID = "id";
	protected static final String BOOK_TITLE = "title";
	protected static final String BOOK_YEAR = "year";
	protected static final String BOOK_GENRE = "genre";
	protected static final String BOOK_ISBN = "ISBN";
	protected static final String BOOK_PUBLISHER = "publisher";
	protected static final String BOOK_AUTHOR = "author";
	protected static final String BOOK_NUM_COPIES = "numCopies";
	protected static final String BOOK_NEW_ARRIVAL = "newArrival";
	protected static final String BOOK_IMG = "img";
	protected static final String BOOK_LOANS = "loans";

	protected static final String LOAN_USER_ID = "id";
	protected static final String LOAN_DUE = "due";
	protected static final String LOAN_RENEW_COUNT = "renew_count";

	public static boolean isJUnitTest() {  
		for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
		  if (element.getClassName().startsWith("org.junit.")) {
			return true;
		  }           
		}
		return false;
	  }
}