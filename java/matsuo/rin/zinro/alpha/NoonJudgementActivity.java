package matsuo.rin.zinro.alpha;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import static matsuo.rin.zinro.alpha.GameState.*;

public class NoonJudgementActivity extends AppCompatActivity {
    /*
        Return Extras :
        int judged; // the id of the player who is judged
     */

    // TODO allow to forbid self-posting
    // TODO forbid posting to dead player

    int judge_saves [];
    int cur_player = 0;

    NoonJudgementAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noon_judgement);

        // safety check
        if (getPlayerNum() <= 0) Error.error(this, "NoonJudgementActivity#onCreate : no player detected");

        // initialize judgements counter
        judge_saves = new int[getPlayerNum()];
        Arrays.fill(judge_saves, 0);

        // initialize the ListView
        ListView lv_players = findViewById(R.id.LVplayers);
        adapter = new NoonJudgementAdapter(this);
        lv_players.setAdapter(adapter);

        // set the first player name
        while (cur_player < getPlayerNum() && !playerList.get(cur_player).isAlive) cur_player++;
        if (cur_player >= getPlayerNum()) Error.error(this, "NoonJudgementActivity#onCreate : No alive player");
        adapter.setCurrentPlayer(cur_player);

        final TextView tv_player_name = findViewById(R.id.TVplayer_name);
        tv_player_name.setText(getString(R.string.judge_msg, playerList.get(cur_player).name));

        lv_players.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int p, long id) {
                new AlertDialog.Builder(NoonJudgementActivity.this)
                        .setMessage(getString(R.string.vote_confirm, adapter.getItem(p).name))
                        .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                handleJudge(p);
                            }
                        })
                        .setNegativeButton(R.string.button_cancel, null)
                        .show();
            }
        });
    }

    private void handleJudge (final int p) {
        judge_saves[p]++;
        while(++cur_player < getPlayerNum() && !playerList.get(cur_player).isAlive); // find next alive player
        if (cur_player >= getPlayerNum()) {
            // finish
            // TODO allow re-judgement
            int judge = 0;
            for (int count = 0; count < getPlayerNum(); count++)
                if (judge_saves[judge] < judge_saves[count]) judge = count;

            Intent i = new Intent();
            i.putExtra("player_judged", judge);
            setResult(RESULT_OK, i);
            finish();
        } else {
            // set the next player name
            ((TextView)findViewById(R.id.TVplayer_name)).setText(getString(R.string.judge_msg, playerList.get(cur_player).name));
            adapter.setCurrentPlayer(cur_player);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            new AlertDialog.Builder(this)
                    .setMessage(R.string.finish_confirm)
                    .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            setResult(RESULT_CANCELED, intent);
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.button_cancel, null)
                    .show();
        }
        return super.onKeyDown(keyCode, event);
    }
}