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

public class InputDrawFragment extends Fragment {

    // Reference to the custom drawing canvas
    private DrawingView drawingView;
    private String savedPath;
    private int numOfSavedDrawings;

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
        numOfSavedDrawings = 0;

        // TODO: Add undo/redo stacks
        // TODO: Add pen/eraser mode switching
        // TODO: Add saving/loading drawing bitmap
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

    public void loadBitmap(String savedPath) {
        if (savedPath != null) {
            File imgFile = new File(savedPath);

            if (imgFile.exists()) {
                // Decode the file into a Bitmap
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                // Set it in drawingView
                drawingView.draw(new Canvas(bitmap));
            }
        }

    }

    public Bitmap getDrawingBitmap() {
        return drawingView.getBitmap();
    }

}



