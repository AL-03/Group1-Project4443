package com.example.eecs4443project.view.fragments.journal;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
    // Used to store transcription in a Bundle from the previous fragment
    private static final String ARG_TRANSCRIPTION = "arg_transcription";

    private ImageButton recordButton;

    private EditText transcriptionBox;

    private String transcription;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> audioRecordLauncher;

    // Creates new instance of this fragment
    public static InputAudioFragment newInstance(String transcription) {
        InputAudioFragment fragment = new InputAudioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRANSCRIPTION, transcription);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get transcription from previous fragment
        if (getArguments() != null) {
            transcription = getArguments().getString(ARG_TRANSCRIPTION);
        }
    }

    // Inflate the layout for this fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_audio, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Inside your Fragment's onViewCreated
        super.onViewCreated(view, savedInstanceState);

        // Bind UI elements from the XML
        recordButton = view.findViewById(R.id.recordButton);
        transcriptionBox = view.findViewById(R.id.transcriptionBox);

        // If JournalEditFragment provided initial text (Edit mode), set it now
        if (transcription != null) {
            transcriptionBox.setText(transcription);
        }

        // Microphone permissions launcher
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        Toast.makeText(requireContext(), "Microphone access granted", Toast.LENGTH_SHORT).show();
                    } else {
                        new AlertDialog.Builder(requireContext())
                                .setTitle("Microphone Permission Needed")
                                .setMessage("To record audio, please enable microphone access in Settings.")
                                .setPositiveButton("Open Settings", (dialog, which) -> {
                                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                    }
                });

        // Speech recognition launcher
        audioRecordLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        ArrayList<String> matches = result.getData()
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (matches != null && !matches.isEmpty() && transcriptionBox != null) {
                            // Transcription
                            transcription = matches.get(0);
                            transcriptionBox.setText(transcription);
                        }
                    }
                }
        );

        recordButton.setOnClickListener(v -> {
            // Run speech recognition if access was granted
            if (requireContext().checkSelfPermission(android.Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED) {
                // Build intent for speech recognizer
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                // Launch speech-to-text pop-up
                audioRecordLauncher.launch(intent);
            } else {
                requestPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO);
            }
        });

    }


    //############## PUBLIC FUNCTIONS ##############

    // Called by JournalEditFragment when in Edit mode to pre-fill the EditText with the saved text
    public void setInitialTranscription(String text) {
        this.transcription = text;

        // If the view is already created, update immediately
        if (transcriptionBox != null) {
            transcriptionBox.setText(text);
        }
    }

    // Allows JournalEditFragment to access what was transcribed (and maybe edited by the user) in the EditText
    public String getTranscription() {
        if (transcriptionBox != null) {
            return transcriptionBox.getText().toString();
        }
        return "";
    }
}