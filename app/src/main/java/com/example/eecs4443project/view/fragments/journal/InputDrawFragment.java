package com.example.eecs4443project.view.fragments.journal;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.eecs4443project.R;

public class InputDrawFragment extends Fragment {

    // Reference to the custom drawing canvas
    private DrawingView drawingView;

    // Buttons for drawing tools
    private ImageButton undoButton, redoButton, penButton, eraserButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the XML layout for this fragment
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

        // TODO: Add undo/redo stacks
        // TODO: Add pen/eraser mode switching
        // TODO: Add saving/loading drawing bitmap
    }

    // Expose the drawing as a Bitmap for saving to Room
    public Bitmap getDrawingBitmap() {
        return drawingView.getBitmap();
    }
}