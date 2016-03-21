package com.habit.devhabit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final int FROM_MAIN_ACTIVITY = 1;

    TargetController mController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Hello Snackbar", Snackbar.LENGTH_LONG).show();
                Intent i = new Intent();
                i.setClass(MainActivity.this, InputTargetActivity.class);

                startActivityForResult(i, FROM_MAIN_ACTIVITY);
            }
        });

        mController = new TargetController(this);

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

    @Override
    protected void onActivityResult(int requestedCode, int resultCode, Intent data) {
        super.onActivityResult(requestedCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            return;
        }

        switch (requestedCode) {
            case FROM_MAIN_ACTIVITY:
                Bundle b = data.getExtras();
                Item item = (Item) b.getSerializable("item");
                ItemDAO itemDAO = new ItemDAO(this);
                itemDAO.insert(item);
                mController.addHabit();
                break;
            default:
                break;
        }
        return;
    }
}
