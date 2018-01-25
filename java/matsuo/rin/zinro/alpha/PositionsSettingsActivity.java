package matsuo.rin.zinro.alpha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class PositionsSettingsActivity extends AppCompatActivity {
    // TODO: show warning if Reibai > 0 && Zinro <= 1
    // TODO: show warning if Zinro == 0
    // TODO: do not hardcode position list in activity_positions_settings.xml

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_positions_settings);

        final Button b_start_game = findViewById(R.id.Bstart_game);
        b_start_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameState.positions_setting.clear();

                int num = ((SeekBar) findViewById(R.id.SBzinro)).getProgress();
                for (int count = 0; count < num; count++)
                    GameState.positions_setting.add(Zinro.id);

                num = ((SeekBar) findViewById(R.id.SBuranai)).getProgress();
                for (int count = 0; count < num; count++)
                    GameState.positions_setting.add(Uranai.id);

                num = ((SeekBar) findViewById(R.id.SBkishi)).getProgress();
                for (int count = 0; count < num; count++)
                    GameState.positions_setting.add(Kishi.id);

                num = ((SeekBar) findViewById(R.id.SBreibai)).getProgress();
                for (int count = 0; count < num; count++)
                    GameState.positions_setting.add(Reibai.id);

                num = ((SeekBar) findViewById(R.id.SBmurabito)).getProgress();
                for (int count = 0; count < num; count++)
                    GameState.positions_setting.add(Person.id);

                num = ((SeekBar) findViewById(R.id.SBkitsune)).getProgress();
                for (int count = 0; count < num; count++)
                    GameState.positions_setting.add(Fox.id);

                num = ((SeekBar) findViewById(R.id.SBhaitoku)).getProgress();
                for (int count = 0; count < num; count++)
                    GameState.positions_setting.add(Haitoku.id);


                if (GameState.positions_setting.size() != GameState.getPlayerNum()) {
                    Toast.makeText(PositionsSettingsActivity.this, R.string.err_num_no_match, Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(getApplicationContext(), GameActivity.class);
                    finish();
                    startActivity(i);
                }
            }
        });

        final Button b_cancel = findViewById(R.id.Bcancell);
        b_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
