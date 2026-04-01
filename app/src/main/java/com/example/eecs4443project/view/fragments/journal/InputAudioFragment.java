package com.example.eecs4443project.view.fragments.journal;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eecs4443project.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

// Used for voice-recorded journal entries
public class InputAudioFragment extends Fragment {

    private ImageButton recordButton;

    private EditText transcriptionBox;
    private Uri audioUri;

    private String transcription;

    private SpeechRecognizer speechRecognizer;
    private static final int REQUEST_RECORD_AUDIO_CODE = 101;

    private MediaPlayer mediaPlayer;
    private ActivityResultLauncher<Intent> audioRecordLauncher;

    // Inflate the layout for this fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_audio, container, false);
    }

    // Define the launcher as a member variable

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Inside your Fragment's onViewCreated
        super.onViewCreated(view, savedInstanceState);

        // Register the callback
        audioRecordLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        ArrayList<String> matches = result.getData()
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (matches != null && !matches.isEmpty()) {
                            transcription = matches.get(0);  // Transcription
                        }

                        //Uri
                        audioUri = result.getData().getData();
                    }
                }
        );

        recordButton = view.findViewById(R.id.recordButton);
        transcriptionBox = view.findViewById(R.id.transcriptionBox);

        recordButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            audioRecordLauncher.launch(intent);
        });
    }


    public String getAudioUri() {
        return audioUri.toString();
    }

    public String getTranscription() {
        return transcription;
    }

    public void loadAudio(String uri, String transcription)
    {
        mediaPlayer = new MediaPlayer();
        transcriptionBox.setText(transcription);

        try {
            // Set context and URI
            mediaPlayer.setDataSource(requireContext(), Uri.parse(uri));

            // Prepare the player (synchronous for local files)
            mediaPlayer.prepare();

            // Start playback
            mediaPlayer.start();

            // Optional: Release resources when finished
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}