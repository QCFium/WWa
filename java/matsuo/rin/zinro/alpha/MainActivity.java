package matsuo.rin.zinro.alpha;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;


/*
                 void -->                        void -->                          void -->              void -->
    MainActivity          PlayerRegisterActivity          PositionSettingsActivity          GameActivity                     FirstNightActivity
                 <-- void                        <-- void                          <-- void              <-- void

                                                                                                         int judgedPlayerId -->
                                                                                                                                NightActivity
                                                                                                         <-- void

                                                                                                         void -->
                                                                                                                               NoonActivity
                                                                                                         <-- int player_judged


 */

public class MainActivity extends AppCompatActivity {
    // TODO add setting menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button bstart_game = findViewById(R.id.Bstart_game);
        bstart_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PlayerRegisterActivity.class);
                startActivity(intent);
            }
        });

        final Button b_show_about = findViewById(R.id.Bshow_about);
        b_show_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage(R.string.license)
                        .setPositiveButton(getString(R.string.button_ok), null)
                        .show();
            }
        });

        final CheckBox cb_forbid_sv = findViewById(R.id.CBforbid_sv);
        // load config
        Config.load(this);
        cb_forbid_sv.setChecked(Config.forbid_self_voting);

        cb_forbid_sv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Config.forbid_self_voting = isChecked;
                Config.save(MainActivity.this);
            }
        });
    }
}
