package prabha.mat.one.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText email;
    private Button forgotPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = (EditText)findViewById(R.id.etEmailReset);
        forgotPassword = (Button)findViewById(R.id.btnResetPassword);
        firebaseAuth = FirebaseAuth.getInstance();

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailID = email.getText().toString().trim();
                if(emailID.isEmpty()){
                    Toast.makeText(ForgotPasswordActivity.this,
                                   "Please enter your registered Email ID",
                                    Toast.LENGTH_LONG).show();
                }else {
                    firebaseAuth.sendPasswordResetEmail(emailID)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ForgotPasswordActivity.this,
                                        "Password reset link has been sent to your Email ID",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgotPasswordActivity.this,
                                                          LoginActivity.class));
                            }else {
                                Toast.makeText(ForgotPasswordActivity.this,
                                        "Failed to send Password reset link",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
