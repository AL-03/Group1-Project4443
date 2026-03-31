package com.example.eecs4443project.data.entity;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    // It's possible for a journal entry to not have a label
    @Nullable private String label;
    // Store input mode used ("TEXT", "DRAW", "AUDIO")
    private String inputMode;
    // Entry if inputMode == "TEXT"
    @Nullable private String textContent;
    // Entry file path if inputMode == "DRAW"
    @Nullable private String drawPath;
    // Entry URI if inputMode == "AUDIO"
    @Nullable private String audioUri;


    // Constructor
    public Journal(int userId, String title, long date, @Nullable String label, String inputMode,
                   @Nullable String textContent, @Nullable String drawPath, @Nullable String audioUri) {
        this.userId = userId;
        this.title = title;
        this.date = date;
        this.label = label;
        this.inputMode = inputMode;
        this.textContent = textContent;
        this.drawPath = drawPath;
        this.audioUri = audioUri;
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

    @Nullable
    public String getLabel() {
        return label;
    }

    public void setLabel(@Nullable String label) {
        this.label = label;
    }

    public String getInputMode() {
        return inputMode;
    }

    public void setInputMode(String inputMode) {
        this.inputMode = inputMode;
    }

    @Nullable
    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(@Nullable String textContent) {
        this.textContent = textContent;
    }

    @Nullable
    public String getDrawPath() {
        return drawPath;
    }

    public void setDrawPath(@Nullable String drawPath) {
        this.drawPath = drawPath;
    }

    @Nullable
    public String getAudioUri() {
        return audioUri;
    }

    public void setAudioUri(@Nullable String audioUri) {
        this.audioUri = audioUri;
    }
}
