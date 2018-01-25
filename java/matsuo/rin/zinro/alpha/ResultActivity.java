package matsuo.rin.zinro.alpha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        final Button bFinish = findViewById(R.id.Bfinish);
        bFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameState.init(); // reset everything

                Intent i = new Intent();
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                setResult(RESULT_OK, i);
                finish();
            }
        });

        Intent intent = getIntent();
        final String won_str = intent.getStringExtra("won_str");
        if (won_str.isEmpty()) Error.error(this, "ResultActivity#onCreate() : failed to get extras");

        String msg = getString(R.string.result, won_str);
        msg += getString(R.string.survived);
        for (Player player : GameState.playerList) {
            if (player.isAlive) msg +=
                    (player.name + "\n");
        }
        msg += "\n\n";

        msg += getString(R.string.position_result);
        // TODO: alignment position to right
        StringBuilder buffer = new StringBuilder();
        for (Player player : GameState.playerList) {
            buffer.append(player.name).append(" ").append(player.position.getName(this)).append('\n');
        }
        msg += buffer.toString();

        final TextView tvResult = findViewById(R.id.TVresult);
        tvResult.setText(msg);
    }
}
