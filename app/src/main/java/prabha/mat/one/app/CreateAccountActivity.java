package prabha.mat.one.app;

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

public class CreateAccountActivity extends AppCompatActivity {

    private EditText emailID, password;
    private Button createAccount;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initialiseVariables();

        firebaseAuth = FirebaseAuth.getInstance();

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    // save values to Firebase
                    firebaseAuth.createUserWithEmailAndPassword(emailID.getText().toString().trim(),
                                    password.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(CreateAccountActivity.this,
                                                "Account Created",
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
                                    }else {
                                        Toast.makeText(CreateAccountActivity.this,
                                                "Account Creation Failed",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void initialiseVariables(){
        emailID = (EditText) findViewById(R.id.etEmailInput);
        password = (EditText) findViewById(R.id.etPasswordInput);
        createAccount = (Button) findViewById(R.id.btnCreateAccount);
    }

    private boolean validate(){
        if(emailID.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }
}
