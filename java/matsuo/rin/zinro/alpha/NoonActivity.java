package matsuo.rin.zinro.alpha;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import static matsuo.rin.zinro.alpha.GameState.*;

public class NoonActivity extends AppCompatActivity {
    /*
        Result :
        int player_judged; // judged player
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noon);

        final String add_str = ((days % 10) == 1) ? "st" : ((days % 10) == 2) ? "nd" : ((days % 10) == 3) ? "rd" : "th" ;

        final TextView tv_desc = findViewById(R.id.TVdesc);
        tv_desc.setText(getString(R.string.noon_count, add_str, Integer.toString(days)));


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            final TextView tv_timer = findViewById(R.id.TVtimer);
            int sec = 120;
            @Override
            public void run() {
                handler.postDelayed(this, 1000);

                if (--sec <= 0) {
                    Intent i = new Intent(getApplicationContext(), NoonJudgementActivity.class);
                    startActivityForResult(i, 0);
                    handler.removeCallbacksAndMessages(null);
                }
                tv_timer.setText(getString(R.string.noon_timer_format, (sec / 60), (sec % 60)));
            }

        }, 0);

        // last night deaths information
        final TextView tvDeathInformation = findViewById(R.id.TVdeath_information);
        StringBuilder buffer = new StringBuilder();
        for (int curProcessingPlayerId = 0; curProcessingPlayerId < getPlayerNum(); curProcessingPlayerId++) {
            if (playerList.get(curProcessingPlayerId).killedLast) {
                playerList.get(curProcessingPlayerId).killedLast = false;
                if (playerList.get(curProcessingPlayerId).followed_fox) {
                    buffer.append(getString(R.string.died_follow_fox, playerList.get(curProcessingPlayerId).name));
                } else {
                    buffer.append(getString(R.string.died_killed, playerList.get(curProcessingPlayerId).name));
                }
            }
        }
        String msg = buffer.toString();
        if (msg.isEmpty()) msg = getString(R.string.died_none);
        tvDeathInformation.setText(msg);


        findViewById(R.id.Bfinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacksAndMessages(null);
                Intent i = new Intent(getApplicationContext(), NoonJudgementActivity.class);
                startActivityForResult(i, 0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int judged = data.getIntExtra("player_judged", -1);

        Intent i = new Intent();
        i.putExtra("player_judged", judged);
        setResult(resultCode, i);

        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.finish_confirm))
                    .setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.putExtra("player_judged", -1);
                            setResult(RESULT_CANCELED, intent);
                            finish();
                        }
                    })
                    .setNegativeButton(getString(R.string.button_cancel), null)
                    .show();
        }
        return super.onKeyDown(keyCode, event);
    }
}
