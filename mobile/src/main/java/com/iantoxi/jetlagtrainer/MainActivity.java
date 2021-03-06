package com.iantoxi.jetlagtrainer;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/** Main activity for the app, shows the home screen. */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Slide slide = new Slide();
        slide.excludeTarget(android.R.id.statusBarBackground, true);
        slide.excludeTarget(android.R.id.navigationBarBackground, true);

        getWindow().setEnterTransition(slide);
        getWindow().setExitTransition(slide);
        getWindow().setSharedElementEnterTransition(slide);
        getWindow().setSharedElementExitTransition(slide);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Schedule> values = Schedule.find(Schedule.class, "active = ? AND calculated = ?", "1", "1");
        if (values.size() != 0) {
            final long scheduleId = values.get(0).getId();
            TextView startText = (TextView) findViewById(R.id.startButton);
            startText.setText(getString(R.string.existing_sleep));

            final Button button = (Button) findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    launchExistingSchedule(scheduleId);
                }
            });
        } else {
            TextView startText = (TextView) findViewById(R.id.startButton);
            startText.setText(getString(R.string.main_sleep));

            final Button button = (Button) findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    launchNewSleepShiftInput(v);
                }
            });
        }
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

    public void launchNewSleepShiftInput(View view) {
        Intent intent = new Intent(this, InputLocationActivity.class);
        String transitionName = getString(R.string.transition_main_input);

        View graphic = findViewById(R.id.sleep_training_graphic);

        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        graphic,   // The view which starts the transition
                        transitionName    // The transitionName of the view we’re transitioning to
                );
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    public void launchHistory(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void launchExistingSchedule(long scheduleId) {
        Intent intent = new Intent(this, ScheduleActivity.class);
        intent.putExtra("scheduleId", scheduleId);
        intent.putExtra("placeholder", "normal");
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

}
