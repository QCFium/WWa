package matsuo.rin.zinro.alpha;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.*;

class Config {
    static boolean forbid_self_voting = false;

    static void load(Context context) {
        SharedPreferences pref = context.getSharedPreferences("config", MODE_PRIVATE);
        forbid_self_voting = pref.getBoolean("forbid_self_voting", false);
    }

    static void save(Context context) {
        SharedPreferences pref = context.getSharedPreferences("config", MODE_PRIVATE);
        pref.edit().putBoolean("forbid_self_voting", forbid_self_voting).apply();
    }
}
