package com.kecher.android.ticktacktoe;

import android.graphics.Path;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by kev on 2/21/17.
 */

public class GamePiece {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({X_PIECE, O_PIECE})
    public @interface PieceType {}
    public static final int X_PIECE = 0;
    public static final int O_PIECE = 1;

    private Path path;
    private @PieceType int type;

    public GamePiece() {
    }

    public GamePiece(Path path, @PieceType int type) {
        this.path = path;
        this.type = type;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public @PieceType int getType() {
        return type;
    }

    public void setType(@PieceType int type) {
        this.type = type;
    }
}
