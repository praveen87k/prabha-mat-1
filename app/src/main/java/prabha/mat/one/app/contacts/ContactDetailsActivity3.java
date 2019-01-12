package prabha.mat.one.app.contacts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import prabha.mat.one.app.HomePageActivity;
import prabha.mat.one.app.ProfileDetailsActivity;
import prabha.mat.one.app.R;

public class ContactDetailsActivity3 extends AppCompatActivity {

    private TextView requesterName;
    private Button viewProfile, confirmBtn, rejectBtn;
    private String matchName, matchId, currentUserId;
    private DatabaseReference currentUserDbRef, matchUserDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details3);

        matchName = getIntent().getExtras().getString("matchName");
        matchId = getIntent().getExtras().getString("matchId");

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        currentUserDbRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(currentUserId);
        matchUserDbRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(matchId);

        requesterName = (TextView) findViewById(R.id.requester_id_3);
        requesterName.setText(matchName);

        viewProfile = (Button) findViewById(R.id.view_profile_btn);
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactDetailsActivity3.this,
                                                      ProfileDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("matchId", matchId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        confirmBtn = (Button) findViewById(R.id.confirm_share);
        rejectBtn = (Button) findViewById(R.id.reject_share);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptRequest();
                finish();
                Intent intent = new Intent(ContactDetailsActivity3.this,
                                                        HomePageActivity.class);
                startActivity(intent);
            }
        });

        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectRequest();
                finish();
                Intent intent = new Intent(ContactDetailsActivity3.this,
                        HomePageActivity.class);
                startActivity(intent);
            }
        });
    }

    private void acceptRequest() {
        currentUserDbRef.child("connections").child("contactedBy").child(matchId)
                .child("approval").setValue(true);
        currentUserDbRef.child("connections").child("contactedBy").child(matchId)
                .child("confirmed").setValue(true);
        matchUserDbRef.child("connections").child("contacted").child(currentUserId)
                .child("approval").setValue(true);
        matchUserDbRef.child("connections").child("contacted").child(currentUserId)
                .child("confirmed").setValue(true);
        Toast.makeText(this, "Phone number shared.", Toast.LENGTH_SHORT).show();
    }

    private void rejectRequest() {
        currentUserDbRef.child("connections").child("contactedBy").child(matchId)
                .child("rejected").setValue(true);
        matchUserDbRef.child("connections").child("contacted").child(currentUserId)
                .child("rejected").setValue(true);
        Toast.makeText(this, "Request Rejected.", Toast.LENGTH_SHORT).show();
    }
}
