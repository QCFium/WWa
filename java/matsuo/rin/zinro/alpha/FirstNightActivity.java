package matsuo.rin.zinro.alpha;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FirstNightActivity extends AppCompatActivity {
    /*
        Extras :
        String cur_player_name // user's name
        int position_id // the role's id(role is already decided)
    */

    boolean isRandomizing = true;
    int position_id_r = 0; // for randomization(dummy)
    int position_id = 0; // from GameActivity(actual)
    private String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_night);

        final TextView tv_position = findViewById(R.id.TVposition);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isRandomizing) {
                    // the TextView is tapped -> stop randomizing and set the position
                    user_name = getIntent().getStringExtra("cur_player_name");
                    position_id = getIntent().getIntExtra("position_id", -1);
                    tv_position.setText(WW_Positions.WW_Positions[position_id]);
                    finish();
                }

                // update position to the next position(this looked randomly)
                tv_position.setText(WW_Positions.WW_Positions[position_id_r]);
                if (++position_id_r >= WW_Positions.positions_count) position_id_r = 0;

                handler.postDelayed(this, 100);
            }
        }, 0);
    }

    public void onClick_start_rand (View v) {
        isRandomizing = false;
    }
}
