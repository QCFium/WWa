package matsuo.rin.zinro.alpha;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class UserRegisterAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater = null;
    public ArrayList<String> userList = new ArrayList<>(); // accessed to update player lists

    public UserRegisterAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public String getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listview_user_register,parent,false);
        }

        final TextView tv_user = (TextView)convertView.findViewById(R.id.ETuser);
        tv_user.setText(userList.get(position));

        final Button b_del_user = (Button) convertView.findViewById(R.id.Bdel_user);
        b_del_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // remove the user
                userList.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}