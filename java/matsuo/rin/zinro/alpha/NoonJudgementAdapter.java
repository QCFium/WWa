package matsuo.rin.zinro.alpha;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class NoonJudgementAdapter extends BaseAdapter {
    /*
        This references ArrayList<Player> Settings.playerList
    */

    private Context context;
    private LayoutInflater layoutInflater = null;
    private int cur_player = -1;

    NoonJudgementAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    void setCurrentPlayer(int cur_player) {
        this.cur_player = cur_player;
    }

    @Override
    public int getCount() {
        return GameState.playerList.size();// - (Config.forbid_sv ? 1 : 0); // don't contain myself
    }

    @Override
    public Player getItem(int position) {
        return GameState.playerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listview_noon_judgement, parent, false);
        }

        final TextView tv_player = convertView.findViewById(R.id.TVplayer);
        tv_player.setText(GameState.playerList.get(position).name);

        final LinearLayout content = convertView.findViewById(R.id.LLcontent);
        if (!GameState.playerList.get(position).isAlive || (Config.forbid_self_voting && position == cur_player)) { // hide dead player and voter himself(if configured)
            content.setVisibility(View.GONE);
        } else {
            content.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}