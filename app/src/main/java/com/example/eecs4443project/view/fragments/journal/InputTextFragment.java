package com.example.eecs4443project.view.fragments.journal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.eecs4443project.R;

// Used for typed journal entries
public class InputTextFragment extends Fragment {
    // Get EditText where user types out journal entry
    private EditText textInputField;

    // Stores initial text passed in from JournalEditFragment (if in Edit mode)
    private String initialText = null;

    // Inflate the XML layout for this fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_text, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        // Bind the EditText from the XML
        textInputField = view.findViewById(R.id.journalTextInput);

        // If JournalEditFragment provided initial text (Edit mode), set it now
        if (initialText != null) {
            textInputField.setText(initialText);
        }
    }

    //############## PUBLIC FUNCTIONS ##############

    // Called by JournalEditFragment when in Edit mode to pre-fill the EditText with the saved text
    public void setInitialText(String text) {
        this.initialText = text;

        // If the view is already created, update immediately
        if (textInputField != null) {
            textInputField.setText(text);
        }
    }

    // Allows JournalEditFragment to access what the user typed into the EditText
    public String getText() {
        if (textInputField != null) {
            return textInputField.getText().toString();
        }
        return "";
    }
}