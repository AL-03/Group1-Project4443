package com.example.eecs4443project.view.fragments.journal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eecs4443project.R;
import com.example.eecs4443project.SessionManager;
import com.example.eecs4443project.data.entity.Journal;
import com.example.eecs4443project.view.activities.JournalPasswordPopupActivity;
import com.example.eecs4443project.view.activities.ProfileActivity;
import com.example.eecs4443project.view.adapters.JournalListAdapter;
import com.example.eecs4443project.viewmodel.JournalViewModel;

// Displays list of user's journals
public class JournalListFragment extends Fragment {
    // Allow indirect access to data
    private JournalViewModel viewModel;
    private JournalListAdapter adapter;
    private static boolean isPasswordCorrect = false;
    private Journal pendingJournal;

    private final ActivityResultLauncher<Intent> passwordLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // This runs ONLY after the user enters the correct password and the activity finishes
                    if(isPasswordCorrect && pendingJournal != null)
                    {
                        navigateToJournalDetail(pendingJournal);
                    }
                }
            }
    );


    // Creates new instance of this fragment
    public static JournalListFragment newInstance() {
        return new JournalListFragment();
    }

    // Inflates the fragment's view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_journal_list, container, false);
    }

    // Set up fragment's view to display the list of journal entries
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Access journal entry data
        viewModel = new ViewModelProvider(requireActivity()).get(JournalViewModel.class);

        // Get userId from SharedPreferences
        int userId = SessionManager.getUserId(requireContext());
        // If user can't be found, show a pop-up message
        if (userId == -1) {
            Toast.makeText(requireContext(), "Cannot find user", Toast.LENGTH_SHORT).show();
        }
        // Otherwise, get the user's journal entries
        else {
            viewModel.getJournalsByAllLabels(userId).observe(getViewLifecycleOwner(), groupedMap -> {
                adapter.submitData(groupedMap);
            });
        }

        // Set up RecyclerView (list of journal entries)
        RecyclerView recyclerView = view.findViewById(R.id.journalList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Set up RecyclerView adapter
        adapter = new JournalListAdapter();
        adapter.setOnJournalClickListener(new JournalListAdapter.OnJournalClickListener() {
            // On short click, go to entry's detailed view
            @Override
            public void onJournalClick(Journal journal) {
            // Passes journal ID to detailed fragment
                if(ProfileActivity.isJournalPassword())
                {
                    pendingJournal = journal;
                    Intent intent = new Intent(requireContext(), JournalPasswordPopupActivity.class);
                    startActivity(intent);
                }

                else
                {
                    navigateToJournalDetail(journal);
                }
            }

            // On long click, offer option to edit or delete the entry
            @Override
            public void onJournalLongClick(Journal journal) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Journal Options");

                String[] options = {"Edit", "Delete"};

                builder.setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        // Navigate to edit screen
                        JournalEditFragment fragment = JournalEditFragment.newInstance(journal.getId());
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.journal_fragment_container, fragment)
                                .addToBackStack(null)
                                .commit();
                    } else {
                        // Delete entry
                        AlertDialog.Builder confirmation = new AlertDialog.Builder(requireContext());
                        confirmation.setTitle("Delete Entry")
                                .setMessage("Are you sure you want to delete this entry?")
                                .setPositiveButton("Delete", (dialogConfirm, whichConfirm) -> {
                                    viewModel.delete(journal);
                                    Toast.makeText(requireContext(), "Entry deleted", Toast.LENGTH_SHORT).show();
                                })
                                .setNegativeButton("Cancel", (dialogConfirm, whichConfirm) -> {
                                    Toast.makeText(requireContext(), "Delete action cancelled", Toast.LENGTH_SHORT).show();
                                });

                        confirmation.show();
                    }
                });

                builder.show();
            }

            // If Edit button clicked, navigate to fragment to edit the journal entry
            @Override
            public void onEditClick(Journal journal) {
                // Navigate to edit screen
                JournalEditFragment fragment = JournalEditFragment.newInstance(journal.getId());
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.journal_fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }

            // If Delete button clicked, delete the entry
            @Override
            public void onDeleteClick(Journal journal) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Delete Entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            viewModel.delete(journal);
                            Toast.makeText(requireContext(), "Entry deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            Toast.makeText(requireContext(), "Delete action cancelled", Toast.LENGTH_SHORT).show();
                        });

                builder.show();
            }
        });
        recyclerView.setAdapter(adapter);

        // Navigate to fragment to add journal entry
        view.findViewById(R.id.addJournal).setOnClickListener(v -> {
            JournalEditFragment fragment = JournalEditFragment.newInstance(-1);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.journal_fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }
    private void navigateToJournalDetail(Journal journal) {
        JournalDetailFragment fragment = JournalDetailFragment.newInstance(journal.getId());
        // Replaces current fragment with JournalDetailFragment and will return to current fragment if Back button is clicked
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.journal_fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public static void  setJournalPasswordCorrect(boolean bol)
    {
        isPasswordCorrect = bol;
    }
}
