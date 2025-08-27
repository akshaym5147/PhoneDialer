package com.example.phonedialer.helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import java.util.Random;

public class InitialsDrawable extends Drawable {
    private final Paint textPaint;
    private final Paint backgroundPaint;
    private final String initials;

    public InitialsDrawable(String name) {
        // Take initials
        String[] parts = name.trim().split("\\s+");
        String first = parts.length > 0 ? parts[0].substring(0, 1) : "";
        String second = parts.length > 1 ? parts[1].substring(0, 1) : "";
        initials = (first + second).toUpperCase();


        int bgColor = getRandomColor();

        // Decide text color based on background (contrast check)
        int textColor = isColorDark(bgColor) ? Color.WHITE : Color.BLACK;

        // Text paint
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(48f);

        // Background paint with random color
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(bgColor);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        float cx = bounds.exactCenterX();
        float cy = bounds.exactCenterY();

        // Draw circle background
        float radius = Math.min(bounds.width(), bounds.height()) / 2f;
        canvas.drawCircle(cx, cy, radius, backgroundPaint);

        // Draw initials in center
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        float textY = cy - (fm.ascent + fm.descent) / 2;
        canvas.drawText(initials, cx, textY, textPaint);
    }

    @Override public void setAlpha(int alpha) { textPaint.setAlpha(alpha); backgroundPaint.setAlpha(alpha); }
    @Override public void setColorFilter(ColorFilter colorFilter) { textPaint.setColorFilter(colorFilter); backgroundPaint.setColorFilter(colorFilter); }
    @Override public int getOpacity() { return PixelFormat.TRANSLUCENT; }

    private int getRandomColor() {
        Random random = new Random();
        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    private boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color)
                + 0.587 * Color.green(color)
                + 0.114 * Color.blue(color)) / 255;
        return darkness >= 0.5; // true if dark
    }

}
