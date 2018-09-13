package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.inventoryapp.data.BooksContract;
import com.example.android.inventoryapp.data.BooksDBHelper;

public class BooksActivity extends AppCompatActivity {

    /**
     * Database helper that will provide us access to the database
     */
    private BooksDBHelper mDbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BooksActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new BooksDBHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the books database.
     */
    private void displayDatabaseInfo() {

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                BooksContract.BookEntry._ID,
                BooksContract.BookEntry.COLUMN_BOOK_NAME,
                BooksContract.BookEntry.COLUMN_BOOK_QUANTITY,
                BooksContract.BookEntry.COLUMN_BOOK_PRICE,
                BooksContract.BookEntry.COLUMN_BOOK_SUPPLIER,
                BooksContract.BookEntry.COLUMN_BOOK_SUPPLIER_PHONE
        };

        Cursor cursor = db.query(
                BooksContract.BookEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null,
                null
        );
        TextView displayView = findViewById(R.id.text_view_book);
        try {
            // Create a header in the Text View that looks like this:
            //
            // The books table contains <number of rows in Cursor> books.
            // _id - name - quantity- price - supplier - phone
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            displayView.setText("The books table contains " + cursor.getCount() + "books.\n\n");
            displayView.append(BooksContract.BookEntry._ID + " - " +
                    BooksContract.BookEntry.COLUMN_BOOK_NAME + " - " +
                    BooksContract.BookEntry.COLUMN_BOOK_QUANTITY + " - " +
                    BooksContract.BookEntry.COLUMN_BOOK_PRICE + " - " +
                    BooksContract.BookEntry.COLUMN_BOOK_SUPPLIER + " - " +
                    BooksContract.BookEntry.COLUMN_BOOK_SUPPLIER_PHONE + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(BooksContract.BookEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(BooksContract.BookEntry.COLUMN_BOOK_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(BooksContract.BookEntry.COLUMN_BOOK_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(BooksContract.BookEntry.COLUMN_BOOK_PRICE);
            int supplierColumnIndex = cursor.getColumnIndex(BooksContract.BookEntry.COLUMN_BOOK_SUPPLIER);
            int phoneColumnIndex = cursor.getColumnIndex(BooksContract.BookEntry.COLUMN_BOOK_SUPPLIER_PHONE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {

                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentPrice = cursor.getString(priceColumnIndex);
                String currentSupplier = cursor.getString(supplierColumnIndex);
                int currentPhone = cursor.getInt(phoneColumnIndex);

                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentQuantity + " - " +
                        currentPrice + " - " +
                        currentSupplier + " - " +
                        currentPhone));
            }

        } finally {

            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    /**
     * Helper method to insert hardcoded book data into the database. For debugging purposes only.
     */
    private void insertBook() {
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and Reckless's book attributes are the values.
        ContentValues values = new ContentValues();
        values.put(BooksContract.BookEntry.COLUMN_BOOK_NAME, "Reckless");
        values.put(BooksContract.BookEntry.COLUMN_BOOK_AUTHOR, "Sidney Sheldon");
        values.put(BooksContract.BookEntry.COLUMN_BOOK_PRICE, 20);
        values.put(BooksContract.BookEntry.COLUMN_BOOK_QUANTITY, 30);
        values.put(BooksContract.BookEntry.COLUMN_BOOK_SUPPLIER, "HarperCollins");
        values.put(BooksContract.BookEntry.COLUMN_BOOK_SUPPLIER_PHONE, 1 - 800 - 456 - 7804);

        // Insert a new row for Reckless in the database, returning the ID of that new row.
        // The first argument for db.insert() is the books table name.
        // The second argument provides the name of a column in which the framework
        // can insert NULL in the event that the ContentValues is empty (if
        // this is set to "null", then the framework will not insert a row when
        // there are no values).
        // The third argument is the ContentValues object containing the info for Reckless.
        db.insert(BooksContract.BookEntry.TABLE_NAME, null, values);
    }

    /**
     * Helper method to delete all books in the database.
     */
    private void deleteAllBooks() {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted = db.delete(BooksContract.BookEntry.TABLE_NAME,null,null);
        Log.v("BookActivity", rowsDeleted + " rows deleted from book database");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_books, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertBook();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllBooks();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
