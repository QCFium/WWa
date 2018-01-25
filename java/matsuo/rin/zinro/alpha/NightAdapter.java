package matsuo.rin.zinro.alpha;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;


class NightAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater = null;
    private Context c;
    private int cur_player = -1;

    NightAdapter(Context context) {
        this.c = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return GameState.playerList.size();
    }

    void setCurPlayer(int player) {
        cur_player = player;
    }

    @Override
    public String getItem(int p) {
        return GameState.playerList.get(p).name;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView (int p, View view, ViewGroup parent) {
        if (view == null) view = layoutInflater.inflate(R.layout.listview_night, parent, false);


        final TextView tvPlayerName = view.findViewById(R.id.TVplayer_name);
        tvPlayerName.setText(GameState.playerList.get(p).name);

        final LinearLayout llContent = view.findViewById(R.id.LLcontent);
        if (!GameState.playerList.get(p).isAlive || (Config.forbid_self_voting && p == cur_player)) { // hide dead player and voter himself(if configured)
            llContent.setVisibility(View.GONE);
        } else {
            llContent.setVisibility(View.VISIBLE);
        }
        return view;
    }
}
