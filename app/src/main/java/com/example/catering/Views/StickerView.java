package com.example.catering.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class StickerView extends View {
    private List<Bitmap> stickersList;
    private List<PointF> stickerPositions;
    private int activeStickerIndex = -1;

    public StickerView(Context context) {
        super(context);
        init();
    }

    public StickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        stickersList = new ArrayList<>();
        stickerPositions = new ArrayList<>();
    }

    public List<Bitmap> getStickersList(){
        return this.stickersList;
    }

    public List<PointF> getStickerPositions() {
        return stickerPositions;
    }

    public void addSticker(Bitmap bitmap) {
        stickersList.add(bitmap);
        stickerPositions.add(new PointF());
        invalidate();
    }

    public void deleteSticker() {
        stickersList.remove(stickersList.size() - 1);
        stickerPositions.remove(stickerPositions.size() - 1);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < stickersList.size(); i++) {
            Bitmap sticker = stickersList.get(i);
            PointF position = stickerPositions.get(i);
            canvas.drawBitmap(sticker, position.x, position.y, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                activeStickerIndex = findActiveSticker(x, y);
                if (activeStickerIndex != -1) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (activeStickerIndex != -1) {
                    stickerPositions.get(activeStickerIndex).set(x, y);
                    invalidate();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                activeStickerIndex = -1;
                break;
        }

        return super.onTouchEvent(event);
    }

    private int findActiveSticker(float x, float y) {
        for (int i = stickersList.size() - 1; i >= 0; i--) {
            PointF position = stickerPositions.get(i);
            Bitmap sticker = stickersList.get(i);
            if (x >= position.x && x < position.x + sticker.getWidth() &&
                    y >= position.y && y < position.y + sticker.getHeight()) {
                return i;
            }
        }
        return -1;
    }
}
