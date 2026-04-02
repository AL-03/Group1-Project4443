package com.example.eecs4443project.view.fragments.journal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eecs4443project.R;
import com.example.eecs4443project.data.entity.Journal;
import com.example.eecs4443project.viewmodel.JournalViewModel;

// Displays details of a specific journal entry
public class JournalDetailFragment extends Fragment {
    // Used to store journal ID in a Bundle from the previous fragment
    private static final String ARG_JID = "journalId";
    // Save the journal ID in a variable (instead of having to get it from the previous fragment each time)
    private int journalId;
    // Store the journal to be displayed
    private Journal j;
    // Allow indirect access to data
    private JournalViewModel viewModel;

    // Creates new instance of this fragment
    public static JournalDetailFragment newInstance(int journalId) {
        JournalDetailFragment fragment = new JournalDetailFragment();
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

        // Get journal id from previous fragment
        if (getArguments() != null) {
            journalId = getArguments().getInt(ARG_JID);
        }
    }

    // Inflate the layout for this fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_journal_detail, container, false);
    }

    // Set the text displayed in the xml based on the journal entry data passed in from the previous fragment
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Access the views in xml
        TextView title = view.findViewById(R.id.journalTitle);
        TextView date = view.findViewById(R.id.entryDate);
        TextView label = view.findViewById(R.id.entryLabel);
        TextView textBody = view.findViewById(R.id.entryBody);
        ImageView imgBody = view.findViewById(R.id.drawnEntryBody);

        // Get the journal object
        viewModel.getJournal(journalId).observe(getViewLifecycleOwner(), journal -> {
            // Save the journal object so it can be used later in the code
            j = journal;

            // Set the TextView content, but if journal entry isn't found, indicate that that's the case
            if (journal != null) {
                title.setText(journal.getTitle());
                date.setText(journal.getFormattedDate());
                label.setText(journal.getLabel());
                // Display content based on input mode
                if (j.getInputMode().equals("TEXT")) {
                    imgBody.setVisibility(ViewGroup.GONE);
                    textBody.setVisibility(ViewGroup.VISIBLE);
                    textBody.setText(journal.getEntry());
                }
                else if (j.getInputMode().equals("DRAW")) {
                    textBody.setVisibility(ViewGroup.GONE);
                    imgBody.setVisibility(ViewGroup.VISIBLE);

                    String imgPath = journal.getDrawingPath();
                    if (imgPath != null) {
                        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                        if (bitmap != null) {
                            imgBody.setImageBitmap(bitmap);
                        }
                        else {
                            imgBody.setImageResource(R.drawable.image_not_found);
                            Toast.makeText(requireContext(), "Image not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        imgBody.setImageResource(R.drawable.image_not_found);
                        Toast.makeText(requireContext(), "No image path found", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (j.getInputMode().equals("AUDIO")) {
                    imgBody.setVisibility(ViewGroup.GONE);
                    textBody.setVisibility(ViewGroup.VISIBLE);
                    textBody.setText(journal.getTranscription());
                }
            }
            else {
                Toast.makeText(requireContext(), "Journal entry not found", Toast.LENGTH_SHORT).show();
            }
        });

        // If Edit button clicked, navigate to fragment to edit the journal entry
        view.findViewById(R.id.editButton).setOnClickListener(v -> {
            // Navigate to edit screen
            JournalEditFragment fragment = JournalEditFragment.newInstance(journalId);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.journal_fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        // If Delete button clicked, have a pop-up message confirming the action, then delete the current journal entry and go back to list view
        view.findViewById(R.id.delButton).setOnClickListener(v -> {
            if (j != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Delete Entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            viewModel.delete(j);
                            Toast.makeText(requireContext(), "Entry deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            Toast.makeText(requireContext(), "Delete action cancelled", Toast.LENGTH_SHORT).show();
                        });

                builder.show();
            }
            else {
                Toast.makeText(requireContext(), "Journal entry not found", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(requireContext(), "Entry deleted", Toast.LENGTH_SHORT).show();

            // Navigate back to List screen
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });
    }
}