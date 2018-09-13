package com.example.android.inventoryapp.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper for books app. Manages database creation and version management.
 */
public class BooksDBHelper extends SQLiteOpenHelper {

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "inventory.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link BooksDBHelper}.
     *
     * @param context of the app
     */
    public BooksDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a String that contains the SQL statement to create the books table
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + BooksContract.BookEntry.TABLE_NAME + " ("
                + BooksContract.BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BooksContract.BookEntry.COLUMN_BOOK_NAME + " TEXT NOT NULL, "
                + BooksContract.BookEntry.COLUMN_BOOK_AUTHOR + " TEXT NOT NULL, "
                + BooksContract.BookEntry.COLUMN_BOOK_PRICE + " INTEGER, "
                + BooksContract.BookEntry.COLUMN_BOOK_QUANTITY + " INTEGER NOT NULL, "
                + BooksContract.BookEntry.COLUMN_BOOK_SUPPLIER + " TEXT NOT NULL, "
                + BooksContract.BookEntry.COLUMN_BOOK_SUPPLIER_PHONE + " INTEGER NOT NULL DEFAULT 0);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_BOOKS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}