package com.example.eecs4443project.view.fragments.journal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.eecs4443project.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

// Used for hand-drawn journal entries
public class InputDrawFragment extends Fragment {
    // Used to store draw path in a Bundle from the previous fragment
    private static final String ARG_SAVED_PATH = "arg_saved_path";
    // Custom view where user draws
    private DrawingView drawingView;
    // Path to existing drawing (for Edit mode)
    private String savedPath = null;
    private int numOfSavedDrawings;

    // Buttons for drawing tools
    private ImageButton undoButton, redoButton, penButton, eraserButton;

    // Creates new instance of this fragment
    public static InputDrawFragment newInstance(String savedPath) {
        InputDrawFragment fragment = new InputDrawFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SAVED_PATH, savedPath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get draw path from previous fragment
        if (getArguments() != null) {
            savedPath = getArguments().getString(ARG_SAVED_PATH);
        }
    }

    // Inflate layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_draw, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Get references to UI elements
        drawingView = view.findViewById(R.id.drawingCanvas);
        undoButton = view.findViewById(R.id.undoButton);
        redoButton = view.findViewById(R.id.redoButton);
        penButton = view.findViewById(R.id.penButton);
        eraserButton = view.findViewById(R.id.eraserButton);
        numOfSavedDrawings = 0;

        // If editing an entry with an existing drawing, load it
        if (savedPath != null) {
            drawingView.post(() -> {
                Bitmap bitmap = BitmapFactory.decodeFile(savedPath);
                if (bitmap != null) {
                    drawingView.loadBitmap(bitmap);
                }
            });
        }

        // TODO: Add undo/redo stacks
        // TODO: Add pen/eraser mode switching
        // TODO: Add saving/loading drawing bitmap
    }

    //############## PUBLIC FUNCTIONS ##############

    // Load existing drawn image
    public void loadBitmap(String savedPath) {
        if (savedPath != null) {
            File imgFile = new File(savedPath);

            if (imgFile.exists()) {
                // Decode the file into a Bitmap
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                // Set it in drawingView
                drawingView.loadBitmap(bitmap);
            }
        }

    }

    // Expose the drawing as a Bitmap for saving to Room
    public String saveDrawingToInternalStorage(Context requiredContext) {
        try {
            File file = new File(requiredContext.getFilesDir(), "my_drawing" + numOfSavedDrawings + ".png");
            savedPath = file.getAbsolutePath();
            FileOutputStream fos = new FileOutputStream(file);
            drawingView.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        numOfSavedDrawings++;
        return savedPath;
    }

    public Bitmap getDrawingBitmap() {
        return drawingView.getBitmap();
    }
}