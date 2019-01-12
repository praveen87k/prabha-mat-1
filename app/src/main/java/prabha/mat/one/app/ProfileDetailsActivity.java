package prabha.mat.one.app;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ProfileDetailsActivity extends AppCompatActivity {

    private String matchId;

    private Button btnContactProfile;
    private TextView etUserName, etPhoneNumber;
    private ImageView ivProfilePic;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference userDbRef, currentUserDbRef;

    private String currentUserId, name, phone, profilePicURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        matchId = getIntent().getExtras().getString("matchId");

        etUserName = (TextView) findViewById(R.id.profileName_details);
        etPhoneNumber = (TextView) findViewById(R.id.profilePhone_details);
        ivProfilePic = (ImageView) findViewById(R.id.profilePic_details);
        btnContactProfile = (Button) findViewById(R.id.contactProfileBtn);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = firebaseAuth.getCurrentUser().getUid();
        userDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(matchId);

        getUserInfo();

        btnContactProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactProfile();
            }
        });
    }

    private void contactProfile() {
        currentUserDbRef = FirebaseDatabase.getInstance().getReference().child("Users")
                                                              .child(currentUserId);
        currentUserDbRef.child("connections").child("contacted").child(matchId).setValue(true);
        userDbRef.child("connections").child("contactedBy").child(currentUserId).setValue(true);
        Toast.makeText(this, "Contact request sent.", Toast.LENGTH_SHORT).show();
    }

    private void getUserInfo(){
        userDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("userName")!=null){
                        name = map.get("userName").toString();
                        etUserName.setText(name);
                    }
                    if(map.get("userPhone")!=null){
                        phone = map.get("userPhone").toString();
                        etPhoneNumber.setText(phone);
                    }
                    Glide.clear(ivProfilePic);
                    if(map.get("profileImageUrl")!=null){
                        profilePicURL = map.get("profileImageUrl").toString();
                        switch(profilePicURL){
                            case "default":
                                Glide.with(getApplication()).load(R.drawable.default_user_pic)
                                        .listener(new RequestListener<Integer, GlideDrawable>() {
                                            @Override
                                            public boolean onException(Exception e, Integer model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                progressDialog.dismiss();
                                                return false;
                                            }
                                            @Override
                                            public boolean onResourceReady(GlideDrawable resource, Integer model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                progressDialog.dismiss();
                                                return false;
                                            }
                                        })
                                        .into(ivProfilePic);
                                break;
                            default:
                                Glide.with(getApplication()).load(profilePicURL)
                                        .listener(new RequestListener<String, GlideDrawable>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                progressDialog.dismiss();
                                                return false;
                                            }
                                            @Override
                                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                progressDialog.dismiss();
                                                return false;
                                            }
                                        })
                                        .into(ivProfilePic);
                                break;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
