package prabha.mat.one.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class WelcomeScreenActivity extends AppCompatActivity {

    private Button LoginMenu;
    private Button CreateAccountMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome_screen);
        getSupportActionBar().hide();

        LoginMenu = (Button)findViewById(R.id.btnLoginMenu);
        CreateAccountMenu = (Button)findViewById(R.id.btnCreateAccountMenu);

        LoginMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeScreenActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        CreateAccountMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeScreenActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }
}