package matsuo.rin.zinro.alpha;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static matsuo.rin.zinro.alpha.GameState.*;

public class FirstNightActivity extends AppCompatActivity{
    /*
        Warning
        This Activity is just for the random animation
        The position is already decided in GameActivity
     */

    boolean isRandomizing = true;
    int positionIdDummy = 0; // for randomization(dummy)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_night);

        if (curPlayerId < 0) Error.error(this, "FirstNightActivity#onCreate : curPlayerId == -1");

        // set the user name
        ((TextView)findViewById(R.id.TVplayer_name)).setText(playerList.get(curPlayerId).name);

        final TextView tv_position = findViewById(R.id.TVposition);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isRandomizing) {
                    String RealPositionName = playerList.get(curPlayerId).position.getName(FirstNightActivity.this);
                    // the TextView is tapped -> stop randomizing and set the position
                    tv_position.setText(RealPositionName);

                    findViewById(R.id.Bfinish).setVisibility(View.VISIBLE); // enable the finish button
                } else {
                    // update position to the next position(this looks random)
                    tv_position.setText(Position.getNameById(positionIdDummy, FirstNightActivity.this));
                    if (++positionIdDummy >= Positions.num) positionIdDummy = 0;

                    handler.postDelayed(this, 100);
                }
            }
        }, 0);
    }

    public void onClick_stop_rand (View v) {
        isRandomizing = false;
    }

    public void onClick_finish (View v) {
        setResult(RESULT_OK);
        finish();
    }
}
