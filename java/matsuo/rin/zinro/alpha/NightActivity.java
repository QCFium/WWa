package matsuo.rin.zinro.alpha;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import static matsuo.rin.zinro.alpha.GameState.*;

public class NightActivity extends AppCompatActivity {
    /*
        Arguments :
        int judgedPlayerId; // id of player killed last noon
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_night);

        final int judgedPlayerId = getIntent().getIntExtra("judgedPlayerId", -2);
        if (judgedPlayerId == -2)
            Error.error(this, "NightActivity#onCreate : failed to get extras");

        // show the current date
        final TextView tvDate = findViewById(R.id.TVdate);
        final String addstr = ((days / 10 == 1) ? "st" : (days / 10 == 2) ? "nd" : (days / 10 == 3) ? "rd" : "th");
        tvDate.setText(getString(R.string.night_count, addstr, Integer.toString(days)));

        // show the current player name
        final TextView tvPlayerName = findViewById(R.id.TVplayer_name);
        tvPlayerName.setText(getString(R.string.turn_msg, playerList.get(curPlayerId).name));

        // show the current player's position
        final TextView tvPlayerPos = findViewById(R.id.TVplayer_position);
        tvPlayerPos.setText(getString(R.string.position_msg, playerList.get(curPlayerId).position.getName(this)));

        // initialize ListView
        final ListView lvPlayers = findViewById(R.id.LVplayers);
        final NightAdapter adapter = new NightAdapter(this);
        adapter.setCurPlayer(curPlayerId);
        lvPlayers.setAdapter(adapter);

        lvPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int selected, long id) {
                String confirmMessage = playerList.get(curPlayerId).position.nightConfirmStr;

                // TODO: allow to see the result in doubting
                if ((playerList.get(selected).position.abilities & Abilities.KNOW_DEAD) != 0) {
                    new android.app.AlertDialog.Builder(NightActivity.this)
                            .setMessage(getString(R.string.reibai_result, playerList.get(judgedPlayerId).name,
                                    SideIds.getDivineResult(NightActivity.this, playerList.get(judgedPlayerId).position.sideId)))
                            .setPositiveButton(getString(R.string.button_ok), null)
                            .show();
                }
                if (confirmMessage == null) return; // do nothing if the position doesn't need any special operation
                new android.app.AlertDialog.Builder(NightActivity.this)
                        .setMessage(confirmMessage)
                        .setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int i) {
                                // We need to do most actual process after all user selection finished
                                // ex : we don't know if a person being killed by a Zinro is protected by a Kishi
                                playerList.get(curPlayerId).lastSelectedPlayerId = selected;
                                int curPlayerAbility = playerList.get(curPlayerId).position.abilities;
                                if ((curPlayerAbility & Abilities.DIVINE) != 0) {
                                    new android.app.AlertDialog.Builder(NightActivity.this)
                                            .setMessage(getString(R.string.divine_result, playerList.get(selected).name,
                                                    SideIds.getDivineResult(NightActivity.this, playerList.get(selected).position.sideId_divined)))
                                            .setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    setResult(RESULT_OK);
                                                    finish();
                                                }
                                            })
                                            .show();

                                    // kill fox unconditionally
                                    if (playerList.get(selected).position.id == Fox.id) playerList.get(selected).kill(true);
                                } else {
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton(getString(R.string.button_cancel), null)
                        .show();
            }
        });
    }
}

