package com.nnenkov.mymusicplayer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by nik on 09.09.16.
 */

public class RecycleViewCustomDecoration extends RecyclerView.ItemDecoration {

    Paint bluePaint, redPaint, downloadColorPaint, explicitPaint, explicitBackgroundPaint;
    int offset;

    public RecycleViewCustomDecoration() {
        offset = 10;

        bluePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        downloadColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        explicitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        explicitBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        redPaint.setColor(Color.RED);
        bluePaint.setColor(Color.BLUE);
        downloadColorPaint.setColor(Color.rgb(61, 132, 54));
        explicitPaint.setColor(Color.rgb(54, 31, 12));
        //explicitPaint.setColor(Color.rgb(54,255,255));
        explicitPaint.setTextSize(30);

        explicitBackgroundPaint.setColor(Color.rgb(115, 92, 77));

        redPaint.setStyle(Paint.Style.FILL);
        bluePaint.setStyle(Paint.Style.FILL);
        downloadColorPaint.setStyle(Paint.Style.STROKE);
        explicitPaint.setStyle(Paint.Style.STROKE);
        explicitBackgroundPaint.setStyle(Paint.Style.FILL);
        //downloadColorPaint.setAlpha();

        redPaint.setStrokeWidth(1f);
        bluePaint.setStrokeWidth(1f);
        downloadColorPaint.setStrokeWidth(5);
        explicitPaint.setStrokeWidth(3);
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.set(offset, offset, offset, offset);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        for (int i = 0; i < parent.getChildCount(); i++) {

            final View child = parent.getChildAt(i);
            if ((parent.getChildAdapterPosition(child) & 1) == 0) {
                // even On every even item add decoration with green download icon
/*                c.drawRect(
                        layoutManager.getDecoratedLeft(child),
                        layoutManager.getDecoratedTop(child),
                        layoutManager.getDecoratedRight(child),
                        layoutManager.getDecoratedBottom(child),
                        bluePaint);*/
                //c.drawCircle(layoutManager.getDecoratedLeft(child),layoutManager.getDecoratedTop(child),30,downloadColorPaint);
                c.drawCircle(layoutManager.getDecoratedLeft(child) + offset + 35, layoutManager.getDecoratedTop(child) + offset - 105, 20, downloadColorPaint);
                //Steblo
                c.drawLine(
                        layoutManager.getDecoratedLeft(child) + offset + 35, layoutManager.getDecoratedTop(child) + offset - 117,
                        layoutManager.getDecoratedLeft(child) + offset + 35, layoutManager.getDecoratedTop(child) + offset - 93,
                        downloadColorPaint);
                // lqvo na strelkata
                c.drawLine(
                        layoutManager.getDecoratedLeft(child) + offset + 23, layoutManager.getDecoratedTop(child) + offset - 103,
                        layoutManager.getDecoratedLeft(child) + offset + 35, layoutManager.getDecoratedTop(child) + offset - 93,
                        downloadColorPaint);
                //dqsno na strelkata
                c.drawLine(
                        layoutManager.getDecoratedLeft(child) + offset + 47, layoutManager.getDecoratedTop(child) + offset - 103,
                        layoutManager.getDecoratedLeft(child) + offset + 35, layoutManager.getDecoratedTop(child) + offset - 93,
                        downloadColorPaint);
                // linq otzad
                c.drawLine(
                        layoutManager.getDecoratedLeft(child) + offset + 24, layoutManager.getDecoratedTop(child) + offset - 101,
                        layoutManager.getDecoratedLeft(child) + offset + 46, layoutManager.getDecoratedTop(child) + offset - 101,
                        downloadColorPaint);
            } else {
                // odd On every odd item add decoration with explicit tag as explained in the video
/*                c.drawRect(
                        layoutManager.getDecoratedLeft(child) + offset,
                        layoutManager.getDecoratedTop(child) + offset,
                        layoutManager.getDecoratedRight(child) - offset,
                        layoutManager.getDecoratedBottom(child) - offset,
                        redPaint);*/
               /* c.drawRoundRect(
                        layoutManager.getDecoratedLeft(child)+ offset+10,
                        layoutManager.getDecoratedTop(child)+ offset-102,layoutManager.getDecoratedRight(child) - offset,
                        layoutManager.getDecoratedBottom(child) - offset,
                        1,2,
                        explicitBackgroundPaint);*/
                c.drawRoundRect(new RectF(layoutManager.getDecoratedLeft(child) + offset - 2,
                        layoutManager.getDecoratedTop(child) + offset - 132,
                        layoutManager.getDecoratedLeft(child) + offset + 144,
                        layoutManager.getDecoratedTop(child) + offset - 129 + 38), 10, 10, explicitBackgroundPaint);
                c.drawText("EXPLICIT", layoutManager.getDecoratedLeft(child) + offset + 10, layoutManager.getDecoratedTop(child) + offset - 102, explicitPaint);

            }
/*
            explicitBackgroundPaint.setStrokeWidth(10);
            c.drawPoint(layoutManager.getDecoratedRight(child) - offset - 10,layoutManager.getDecoratedTop(child) + offset + 75,explicitBackgroundPaint);
            c.drawPoint(layoutManager.getDecoratedRight(child) - offset - 10,layoutManager.getDecoratedTop(child) + offset + 100,explicitBackgroundPaint);
            c.drawPoint(layoutManager.getDecoratedRight(child) - offset - 10,layoutManager.getDecoratedTop(child) + offset + 125,explicitBackgroundPaint);
*/
            if (MainActivity.songsList.get(parent.getChildAdapterPosition(child)).getPlayNow()){
                explicitBackgroundPaint.setStrokeWidth(5);
                c.drawLine(layoutManager.getDecoratedLeft(child) + offset,
                        layoutManager.getDecoratedBottom(child)+ offset,
                        layoutManager.getDecoratedRight(child) + offset,
                        layoutManager.getDecoratedBottom(child)+ offset,
                        explicitBackgroundPaint);
                explicitBackgroundPaint.setStrokeWidth(10);

            }

        }



    }
}
