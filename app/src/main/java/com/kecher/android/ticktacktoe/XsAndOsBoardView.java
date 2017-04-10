package com.kecher.android.ticktacktoe;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

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

    private List<GamePiece> placedPieces;

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

    private DisplayMetrics dm;

    public XsAndOsBoardView(Context context) {
        super(context);
        dm = getContext().getResources().getDisplayMetrics();
        init();
    }

    public XsAndOsBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        dm = getContext().getResources().getDisplayMetrics();
        init();
    }

    public XsAndOsBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dm = getContext().getResources().getDisplayMetrics();
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
        crossPaint.setStrokeWidth(15); // TODO replace with dp
        crossPaint.setStyle(Paint.Style.STROKE);

        naughtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        naughtPaint.setColor(res.getColor(R.color.naughtColor));
        naughtPaint.setStrokeWidth(15); // TODO replace with dp
        naughtPaint.setStyle(Paint.Style.STROKE);

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

        leftVertHash = new Rect(leftStroke - pxToDp(10), hashTop, leftStroke + pxToDp(10), hashBottom);
        rightVertHash = new Rect(rightStroke - pxToDp(10), hashTop, rightStroke + pxToDp(10), hashBottom);
        topHorizHash = new Rect(hashLeft, topStroke - pxToDp(10), hashRight, topStroke + pxToDp(10));
        botHorizHash = new Rect(hashLeft, bottomStroke - pxToDp(10), hashRight, bottomStroke + pxToDp(10));

        if (placedPieces == null) {
            placedPieces = new ArrayList<>();
        }
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
        for (GamePiece piece : placedPieces) {
            canvas.drawPath(piece.getPath(), piece.getType() == GamePiece.X_PIECE ? crossPaint : naughtPaint);
        }

        // draw game win.
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
                @GamePiece.PieceType int type = gridCallback.gridClicked(gridX, gridY);
                placedPieces.add(new GamePiece(generatePath(gridX, gridY, type), type));
            }
        }

        invalidate();
        return true;
    }

    private Path generatePath(int gridX, int gridY, @GamePiece.PieceType int type) {
        // TODO calculate width and height of grid cells
        int gridHeight = (hashBottom - hashTop) / 3;
        int gridWidth = (hashRight - hashLeft) / 3;
        // TODO calculate Left start point.
        int leftStart = hashLeft + (gridX * gridWidth);
        // TODO calculate top start point.
        int topStart = hashTop + (gridY * gridHeight);

        Path path = new Path();
        if (type == GamePiece.X_PIECE) {
            path.moveTo(leftStart + 20, topStart + 20);
            path.lineTo(leftStart + gridWidth - 20, topStart + gridHeight - 20);
            path.moveTo(leftStart + 20, topStart + gridHeight - 20);
            path.lineTo(leftStart + gridWidth - 20, topStart + 20);
        } else {
            path.addCircle(leftStart + gridWidth/2, topStart + gridHeight/2, gridWidth/2 - 20, Path.Direction.CW);
        }
        path.close();

        return path;
    }

    private int pxToDp(int px) {
        Log.d(TAG, "px: " + px + " density: " + dm.density);
        return (int) ((px/dm.density)+0.5);
    }
}
