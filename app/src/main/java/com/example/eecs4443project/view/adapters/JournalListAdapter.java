package com.example.eecs4443project.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eecs4443project.R;
import com.example.eecs4443project.data.entity.Journal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JournalListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // Used to determine whether the item the RecyclerView is showing is a label (which will be a header) or an actual entry
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ENTRY = 1;

    // List of items to display with RecyclerView
    private final List<Object> items = new ArrayList<>();

    // Used to detect when entry is clicked and run the callback function
    private OnJournalClickListener listener;

    // Define a callback to the JournalListFragment so that the adapter doesn't directly access other fragments
    public interface OnJournalClickListener {
        // Short click function
        void onJournalClick(Journal journal);
        // Long click function
        void onJournalLongClick(Journal journal);
    }

    // Used to detect when entry is clicked and run the callback function to open JournalDetailFragment
    public void setOnJournalClickListener(OnJournalClickListener listener) {
        this.listener = listener;
    }

    // Gets data from ViewModel to display
    public void submitData(Map<String, List<Journal>> grouped) {
        // Clean the items list
        items.clear();
        // For each group of journal entries, add the label to the list, then add the entries to the list
        for (String label : grouped.keySet()) {
            items.add(label); // label (which will serve as a header)
            items.addAll(grouped.get(label)); // entries
        }
        notifyDataSetChanged();
    }

    // Identify whether the item is a String (i.e. the labels) or a Journal object (i.e. the journal entries)
    // Used to determine the formatting of the item in RecyclerView
    @Override
    public int getItemViewType(int position) {
        return (items.get(position) instanceof String) ? TYPE_HEADER : TYPE_ENTRY;
    }

    // Inflate layout of the item depending on whether it's a label or journal entry
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_label, parent, false);
            return new LabelHeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_entry, parent, false);
            return new JournalEntryViewHolder(view, listener);
        }
    }

    // Fill out RecyclerView items with the actual data
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LabelHeaderViewHolder) {
            ((LabelHeaderViewHolder) holder).bind((String) items.get(position));
        } else {
            ((JournalEntryViewHolder) holder).bind((Journal) items.get(position));
        }
    }

    // Get number of items in the list
    @Override
    public int getItemCount() {
        return items.size();
    }
}

//################ VIEWHOLDERS ################
// Viewholders bind data to the layout

// ViewHolder for label
class LabelHeaderViewHolder extends RecyclerView.ViewHolder {
    // Notes that there is a TextView in the label's layout
    private final TextView label;

    LabelHeaderViewHolder(View itemView) {
        super(itemView);
        label = itemView.findViewById(R.id.labelHeader);
    }

    // Sets the text in the TextView given the data
    void bind(String labelText) {
        label.setText(labelText);
    }
}

// ViewHolder for Journal
class JournalEntryViewHolder extends RecyclerView.ViewHolder {
    private final TextView title;
    private final TextView date;
    // Store the current Journal object
    private Journal currEntry;

    JournalEntryViewHolder(View itemView, JournalListAdapter.OnJournalClickListener listener) {
        super(itemView);
        title = itemView.findViewById(R.id.entryTitle);
        date = itemView.findViewById(R.id.entryDate);

        // Use the callback function to open the entry's detailed view when clicked
        itemView.setOnClickListener(v -> {
            if (listener != null && currEntry != null) {
                listener.onJournalClick(currEntry);
            }
        });

        // Use the callback function to offer the option to edit/delete when long clicked
        itemView.setOnLongClickListener(v -> {
            if (listener != null && currEntry != null) {
                listener.onJournalLongClick(currEntry);
                return true; // note that the long click has been completed
            }
            return false;
        });

    }

    // Sets the text in the TextView given the data
    void bind(Journal entry) {
        title.setText(entry.getTitle());
        date.setText(entry.getFormattedDate());
        currEntry = entry;
    }
}



