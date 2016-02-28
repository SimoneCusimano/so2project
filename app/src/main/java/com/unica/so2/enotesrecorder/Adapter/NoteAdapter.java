package com.unica.so2.enotesrecorder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.unica.so2.enotesrecorder.Model.Note;
import com.unica.so2.enotesrecorder.R;

import java.util.ArrayList;

public class NoteAdapter extends ArrayAdapter<Note> {
    public NoteAdapter(Context context, ArrayList<Note> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Note note = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_note, parent, false);
        }

        // Lookup view for data population
        TextView titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
        //TextView lastEditTextView = (TextView) convertView.findViewById(R.id.lastEditTextView);
        TextView ratingTextView = (TextView) convertView.findViewById(R.id.ratingTextView);

        // Populate the data into the template view using the data object
        titleTextView.setText(note.getTitle());
        //lastEditTextView.setText(note.getLastEdit().toString());
        ratingTextView.setText(Float.toString(note.getRating()));

        // Return the completed view to render on screen
        return convertView;
    }
}