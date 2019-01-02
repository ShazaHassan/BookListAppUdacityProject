package com.example.shazahassan.booklistapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Shaza Hassan on 25-Sep-18.
 */
public class BookAdapter  extends ArrayAdapter<Book> {

    public BookAdapter(Context context, List<Book> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_view, parent, false);
        }

        TextView titleBook = listItemView.findViewById(R.id.book_title);
        TextView authorBook = listItemView.findViewById(R.id.book_author);
        Book currentBook = getItem(position);
        titleBook.setText(currentBook.getTitle());
        authorBook.setText(currentBook.getAuthor());

        return listItemView;
    }
}
