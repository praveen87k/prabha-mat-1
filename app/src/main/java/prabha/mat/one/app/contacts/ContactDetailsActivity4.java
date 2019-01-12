package prabha.mat.one.app.contacts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import prabha.mat.one.app.R;

public class ContactDetailsActivity4 extends AppCompatActivity {

    private String phoneNumber;
    private TextView phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details4);

        phoneNumber = getIntent().getExtras().getString("matchPhoneNum");
        phone = (TextView) findViewById(R.id.phone_number);
        phone.setText(phoneNumber);
    }
}
