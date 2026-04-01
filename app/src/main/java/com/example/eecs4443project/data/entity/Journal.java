package com.example.eecs4443project.data.entity;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.example.eecs4443project.view.fragments.journal.JournalEditFragment.InputMode;

// Create a table of journal entries, tied to a specific user by their ID
// If the user is deleted, so are their journal entries

@Entity(tableName = "journals",
        foreignKeys = @ForeignKey(
            entity = User.class,
            parentColumns = "id",
            childColumns = "userId",
            onDelete = ForeignKey.CASCADE
        )
)
public class Journal {

    @PrimaryKey(autoGenerate = true)
    private int id;
    // The user who owns this journal entry

    private int userId;
    // Journal entry title

    private String title;
    // Save date of entry as epoch ms
    private long date;
    // Actual contents of the entry
    private String entry;

    // Type of input mode
    private String inputMode = "TEXT";

    // Uri for audio

    private String uri;

    // Transcription for audio

    private String transcription;

    // It's possible for a journal entry to not have a label
    @Nullable private String label;

    // It's possible for a journal entry to not have a drawing path
    @Nullable private String drawingPath;

    // Constructor
    public Journal(int userId, String title, long date, String entry, @Nullable String label) {
        this.userId = userId;
        this.title = title;
        this.date = date;
        this.entry = entry;
        this.label = label;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public String getFormattedDate() {
        Date dateObj = new Date(this.date);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        return sdf.format(dateObj);
    }


    public void setDate(long date) {
        this.date = date;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public void setDrawingPath(@Nullable String drawingPath)
    {
      this.drawingPath = drawingPath;
    }

    public void setInputMode(String inputMode)
    {
        this.inputMode = inputMode;
    }

    public String getInputMode()
    {
        return inputMode;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setTranscription(String transcription)
    {
        this.transcription = transcription;
    }

    public String getTranscription()
    {
        return transcription;
    }

    @Nullable
    public String getDrawingPath(){return drawingPath;}

    @Nullable
    public String getLabel() {
        return label;
    }

    public void setLabel(@Nullable String label) {
        this.label = label;
    }
}
