package com.example.eecs4443project.view.fragments.journal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.eecs4443project.R;

import java.io.File;
import java.io.FileOutputStream;

// Used for hand-drawn journal entries
public class InputDrawFragment extends Fragment {
    // Custom view where user draws
    private DrawingView drawingView;
    // Path to existing drawing (for Edit mode)
    private String loadedDrawingPath = null;

    // Inflate layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_draw, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        drawingView = view.findViewById(R.id.drawingCanvas);

        // If editing an entry with an existing drawing, load it
        if (loadedDrawingPath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(loadedDrawingPath);
            if (bitmap != null) {
                drawingView.loadBitmap(bitmap);
            }
        }
    }

    // Load existing drawn image
    public void loadBitmap(String path) {
        this.loadedDrawingPath = path;

        if (drawingView != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            if (bitmap != null) {
                drawingView.loadBitmap(bitmap);
            }
        }
    }

    // Save drawing to internal storage
    public String saveDrawingToInternalStorage(Context context) {
        Bitmap bitmap = drawingView.getBitmap();
        String filename = "drawing_" + System.currentTimeMillis() + ".png";

        File file = new File(context.getFilesDir(), filename);

        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}