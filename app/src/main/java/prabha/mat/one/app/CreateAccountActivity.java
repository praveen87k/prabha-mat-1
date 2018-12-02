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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText emailID, password, name, age;
    private Button createAccount;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initialiseVariables();

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    progressDialog.setMessage("Creating account");
                    progressDialog.show();
                    // save values to Firebase
                    firebaseAuth.createUserWithEmailAndPassword(emailID.getText().toString().trim(),
                                    password.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        progressDialog.dismiss();
                                        sendUserData();
                                        firebaseAuth.signOut();
                                        Toast.makeText(CreateAccountActivity.this,
                                                "Account created successfully. Please Login",
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(CreateAccountActivity.this,
                                                                  LoginActivity.class));
                                        finish();
                                    }else {
                                        progressDialog.dismiss();
                                        Toast.makeText(CreateAccountActivity.this,
                                                "Account creation failed",
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
        name = (EditText) findViewById(R.id.etName);
        age = (EditText) findViewById(R.id.etAge);
    }

    private boolean validate(){
        if(emailID.getText().toString().isEmpty()
                || password.getText().toString().isEmpty()
                || age.getText().toString().isEmpty()
                || name.getText().toString().isEmpty()){
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    private void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        UserProfile userProfile = new UserProfile(age.getText().toString().trim(),
                                                  name.getText().toString().trim());
        databaseReference.child("users").child(firebaseAuth.getUid()).setValue(userProfile);
    }
}
