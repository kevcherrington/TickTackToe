package com.kecher.android.ticktacktoe;

import android.util.DisplayMetrics;

/**
 * Created by kev on 12/19/16.
 */

public interface GridCallback {
    @GamePiece.PieceType int gridClicked(int x, int y);
}
