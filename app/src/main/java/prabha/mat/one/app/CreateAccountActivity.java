package prabha.mat.one.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText emailID, password, name, age, location, phoneNum;
    private RadioGroup gender;
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
        location = (EditText) findViewById(R.id.etLocation);
        phoneNum = (EditText) findViewById(R.id.etPhone);
        gender = (RadioGroup) findViewById(R.id.rgGender);
    }

    private boolean validate(){
        if(emailID.getText().toString().isEmpty()
                || password.getText().toString().isEmpty()
                || age.getText().toString().isEmpty()
                || name.getText().toString().isEmpty()
                || location.getText().toString().isEmpty()
                || phoneNum.getText().toString().isEmpty()){
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }else if(emailValidationFailed()){
            Toast.makeText(this, "Please fill valid Email Id", Toast.LENGTH_SHORT).show();
            return false;
        }else if(password.getText().toString().length() < 8){
            Toast.makeText(this, "Password should have at least 8 characters", Toast.LENGTH_SHORT).show();
            return false;
        }else if(Integer.parseInt(age.getText().toString()) < 21
                                       && getGender().equals("Male")){
            Toast.makeText(this, "You should be at least 21 years old", Toast.LENGTH_SHORT).show();
            return false;
        }else if(Integer.parseInt(age.getText().toString()) < 18){
            Toast.makeText(this, "You should be at least 18 years old", Toast.LENGTH_SHORT).show();
            return false;
        }else if(Integer.parseInt(age.getText().toString()) > 90){
            Toast.makeText(this, "Please enter valid age", Toast.LENGTH_SHORT).show();
            return false;
        }else if(phoneValidationFailed()){
            Toast.makeText(this, "Please enter valid phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    private boolean phoneValidationFailed() {
        if(android.util.Patterns.PHONE.matcher(phoneNum.getText().toString()).matches()
                && phoneNum.getText().toString().length() > 9
                && phoneNum.getText().toString().length() < 13){
            return false;
        }
        return true;
    }

    private boolean emailValidationFailed() {
        String email = emailID.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.matches(emailPattern)){
            return false;
        }
        return true;
    }

    private String getGender(){
        int selectedRadioButtonID = gender.getCheckedRadioButtonId();
        // If nothing is selected from Radio Group, then it return -1
        if (selectedRadioButtonID != -1) {
            RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
            String selectedRadioButtonText = selectedRadioButton.getText().toString();
            Log.i("selectedRadioButtonText", " : " + selectedRadioButtonText);
            return selectedRadioButtonText;
        }else {
            return "Male";
        }
    }

    private void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        UserProfile userProfile = new UserProfile(age.getText().toString().trim(),
                                                  name.getText().toString().trim(),
                                                  this.getGender(),
                                                  "default",
                                                  phoneNum.getText().toString().trim(),
                                                  location.getText().toString().trim());
        databaseReference.child("Users").child(firebaseAuth.getUid()).setValue(userProfile);
    }
}
