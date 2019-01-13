package prabha.mat.one.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyProfileActivity extends AppCompatActivity {

    private Button btnUpdateProfile;
    private EditText etUserName, etPhoneNumber, etUserAge, etUserLocation;
    private TextView tvGender;
    private ImageView ivProfilePic;
    private ProgressDialog progressDialog, uploadProgress;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference userDbRef;

    private String userId, name, age, location, gender, phone, profilePicURL;
    private Uri resultURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        etUserName = (EditText) findViewById(R.id.profileName);
        etPhoneNumber = (EditText) findViewById(R.id.profilePhone);
        etUserAge = (EditText) findViewById(R.id.profileAge);
        etUserLocation = (EditText) findViewById(R.id.profileLocation);
        tvGender = (TextView) findViewById(R.id.profileGender);
        ivProfilePic = (ImageView) findViewById(R.id.profilePic);
        btnUpdateProfile = (Button) findViewById(R.id.updateProfile);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        userDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        getUserInfo();

        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
            }
        });
    }

    private void saveUserInfo(){
        name = etUserName.getText().toString();
        phone = etPhoneNumber.getText().toString();
        age = etUserAge.getText().toString();
        location = etUserLocation.getText().toString();

        if(!validate()){
            return;
        }

        Map userInfo = new HashMap();
        userInfo.put("userName", name);
        userInfo.put("userAge", age);
        userInfo.put("userLocation", location);
        userInfo.put("userPhone", phone);
        userDbRef.updateChildren(userInfo);

        if(resultURI != null){
            uploadProgress = new ProgressDialog(this);
            uploadProgress.setMessage("Uploading");
            uploadProgress.show();

            StorageReference filePath = FirebaseStorage.getInstance().getReference()
                                          .child("ProfilePics").child(userId);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),
                                                           resultURI);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filePath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    uploadProgress.dismiss();
                    Toast.makeText(MyProfileActivity.this,
                            "Profile update failed", Toast.LENGTH_SHORT).show();
                }
            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference storageReference = taskSnapshot.getStorage();
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            Map userInfo = new HashMap();
                            userInfo.put("profileImageUrl", downloadUrl.toString());
                            userDbRef.updateChildren(userInfo);
                        }
                    });
                    uploadProgress.dismiss();
                    Toast.makeText(MyProfileActivity.this,
                            "Profile updated", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(MyProfileActivity.this,
                    "Profile updated", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validate(){
        if(phone.isEmpty()
                || name.isEmpty()
                || age.isEmpty()
                || location.isEmpty()){
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }else if(Integer.parseInt(age) < 21
                && gender.equals("Male")){
            Toast.makeText(this, "You should be at least 21 years old", Toast.LENGTH_SHORT).show();
            return false;
        }else if(Integer.parseInt(age) < 18){
            Toast.makeText(this, "You should be at least 18 years old", Toast.LENGTH_SHORT).show();
            return false;
        }else if(Integer.parseInt(age) > 90){
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
        if(android.util.Patterns.PHONE.matcher(phone).matches()
                && phone.length() > 9
                && phone.length() < 13){
            return false;
        }
        return true;
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
                    if(map.get("userAge")!=null){
                        age = map.get("userAge").toString();
                        etUserAge.setText(age);
                    }
                    if(map.get("userLocation")!=null){
                        location = map.get("userLocation").toString();
                        etUserLocation.setText(location);
                    }
                    if(map.get("userGender")!=null){
                        gender = map.get("userGender").toString();
                        tvGender.setText(gender);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultURI = imageUri;
            ivProfilePic.setImageURI(resultURI);
        }
    }
}
