package matsuo.rin.zinro.alpha;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

class PlayerRegisterAdapter extends BaseAdapter {
    /*
        This references ArrayList<String> Settings.playerList
     */

    private Context context;
    private LayoutInflater layoutInflater = null;

    public PlayerRegisterAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return GameState.playerList.size();
    }

    @Override
    public String getItem(int position) {
        return GameState.playerList.get(position).name;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listview_player_register,parent,false);
        }

        final TextView tv_player = convertView.findViewById(R.id.ETplayer);
        tv_player.setText(GameState.playerList.get(position).name);

        final Button b_del_player = convertView.findViewById(R.id.Bdel_player);
        b_del_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // remove the player
                GameState.playerList.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}