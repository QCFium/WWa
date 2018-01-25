package matsuo.rin.zinro.alpha;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

import static matsuo.rin.zinro.alpha.GameState.*;

public class GameActivity extends AppCompatActivity {
    // TODO add confirm prompt on entering each FirstNightActivity

    private Random random = new Random();

    int judgedLastNoonPlayerId = -1; // id of player that killed last noon

    private static class RequestCodes {
        static final int FirstNightActivity = 0;
        static final int NightActivity = 1;
        static final int NoonActivity = 2;
        static final int ResultActivity = 3;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // safety check
        if (positions_setting.size() != getPlayerNum() || getPlayerNum() <= 0)
            Error.error(this, "GameActivity#onCreate : Safety check failed : " + ((getPlayerNum() <= 0) ? "no player" : "player_num not matched"));

        // set the first player name to the TextView
        TextView tvCurplayerName = findViewById(R.id.TVplayer_name);
        tvCurplayerName.setText(playerList.get(0).name); // length is made sure not 0 in playerRegisterActivity so no AListOOBException
    }

    public void onClick_enter (View v) {
        if (days == 0) {
            Player curPlayer = playerList.get(curPlayerId);
            // get actual position of the player(retry if already used)
            int position_id_settings = random.nextInt(getPlayerNum());
            while (isUsedPositionSettingsId(position_id_settings))
                position_id_settings = random.nextInt(getPlayerNum()); // retry until position is not used
            curPlayer.position =
                    Positions.createPositionById(positions_setting.get(position_id_settings));
            curPlayer.position_id_settings = position_id_settings;
            // safety check
            if (playerList.get(curPlayerId).position == null || curPlayer.position == null)
                Error.error(this, "GameActivity#onClick_enter : position null");

            // start FirstNightActivity
            Intent i = new Intent(getApplicationContext(), FirstNightActivity.class);

            startActivityForResult(i, RequestCodes.FirstNightActivity);
        } else if (isNoon) {
            Error.error(this, "GameActivity#onClick_enter : the button pressed in noon(unexpected)");
        } else {
            Intent i = new Intent(getApplicationContext(), NightActivity.class);
            i.putExtra("judgedPlayerId", judgedLastNoonPlayerId);
            startActivityForResult(i, RequestCodes.NightActivity);
        }
    }

    private boolean isUsedPositionSettingsId (int position_id_settings) {
        for (Player p : playerList) {
            if (p.position_id_settings == position_id_settings) return true;
        }
        return false;
    }

    /* Process killing by a Zinro and protection by a Kishi etc.   */
    /* Returns the number of killed person */
    private void processUserSelection() {
        for (Player p : playerList) {
            if (p.isAlive) continue;

            int selectedUserId = p.lastSelectedPlayerId;
            int curAbilities = p.position.abilities;
            if ((curAbilities & Abilities.BITE) != 0)
                playerList.get(selectedUserId).beingKilled = true;
            else if ((curAbilities & (Abilities.DEFEND | Abilities.DEFEND_SELF)) != 0)
                playerList.get(selectedUserId).isProtected = true;
        }

        // process most operations
        for (Player p : playerList) {
            // Zinro killing
            if (p.isAlive && p.beingKilled && !p.isProtected)
                p.kill(true);

            // reset
            p.beingKilled = false;
            p.isProtected = false;
        }
    }

    // tasks on finishing FirstNightActivity and others
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) { // return to MainActivity
            finish();
            while(true);
        }
        if (requestCode == RequestCodes.FirstNightActivity) {
            // set to the next player
            if (++curPlayerId >= getPlayerNum()) {
                curPlayerId = 0;
                days++;
                isNoon = true; // it become noon

                checkWin(this);

                Intent i = new Intent(getApplicationContext(), NoonActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(i, RequestCodes.NoonActivity);
            }
            updatePlayerNameView();
        } else if (requestCode == RequestCodes.NoonActivity) {
            // NoonActivity is once per day
            // get the judged player
            int judged = data.getIntExtra("player_judged", -1);
            if (judged == -1) Error.error(this, "GameActivity#onActivityResult : failed to get return extra(judged)");

            new AlertDialog.Builder(GameActivity.this)
                    .setMessage(getString(R.string.judged, playerList.get(judged).name))
                    .setPositiveButton("OK", null)
                    .show();

            playerList.get(judged).kill(false);
            judgedLastNoonPlayerId = judged;
            isNoon = false;
            checkWin(this);
        } else if (requestCode == RequestCodes.NightActivity) {
            while (++curPlayerId < getPlayerNum() && !playerList.get(++curPlayerId).isAlive); // who's the next alive player...
            if (curPlayerId >= getPlayerNum()) {
                processUserSelection();
                curPlayerId = 0;
                isNoon = true;
                days++;

                checkWin(this);

                // go noon activity straightly
                Intent i = new Intent(this, NoonActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(i, RequestCodes.NoonActivity);
            }
            updatePlayerNameView();
        } else if (requestCode == RequestCodes.ResultActivity) { // finished ResultActivity -> finish application
            Intent i = new Intent();
            i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            setResult(RESULT_OK, i);
            finish();
        } else {
            Error.error(this, "GameActivity#onActivityResult unexpected requestcode");
        }
    }

    private void updatePlayerNameView() {
        // display the next player name
        TextView tvCurplayerName = findViewById(R.id.TVplayer_name);
        tvCurplayerName.setText(playerList.get(curPlayerId).name);
    }

    private static class WinCodes {
        static final int NO_WIN = 0;
        static final int WEREWOLVES_WIN = 1;
        static final int PEOPLE_WIN = 2;
        static final int FOXES_WIN = 3;
    }
    private void checkWin(Context context) {
        int werewolves_alive = 0;
        int people_alive = 0;
        int foxes_alive = 0;

        for (Player p : playerList) {
            if (p.isAlive && p.position.sideId_counted == SideIds.PEOPLE) people_alive++;
            else if (p.isAlive && p.position.sideId_counted == SideIds.WEREWOLVES) werewolves_alive++;
            else if (p.isAlive && p.position.sideId_counted == SideIds.FOXES) foxes_alive++;
        }

        int won;
        if (werewolves_alive == 0) won = WinCodes.PEOPLE_WIN;
        else if (werewolves_alive >= people_alive) won = WinCodes.WEREWOLVES_WIN;
        else won = WinCodes.NO_WIN;

        if (won != WinCodes.NO_WIN && foxes_alive > 0) won = WinCodes.FOXES_WIN;

        String won_str = null;
        if (won == WinCodes.PEOPLE_WIN) won_str = getString(R.string.person);
        else if (won == WinCodes.WEREWOLVES_WIN) won_str = getString(R.string.zinro);
        else if (won == WinCodes.FOXES_WIN) won_str = getString(R.string.fox);

        if (won_str != null) {
            Intent intent = new Intent(context, ResultActivity.class);
            intent.putExtra("won_str", won_str);
            startActivityForResult(intent, RequestCodes.ResultActivity);
        }
    }
}
