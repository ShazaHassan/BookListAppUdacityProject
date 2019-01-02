package com.example.shazahassan.booklistapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    private ListView searchResult;
    private SearchView searchField;
    private BookAdapter bookAdapter;
    private TextView noInternet,noResult;
    private boolean isConnected;
    private ConnectivityManager cm;
    private String mUrlRequestGoogleBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchField = findViewById(R.id.search_view);
        searchResult = findViewById(R.id.search_result);
        noInternet = findViewById(R.id.no_internet);
        noResult = findViewById(R.id.no_result);
        bookAdapter = new BookAdapter(this,new ArrayList<Book>());
        searchResult.setAdapter(bookAdapter);

        cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        /**
         * At the beginning check the connection with internet and save result to (boolean) variable isConnected
         * Checking if network is available
         * If TRUE - work with LoaderManager
         * If FALSE - hide loading spinner and show emptyStateTextView
         */
        checkConnection(cm);

        if (isConnected) {
            noInternet.setVisibility(GONE);

        } else {
            // Progress bar mapping
            Log.i("INTERNET connection : ", String.valueOf(isConnected) + ". Sorry dude, no internet - no data :(");
            noResult.setVisibility(GONE);
            searchResult.setVisibility(GONE);
            // Set empty state text to display "No internet connection."
        }

        searchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Book currentBook = bookAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentBook.getUri());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

    }

    public void searchForBook(View view) {

        checkConnection(cm);
        Log.v("connected",Boolean.toString(isConnected));
        if (isConnected) {
            // Update URL and restart loader to displaying new result of searching
            updateQueryUrl(searchField.getQuery().toString());
            Log.i("Search value: ",searchField.getQuery().toString());
            BookAsyncTask bookAsyncTask = new BookAsyncTask();
            bookAsyncTask.execute(mUrlRequestGoogleBooks);
        } else {
            // Clear the adapter of previous book data
            bookAdapter.clear();
            noResult.setVisibility(GONE);
            searchResult.setVisibility(GONE);
            noInternet.setVisibility(View.VISIBLE);
        }



    }

    private void updateQueryUrl(String searchValue) {

        if (searchValue.isEmpty()) {
            noInternet.setVisibility(GONE);
            searchResult.setVisibility(GONE);
            noResult.setVisibility(View.VISIBLE);
        } else{
        StringBuilder sb = new StringBuilder();
        sb.append("https://www.googleapis.com/books/v1/volumes?q=").append(searchValue).append("&maxResults=4");
        mUrlRequestGoogleBooks = sb.toString();
        searchResult.setVisibility(View.VISIBLE);


        }
    }

    public void checkConnection(ConnectivityManager connectivityManager) {
        // Status of internet connection
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null &&
                activeNetwork.isConnected()) {
            isConnected = true;

            Log.i("INTERNET connection : ", String.valueOf(isConnected));

        } else {
            isConnected = false;

        }
    }

    private class BookAsyncTask extends AsyncTask <String,Void,List<Book>>{

        @Override
        protected List<Book> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<Book> result = QueryUtils.fetchBooks(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<Book> data) {

            bookAdapter.clear();

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
                bookAdapter.addAll(data);
                noInternet.setVisibility(GONE);
                noResult.setVisibility(View.GONE);
            } else{
                noInternet.setVisibility(GONE);
                searchResult.setVisibility(GONE);
                noResult.setVisibility(View.VISIBLE);
            }
        }
    }
}
