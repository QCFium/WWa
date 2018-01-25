package matsuo.rin.zinro.alpha;

import android.content.Context;
import android.widget.Toast;

class Error {
    static void error(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        System.err.println("-------------------------------------");
        System.err.println("-------------------------------------");
        System.err.println("-------------------------------------");
        System.err.println(msg);
        System.err.println("-------------------------------------");
        System.err.println("-------------------------------------");
        System.err.println("-------------------------------------");
        while(true);
    }
}
