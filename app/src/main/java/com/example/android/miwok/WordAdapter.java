package com.example.android.miwok;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Word> { // This WordAdapter class is inheriting stuff from ArrayAdapter class.
    private int mColorResourceId;

    public WordAdapter(Activity context, ArrayList<Word> names, int colorResourceId) { // This class constructor is taking a Context and a ArrayList class object.
        super(context, 0, names); // Here we are passing 0 as the resource id cuz we want to manually define the resource using the getView method.
        mColorResourceId = colorResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { // We are overriding this getView method from the ArrayAdapter class.
        View listItemView = convertView;
        if (listItemView == null) { // The listItemView will be null when there is no layout or view set for the Activity.
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false); // A LayoutInflater converts a xml file into View objects. Here it is converting the list_item.xml file into a ViewGroup.
        }
        Word currentWord = getItem(position); // This will return an object of the type Word. The getItem method is defined in the ArrayAdapter class.
        // Now we can update the textViews in the layout.
        TextView miwokTextView = listItemView.findViewById(R.id.miwok_text_view); // This miwok_text_view is the id of the TextView in list_item.xml file.
        miwokTextView.setText(currentWord.getMiwokTranslation()); // This method is defined in Word class which returns the miwok word.
        TextView defaultTextView = listItemView.findViewById(R.id.default_text_view); // This default_text_view is the id of the TextView in list_item.xml file.
        defaultTextView.setText(currentWord.getDefaultTranslation()); // This method is defined in Word class which returns the default word.
        ImageView imageView = listItemView.findViewById(R.id.image);
        if (currentWord.hasImage()) { // This will hide the image for the Activity in which the image is not provided.
            imageView.setImageResource(currentWord.getImageResourceId());
            imageView.setVisibility(View.VISIBLE); // Now the imageView will be visible for the Activities in which the image is provided.
        } else {
            imageView.setVisibility(View.GONE); // Now the imageView will be invisible and it will not take up blank space in the layout.
        }
        // This part is for setting the colour for each Activity separately.
        View textContainer = listItemView.findViewById(R.id.text_container);
        int color = ContextCompat.getColor(getContext(), mColorResourceId);
        textContainer.setBackgroundColor(color);
        return listItemView;
    }
}
