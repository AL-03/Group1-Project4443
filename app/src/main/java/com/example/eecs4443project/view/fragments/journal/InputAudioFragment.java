package com.example.eecs4443project.view.fragments.journal;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.eecs4443project.R;

// Used for voice-recorded journal entries
public class InputAudioFragment extends Fragment {
    // URI of recorded audio
    private Uri audioUri = null;

    private ImageButton recordButton;
//    private ImageButton playButton;
//    private EditText transcriptionBox;

    // Allows us to launch the device's default audio recorded
    private ActivityResultLauncher<Intent> audioRecorderLauncher;

    // Inflate the layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_audio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        // Get UI elements from xml
        recordButton = view.findViewById(R.id.recordButton);
//        playButton = view.findViewById(R.id.playButton);
//        transcriptionBox = view.findViewById(R.id.transcriptionBox);
//        playButton.setVisibility(View.GONE);

        // Launch the device's audio recorder
        audioRecorderLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            audioUri = data.getData();
//                            playButton.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );
        recordButton.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
            audioRecorderLauncher.launch(intent);
        });

//        playButton.setOnClickListener(v -> {
//            if (audioUri != null) {
//                MediaPlayer player = MediaPlayer.create(requireContext(), audioUri);
//                player.start();
//            }
//        });

//        // If editing an existing entry, load audio + transcription
//        if (audioUri != null) {
//            playButton.setVisibility(View.VISIBLE);
//            transcriptionBox.setText(transcription);
//        }
    }

    // Called by JournalEditFragment to load existing audio
    public void loadAudio(String uriString) {
        if (uriString != null) {
            audioUri = Uri.parse(uriString);
        }
    }

//    // Called by JournalEditFragment to load existing audio transcription
//    public void loadTranscription(String text) {
//        transcription = text;
//
//        if (transcriptionBox != null) {
//            transcriptionBox.setText(text);
//        }
//    }

    // Called by JournalEditFragment to save audio
    public String getAudioUri() {
        return audioUri != null ? audioUri.toString() : null;
    }

//    // Called by JournalEditFragment to save audio transcription
//    public String getTranscription() {
//        return transcriptionBox.getText().toString();
//    }
}