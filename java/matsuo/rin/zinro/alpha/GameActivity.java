package matsuo.rin.zinro.alpha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    ArrayList<String> player_list;
    int players_num;
    int cur_player = -1; // current player id
    boolean is_used_list[]; // used state of each positions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        player_list = getIntent().getStringArrayListExtra("user_list");
        players_num = player_list.size();
        Toast.makeText(this, Integer.toString(players_num), Toast.LENGTH_SHORT).show();

        is_used_list = new boolean[WW_Positions.positions_count];
        Arrays.fill(is_used_list, false);

        // set user name to the TextView
        TextView tvCurUserName = findViewById(R.id.TVuser_name);
        tvCurUserName.setText((player_list.size() == 0) ? "Null" : player_list.get(0));
    }

    public void onClick_enter (View v) {
        if (++cur_player >= players_num) {
            finish();
            while(true) ;
        }

        String curPlayerName = player_list.get(cur_player);
        int position_id;

        // set user name to the TextView
        TextView tvCurUserName = findViewById(R.id.TVuser_name);
        tvCurUserName.setText(curPlayerName);

        // debug
        Toast.makeText(this, curPlayerName, Toast.LENGTH_SHORT).show();

        // get random position(retry if already used)
        Random r = new Random();
        position_id = r.nextInt(WW_Positions.positions_count);
        while (is_used_list[position_id]) position_id = r.nextInt(WW_Positions.positions_count); // retry until position is not used
        is_used_list[position_id] = true;

        // start FirstNightActivity
        Intent i = new Intent(getApplicationContext(), FirstNightActivity.class);
        i.putExtra("cur_player_name", curPlayerName);
        i.putExtra("position_id", position_id);

        startActivity(i);
    }

}
