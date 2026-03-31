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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InputAudioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InputAudioFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageButton recordButton;

    private EditText transcriptionBox;
    private Uri audioUri;

    private String transcription;

    private SpeechRecognizer speechRecognizer;
    private static final int REQUEST_RECORD_AUDIO_CODE = 101;

    private MediaPlayer mediaPlayer;


    public InputAudioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InputAudioFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static InputAudioFragment newInstance(String param1, String param2) {
        InputAudioFragment fragment = new InputAudioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

        // Register the callback
        final ActivityResultLauncher<Intent> audioRecordLauncher = registerForActivityResult(
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input_audio, container, false);
    }

    // Define the launcher as a member variable

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Inside your Fragment's onViewCreated
        super.onViewCreated(view, savedInstanceState);

        recordButton = view.findViewById(R.id.recordButton);
        transcriptionBox = view.findViewById(R.id.transcriptionBox);

        recordButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            audioRecordLauncher.launch(intent);
        });
    }


    public Uri getAudioUri() {
        return audioUri;
    }

    public String getTranscription() {
        return transcription;
    }

    public void loadAudio(Uri uri, String transcription)
    {
        mediaPlayer = new MediaPlayer();
        transcriptionBox.setText(transcription);

        try {
            // Set context and URI
            mediaPlayer.setDataSource(requireContext(), audioUri);

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
