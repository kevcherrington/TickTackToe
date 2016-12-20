package com.kecher.android.ticktacktoe;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class XsAndOsBoardView extends View {
    public static final String TAG = "PrettyView";

    private Context context;
    private Paint bgPaint;
    private Paint hashPaint;
    private Paint crossPaint;
    private Paint naughtPaint;

    private LinearGradient bgGradient;

    private Rect leftVertHash;
    private Rect rightVertHash;
    private Rect topHorizHash;
    private Rect botHorizHash;

    int hashLeft;
    int hashRight;
    int hashTop;
    int hashBottom;
    int leftStroke;
    int rightStroke;
    int topStroke;
    int bottomStroke;
    int vertOffset;
    int horizOffset;

    private int height;
    private int width;

    private GridCallback gridCallback;

    public XsAndOsBoardView(Context context) {
        super(context);
        init();
    }

    public XsAndOsBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public XsAndOsBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Resources res = getResources();
        bgGradient = new LinearGradient(0,0,0,height, res.getColor(R.color.bg_top), res.getColor(R.color.bg_bottom), Shader.TileMode.MIRROR);
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setShader(bgGradient);

        hashPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        hashPaint.setColor(res.getColor(R.color.hash));

        crossPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        crossPaint.setColor(res.getColor(R.color.crossColor));

        naughtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        naughtPaint.setColor(res.getColor(R.color.naughtColor));

        vertOffset = height < width ? 0 : (height - width) / 2;
        horizOffset = width < height ? 0 : (width - height) / 2;
        Log.d(TAG, "init: vo:" + vertOffset + " ho:" + horizOffset);

        // height = 9/11 of viewport
        // width = 9/11 of viewport
        int shortSide = width < height ? width : height;
        hashLeft = shortSide/11 + horizOffset;
        hashRight = (shortSide/11)*10 + horizOffset;
        hashTop = shortSide/11 + vertOffset;
        hashBottom = (shortSide/11)*10 + vertOffset;

        leftStroke = hashLeft + (hashRight-hashLeft) / 3;
        rightStroke = hashLeft + ((hashRight-hashLeft) / 3) * 2;
        topStroke = hashTop + (hashBottom-hashTop) / 3;
        bottomStroke = hashTop + ((hashBottom-hashTop) / 3) * 2;

        Log.d(TAG, "init: width:" + width +
                " height:" + height +
                " hl:" + hashLeft +
                " hr:" + hashRight +
                " ht:" + hashTop +
                " hb:" + hashBottom +
                " ls:" + leftStroke +
                " rs:" + rightStroke +
                " ts:" + topStroke +
                " bs:" + bottomStroke);

        leftVertHash = new Rect(leftStroke - 10, hashTop, leftStroke + 10, hashBottom);
        rightVertHash = new Rect(rightStroke - 10, hashTop, rightStroke + 10, hashBottom);
        topHorizHash = new Rect(hashLeft, topStroke - 10, hashRight, topStroke + 10);
        botHorizHash = new Rect(hashLeft, bottomStroke - 10, hashRight, bottomStroke + 10);

    }

    public void setGridGridCallback(GridCallback gridCallback) {
        this.gridCallback = gridCallback;
    }
    @Override
    protected void onMeasure(int wMeasureSpec, int hMeasureSpec) {
        int hSpecMode = MeasureSpec.getMode(hMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(hMeasureSpec);
        int myHeight = hSpecSize;

        int wSpecMode = MeasureSpec.getMode(wMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(wMeasureSpec);
        int myWidth = wSpecSize;

        if (hSpecMode == MeasureSpec.EXACTLY) {
            myHeight = hSpecSize;
        } else if (hSpecMode == MeasureSpec.AT_MOST) {
            // Wrap Content
        }

        if (wSpecMode == MeasureSpec.EXACTLY) {
            myWidth = wSpecSize;
        } else if (wSpecMode == MeasureSpec.AT_MOST) {
            // Wrap Content
        }

        height = myHeight;
        width = myWidth;
        init();
        setMeasuredDimension(myWidth, myHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw the background
        canvas.drawPaint(bgPaint);

        // draw the hash
        canvas.drawRect(leftVertHash, hashPaint);
        canvas.drawRect(rightVertHash, hashPaint);
        canvas.drawRect(topHorizHash, hashPaint);
        canvas.drawRect(botHorizHash, hashPaint);

        // draw crosses and naughts
        // TODO
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        super.onTouchEvent(e);

        // detect touches within the hash bounds
        if (e.getActionMasked() == MotionEvent.ACTION_UP && e.getPointerCount() < 2) {
            float x = e.getX();
            float y = e.getY();

            if (y >= hashTop && y <= hashBottom
                    && x >= hashLeft && x <= hashRight) {
                int gridX = (int) (x-hashLeft)/((hashRight-hashLeft)/3);
                int gridY = (int) (y-hashTop)/((hashBottom-hashTop)/3);
                Log.d(TAG, "onTouchEvent: Its In!!! X:" + x + " Y:" + y);
                gridCallback.gridClicked(gridX, gridY);
            }
        }

        return true;
    }
}
