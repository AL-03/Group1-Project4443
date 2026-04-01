//TODO: Finish this

package com.example.eecs4443project.view.fragments.journal;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eecs4443project.R;
import com.example.eecs4443project.SessionManager;
import com.example.eecs4443project.data.entity.Journal;
import com.example.eecs4443project.data.entity.User;
import com.example.eecs4443project.view.activities.JournalActivity;
import com.example.eecs4443project.viewmodel.JournalViewModel;
import com.google.android.material.button.MaterialButton;

import java.time.Instant;

// Allows for editing/adding journal entries
public class JournalEditFragment extends Fragment {
    // Used to store journal ID in a Bundle from the previous fragment
    private static final String ARG_JID = "journalId";
    // True if editing an existing entry; False if we're adding an entry
    private boolean isEditMode = false;
    // ID of the journal entry being edited (-1 if we're adding an entry)
    private int journalId = -1;
    // Store the journal to be edited (null if we're adding a new one)
    private Journal j = null;
    // Allow indirect access to data
    private JournalViewModel viewModel;
    // Determine input mode
    public enum InputMode { TEXT, DRAW, AUDIO }
    private InputMode currentMode = InputMode.TEXT;
    // UI elements from xml
    TextView editTitleHeader;
    EditText editTitle;
    TextView editLabelHeader;
    EditText editLabel;
    MaterialButton textModeButton;
    MaterialButton drawModeButton;
    MaterialButton audioModeButton;
    // Fragments for each input mode
    InputTextFragment textFragment;
    InputDrawFragment drawFragment;
    InputAudioFragment audioFragment;

    // Creates new instance of this fragment
    public static JournalEditFragment newInstance(int journalId) {
        JournalEditFragment fragment = new JournalEditFragment();
        // Creates key-value map to store data passed within the JournalActivity
        Bundle args = new Bundle();
        // Add journalId and its value to the Bundle as something to be stored within the JournalActivity
        args.putInt(ARG_JID, journalId);
        // Saves journalId such that we can access it later
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Allow access to journal entry data
        viewModel = new ViewModelProvider(requireActivity()).get(JournalViewModel.class);

        // Determine Add (null journal object) vs Edit mode (provided journal ID)
        if (getArguments() != null) {
            journalId = getArguments().getInt(ARG_JID, -1);
            isEditMode = (journalId != -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_journal_edit, container, false);
    }

    // Set the text displayed in the xml based on whether we're in Add or Edit mode
    // Also show the input mode
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        // Get UI elements from xml
        editTitleHeader = view.findViewById(R.id.editTitleHeader);
        editTitle = view.findViewById(R.id.editTitle);
        editLabelHeader = view.findViewById(R.id.editLabelHeader);
        editLabel = view.findViewById(R.id.editLabel);
        textModeButton = view.findViewById(R.id.modeTyping);
        drawModeButton = view.findViewById(R.id.modeDraw);
        audioModeButton = view.findViewById(R.id.modeAudio);


        // Get fragments for different input modes
        textFragment = new InputTextFragment();
        drawFragment = new InputDrawFragment();
        audioFragment = new InputAudioFragment();

        // Load initial input mode fragment (default: text)
        switchToMode(InputMode.TEXT);

        // Allow switching to different input modes
        textModeButton.setOnClickListener(v -> switchToMode(InputMode.TEXT));
        drawModeButton.setOnClickListener(v -> switchToMode(InputMode.DRAW));
        audioModeButton.setOnClickListener(v -> switchToMode(InputMode.AUDIO));

//        //Set up save button
          view.findViewById(R.id.saveJournal).setOnClickListener(v -> {
               String title = editTitle.getText().toString();
               String label = editLabel.getText().toString();

          // Create or update entry object
           Journal journal = isEditMode ? j : new Journal(SessionManager.getUserId(requireContext()), title, System.currentTimeMillis(), "", label);

           journal.setInputMode(currentMode);

           // Collect content based on input mode
           if (currentMode == InputMode.TEXT) {
               journal.setEntry(textFragment.getText());
           }
            else if (currentMode == InputMode.DRAW) {
                String savedPath = drawFragment.saveDrawingToInternalStorage(requireContext());
                journal.setDrawingPath(savedPath);
           }
           else if (currentMode == InputMode.AUDIO) {
                journal.setUri(audioFragment.getAudioUri());
                journal.setTranscription(audioFragment.getTranscription());
         }


           // Insert or update in Room
           if (isEditMode) {
                viewModel.update(journal);
          } else {
               viewModel.insert(journal);
            }

            // Close fragment or navigate back
           requireActivity().onBackPressed();
       });

        // Set up view based on whether we're in Edit mode or Add mode
        if (isEditMode) {
            viewModel.getJournal(journalId).observe(getViewLifecycleOwner(), journal -> {
                if (journal != null) {
                    Journal j = journal;

                    // Set up edit instructions
                    editTitleHeader.setText(getString(R.string.edit_title));
                    editLabelHeader.setText(getString(R.string.edit_label));

                    // Pre-fill title and label
                    editTitle.setText(journal.getTitle());
                    editLabel.setText(journal.getLabel());

//                // Load correct input mode
               currentMode = journal.getInputMode();
               switchToMode(currentMode);
              // Pre-fill input fragment content
              if (currentMode == InputMode.TEXT) {
                   textFragment.setInitialText(journal.getEntry());
              } else if (currentMode == InputMode.DRAW) {
                    drawFragment.loadBitmap(journal.getDrawingPath());
               } else if (currentMode == InputMode.AUDIO) {
                    audioFragment.loadAudio(journal.getUri(), journal.getTranscription());
                }
                }
            });
        }
        else {
            // Set up add instructions
            editTitleHeader.setText(getString(R.string.set_title));
            editLabelHeader.setText(getString(R.string.set_label));

            // Pre-fill title and label with default content
            editTitle.setText(getString(R.string.default_journal_title));
            editLabel.setText(getString(R.string.default_journal_label));

        }
    }

    //############## PRIVATE FUNCTIONS ##############
    // Switches the journal entry input fragment based on the selected mode
    private void switchToMode(InputMode mode) {
        currentMode = mode;

        Fragment fragmentToShow;

        switch (mode) {
            case TEXT:
                fragmentToShow = textFragment;
                break;
            case DRAW:
                fragmentToShow = drawFragment;
                break;
            case AUDIO:
                fragmentToShow = audioFragment;
                break;
            default:
                fragmentToShow = textFragment;
        }

        // Replace the fragment inside the container
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.inputModeFragmentContainer, fragmentToShow)
                .commit();
    }

}