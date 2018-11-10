package prabha.mat.one.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText Email;
    private EditText Password;
    private Button Login;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = (EditText)findViewById(R.id.etEmail);
        Password = (EditText)findViewById(R.id.etPassword);
        Login = (Button)findViewById(R.id.btnLoginMenu);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Email.getText().toString(), Password.getText().toString());
            }
        });
    }

    private void validate(String emailID, String password){
        if(emailID.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.setMessage("Logging in");
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(emailID, password).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this,
                                        "Login successful",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this,
                                                             HomePageActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this,
                                        "Login failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );
        }
    }
}
