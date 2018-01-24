package matsuo.rin.zinro.alpha;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class UserRegisterActivity extends AppCompatActivity {
    private static final int MAX_PEOPLE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        final ListView listView = findViewById(R.id.LVusers);
        final UserRegisterAdapter adapter = new UserRegisterAdapter(this);

        // start a game
        final Button b_start_game = findViewById(R.id.Bstart_game);
        b_start_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), GameActivity.class)
                        .putStringArrayListExtra("user_list", adapter.userList);
                startActivity(i);
            }
        });

        final Button b_add_user = findViewById(R.id.Badd_user);
        b_add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check max # of users
                if (adapter.getCount() >= MAX_PEOPLE) {
                    Toast.makeText(UserRegisterActivity.this, "You can't add more player", Toast.LENGTH_SHORT).show();
                }

                // Show a prompt to enter the name
                final EditText et_new_user = new EditText(UserRegisterActivity.this);
                new AlertDialog.Builder(UserRegisterActivity.this)
                    .setMessage("Enter new user's name")
                    .setView(et_new_user)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Update the ListView
                            adapter.userList.add(et_new_user.getText().toString());
                            listView.setAdapter(adapter);
                        }
                    })
                    .show();
            }
        });


    }
}
