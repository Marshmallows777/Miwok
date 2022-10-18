package com.example.android.miwok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    AudioManager audioManager; // This object is created to handle the audio focus in our app.
    // This part will take care of the AudioFocusChangeListener.
    AudioManager.OnAudioFocusChangeListener OnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // Pause Playback
                mediaPlayer.pause();
                mediaPlayer.seekTo(0); // The user will hear the audio from the start and not from where it was paused.
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // Resume Playback
                mediaPlayer.start(); // We have regained the audio focus.
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // Stop Playback and release the resources
                releaseMediaPlayer();
            }
        }
    };
    // The below line for releasing the resources of the MediaPlayer is now stored in the mCompletionListener variable.
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        final ArrayList<Word> names = new ArrayList<Word>();

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE); // This line is required to create and request for audio focus.

        // This is part is when we have added an image beside each text and audio for each view.
        names.add(new Word("one", "lutti", R.drawable.number_one, R.raw.number_one));
        names.add(new Word("two", "otiiko", R.drawable.number_two, R.raw.number_two));
        names.add(new Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        names.add(new Word("four", "oyyisa", R.drawable.number_four, R.raw.number_four));
        names.add(new Word("five", "massokka", R.drawable.number_five, R.raw.number_five));
        names.add(new Word("six", "temmokka", R.drawable.number_six, R.raw.number_six));
        names.add(new Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        names.add(new Word("eight", "tawinta", R.drawable.number_eight, R.raw.number_eight));
        names.add(new Word("nine", "wo'e", R.drawable.number_nine, R.raw.number_nine));
        names.add(new Word("ten", "na'aacha", R.drawable.number_ten, R.raw.number_ten));

        WordAdapter itemsAdapter = new WordAdapter(this, names, R.color.category_numbers); // The updated textViews in the layout will be stored in this object.
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);

        // This part is for playing the audio file for each view.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Word word = names.get(position); // This will return the current position of the view.
                releaseMediaPlayer();
                // This part is to request for the audio focus.
                int result = audioManager.requestAudioFocus(OnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, // To use the music stream.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT); // To request for permanent focus.
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer = MediaPlayer.create(NumbersActivity.this, word.getmAudioResourceId()); // This will play the audio file corresponding ot the view.
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mCompletionListener); // With this line we can call the OnCompletionListener method for clearing the resources. It is defined as a global variable.
                }
            }
        });
    }

    @Override
    protected void onStop() { // This method will be called when we leave the app and go to another app but not close it completely.
        super.onStop(); // This line is important to override the method.
        releaseMediaPlayer(); // This will clear the resources when we switch to another app. It will stop the audio immediately when we go to another app.
    }

    private void releaseMediaPlayer() { // This function will release all the resources held by the MediaPlayer so that it can be used by others as well.
        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) { // If the mediaPlayer is null that means there is no object instantiated for the MediaPlayer.
            // Regardless of the current state of the media player, release its resources cuz we no longer need it.
            mediaPlayer.release();
            // Set the media player back to null. For our code, we've decided that setting the media player to null is an easy way to tell that the media player is not configured to play an audio file at the moment.
            mediaPlayer = null;
            // This line will abandon the audio focus cuz we have finished playing the audio file.
            audioManager.abandonAudioFocus(OnAudioFocusChangeListener);
        }
    }
}