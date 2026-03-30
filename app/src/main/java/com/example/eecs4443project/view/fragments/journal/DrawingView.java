package com.example.eecs4443project.view.fragments.journal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {
    // Stores current drawing path
    private Path path = new Path();
    // Describes stroke colour/width/style
    private Paint paint = new Paint();
    // Stores finished strokes
    private Bitmap bitmap;
    // Android's drawing framework
    private Canvas canvas;
    // Stores last touch coordinates (to make lines smoother)
    private float lastX, lastY;

    // Constructor
    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // Initialise "paintbrush" properties
    private void init() {
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8f);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    // If view size changes, create a new bitmap to draw on
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    // Draws stored bitmap and current path on the canvas
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawPath(path, paint);
    }

    // Handle touch invents
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            // Detect when user touches the screen
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                lastX = x;
                lastY = y;
                break;
            // Detect when finger moves and draw a curve from last point to current point
            case MotionEvent.ACTION_MOVE:
                path.quadTo(lastX, lastY, (x + lastX) / 2f, (y + lastY) / 2f);
                lastX = x;
                lastY = y;
                break;
            // When user's finger leaves the screen, save the current stroke to bitmap
            case MotionEvent.ACTION_UP:
                canvas.drawPath(path, paint);
                path.reset();
                break;
        }

        // Redraw the view
        invalidate();
        return true;
    }

    // Clear canvas by colouring white over the current content
    public void clear() {
        if (canvas != null) {
            canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_OVER);
            invalidate();
        }
    }

    // Return the final drawing as a Bitmap to be saved
    public Bitmap getBitmap() {
        return bitmap;
    }
}

