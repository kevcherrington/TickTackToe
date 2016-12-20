package com.kecher.android.ticktacktoe;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Main Activity";
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({GAME_START, X_TURN, O_TURN, X_WINS, O_WINS})
    public @interface BannerMessageFlag {}
    public static final int GAME_START = 0;
    public static final int X_TURN = 1;
    public static final int O_TURN = 2;
    public static final int X_WINS = 3;
    public static final int O_WINS = 4;

    private boolean isXTurn = true;
    private int[][] grid = new int[3][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Reset game is now implemented. Keeping this for future reference
//                Snackbar.make(view, "New game not yet implemented.", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                resetGame();
            }
        });

        XsAndOsBoardView custView = (XsAndOsBoardView) findViewById(R.id.custom_view);
        custView.setGridGridCallback(new GridCallback() {
            public void gridClicked(int x, int y) {
                gridSelected(x, y);
            }
        });
    }

    private void gridSelected (int x, int y) {
        Log.d(TAG, "gridSelected: Boom!!! X:" + x + " Y:" + y);
    }

    public void buttonClicked(View v) {
        Resources res = getResources();
        Button b = (Button) v;
        if (isXTurn) {
            b.setText(getString(R.string.x_selected));
            b.setTextColor(res.getColor(R.color.xSelected));
        } else {
            b.setText(getString(R.string.o_selected));
            b.setTextColor(res.getColor(R.color.oSelected));
        }
        updateGrid((String) b.getTag(), grid, isXTurn);
        isXTurn = !isXTurn;
        b.setEnabled(false);

        if (xWins()) {
            updateBanner(X_WINS);
            disableAllButtons();
        } else if (yWins()) {
            updateBanner(O_WINS);
            disableAllButtons();
        } else {
            updateBanner(isXTurn? X_TURN:O_TURN);
        }
    }

    private void updateGrid(String tag, int[][] grid, boolean turn) {
        Integer x = Integer.parseInt(tag.substring(0,1));
        Integer y = Integer.parseInt(tag.substring(2,3));
        grid[x-1][y-1] = turn ? 1 : 2;
    }

    private void disableAllButtons() {
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                String buttonId = "cell_" + i + "x" + j;
                int resId = getResources().getIdentifier(buttonId, "id", getPackageName());
                findViewById(resId).setEnabled(false);
            }
        }
    }

    private void updateBanner(@BannerMessageFlag int messageFlag) {
        TextView notifications = (TextView) findViewById(R.id.game_master_notification);
        switch (messageFlag) {
            case GAME_START:
                notifications.setText(R.string.tic_tac_toe_welcome);
                break;
            case X_TURN:
                notifications.setText(R.string.turn_x);
                break;
            case O_TURN:
                notifications.setText(R.string.turn_o);
                break;
            case X_WINS:
                notifications.setText(R.string.win_x);
                break;
            case O_WINS:
                notifications.setText(R.string.win_o);
                break;
            default:
                notifications.setText(R.string.tic_tac_toe_welcome);
                break;
        }
    }

    private boolean yWins() {
//        for (int[] row : grid) {
//            Log.d(TAG, "[" + row[0] + "," + row[1] + "," + row[2] + "]");
//        }
//        Log.d(TAG, "---------");
        for (int i = 0; i < grid[0].length; i++) {
            // check row
            if (grid[i][0] == 2 && grid[i][1] == 2 && grid[i][2] == 2) {
                return true;
            }
            // check col
            if (grid[0][i] == 2 && grid[1][i] == 2 && grid[2][i] == 2) {
                return true;
            }
        }
        // check diagonals
        if (grid[0][0] == 2 && grid[1][1] == 2 && grid[2][2] == 2) {
            return true;
        } else if (grid[0][2] == 2 && grid[1][1] == 2 && grid[2][0] == 2) {
            return true;
        }
        return false;
    }

    private boolean xWins() {
        for (int i = 0; i < grid[0].length; i++) {
            // check row
            if (grid[i][0] == 1 && grid[i][1] == 1 && grid[i][2] == 1) {
                return true;
            }
            // check col
            if (grid[0][i] == 1 && grid[1][i] == 1 && grid[2][i] == 1) {
                return true;
            }
        }
        // check diagonals
        if (grid[0][0] == 1 && grid[1][1] == 1 && grid[2][2] == 1) {
            return true;
        } else if (grid[0][2] == 1 && grid[1][1] == 1 && grid[2][0] == 1) {
            return true;
        }
        return false;
    }

    public void resetGame () {
        // reset turn back to x
        isXTurn = true;
        // reset the game grid.
        grid = new int[3][3];
        // clear buttons and enable them.
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                String buttonId = "cell_" + i + "x" + j;
                int resId = getResources().getIdentifier(buttonId, "id", getPackageName());
                resetButton((Button) findViewById(resId));
            }
        }
        // reset banner.
        updateBanner(GAME_START);
    }

    private void resetButton(Button b) {
        b.setText(getString(R.string.empty_square));
        b.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
