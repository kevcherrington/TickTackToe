package com.kecher.android.ticktacktoe;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private boolean isXTurn = true;

    private Button.OnClickListener buttonClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        registerTicTacToeCells();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "New game not yet implemented.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        isXTurn = !isXTurn;
        b.setEnabled(false);
    }

//    private void registerTicTacToeCells() {
//        final Resources res = getResources();
//        buttonClickListener = new Button.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Button b = (Button) v;
//                if (isXTurn) {
//                    b.setText(getString(R.string.x_selected));
//                    b.setTextColor(res.getColor(R.color.xSelected));
//                } else {
//                    b.setText(getString(R.string.o_selected));
//                    b.setTextColor(res.getColor(R.color.oSelected));
//                }
//                isXTurn = !isXTurn;
//                b.setEnabled(false);
//            }
//        };
//         ugh... this is tedious. I wish there was a better way to iterate through the buttons... or is there?
//        ((Button) findViewById(R.id.cell_1x1)).setOnClickListener(buttonClickListener);
//        ((Button) findViewById(R.id.cell_1x2)).setOnClickListener(buttonClickListener);
//        ((Button) findViewById(R.id.cell_1x3)).setOnClickListener(buttonClickListener);
//        ((Button) findViewById(R.id.cell_2x1)).setOnClickListener(buttonClickListener);
//        ((Button) findViewById(R.id.cell_2x2)).setOnClickListener(buttonClickListener);
//        ((Button) findViewById(R.id.cell_2x3)).setOnClickListener(buttonClickListener);
//        ((Button) findViewById(R.id.cell_3x1)).setOnClickListener(buttonClickListener);
//        ((Button) findViewById(R.id.cell_3x2)).setOnClickListener(buttonClickListener);
//        ((Button) findViewById(R.id.cell_3x3)).setOnClickListener(buttonClickListener);
//    }

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
