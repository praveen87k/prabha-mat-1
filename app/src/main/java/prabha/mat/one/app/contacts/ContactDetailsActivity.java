package prabha.mat.one.app.contacts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import prabha.mat.one.app.R;

public class ContactDetailsActivity extends AppCompatActivity {

    private String matchId, matchName, requestType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        matchId = getIntent().getExtras().getString("matchId");
        matchName = getIntent().getExtras().getString("matchName");
        requestType = getIntent().getExtras().getString("requestType");
    }
}
