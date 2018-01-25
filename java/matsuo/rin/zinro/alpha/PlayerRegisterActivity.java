package matsuo.rin.zinro.alpha;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlayerRegisterActivity extends AppCompatActivity {
    // TODO: allow saving player list
    // TODO: add confirm prompt on starting a game
    // TODO: forbid adding the same name as another
    // TODO: implement cancel button processing
    // PLANNED: allow editing player name

    private static final int MAX_PLAYER_NUM = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_register);

        final PlayerRegisterAdapter adapter = new PlayerRegisterAdapter(this);
        final ListView listView = findViewById(R.id.LVplayers);
        listView.setAdapter(adapter);

        GameState.init();

        // start a game
        final Button b_start_game = findViewById(R.id.Bstart_game);
        b_start_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check
                if (GameState.playerList.size() > 0) {
                    Intent i = new Intent(PlayerRegisterActivity.this, PositionsSettingsActivity.class);
                    finish();
                    startActivity(i);
                }
            }
        });

        final Button b_add_player = findViewById(R.id.Badd_player);
        b_add_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check max # of players
                if (adapter.getCount() >= MAX_PLAYER_NUM) {
                    Toast.makeText(PlayerRegisterActivity.this, R.string.err_no_more_player, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Show a prompt to enter the name
                final EditText et_new_player = new EditText(PlayerRegisterActivity.this);
                et_new_player.setInputType(InputType.TYPE_CLASS_TEXT);
                final AlertDialog d = new AlertDialog.Builder(PlayerRegisterActivity.this)
                    .setMessage(R.string.dialog_new_player)
                    .setView(et_new_player)
                    .setPositiveButton(R.string.button_ok, null)
                    .show();
                d.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String new_player = et_new_player.getText().toString();
                        // detect illegal name
                        if (new_player.contains("\n") || new_player.trim().equals("")) { // contains linebreak or is empty
                            Toast.makeText(PlayerRegisterActivity.this, R.string.err_illegal_name, Toast.LENGTH_SHORT).show();
                        } else {
                            // Update the ListView
                            GameState.playerList.add(new Player(new_player));
                            d.dismiss();
                        }
                    }
                });
            }
        });
    }
}
