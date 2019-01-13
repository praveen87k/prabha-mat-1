package prabha.mat.one.app;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import java.util.ArrayList;
import java.util.List;

public class Tab1Matches extends Fragment{

    private ProgressDialog progressDialog;
    private arrayAdapter arrayAdapter;

    private String currentUserId;
    private DatabaseReference databaseReference;

    List<Cards> rowItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab1_matches, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        progressDialog.show();


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        checkUserGender();

        rowItems = new ArrayList<Cards>();
        arrayAdapter = new arrayAdapter(getActivity(), R.layout.item, rowItems);


        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) rootView.findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Cards cards = (Cards) dataObject;
                String userID = cards.getUserId();
                databaseReference.child(userID).child("connections")
                                 .child("rejectedBy").child(currentUserId).setValue(true);
                Toast.makeText(getActivity(), "Rejected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Cards cards = (Cards) dataObject;
                String userID = cards.getUserId();
                databaseReference.child(userID).child("connections")
                        .child("acceptedBy").child(currentUserId).setValue(true);
                databaseReference.child(currentUserId).child("connections")
                        .child("shortListed").child(userID).setValue(true);
                isConnectionMatch(userID);
                Toast.makeText(getActivity(), "Shortlisted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                // al.add("XML ".concat(String.valueOf(i)));
                // arrayAdapter.notifyDataSetChanged();
                // Log.d("LIST", "notified");
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                // Don't do this, as this would result in contacting the profile without
                // shortlisting and post this profile details of the same can't be viewed.
//                Cards cards = (Cards) dataObject;
//                String userID = cards.getUserId();
//                Intent intent = new Intent(getActivity(), ProfileDetailsActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("matchId", userID);
//                intent.putExtras(bundle);
//                startActivity(intent);
                Toast.makeText(getActivity(), "Swipe right to Shortlist.", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), "Swipe left to Reject.", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private String userGender;
    private String userOppositeGender;

    public void checkUserGender(){
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = currentUser.getUid();
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference()
                                                          .child("Users").child(currentUserId);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("userGender").getValue() != null){
                        userGender = dataSnapshot.child("userGender").getValue().toString();
                        if(userGender.equals("Male")){
                            userOppositeGender = "Female";
                        }else {
                            userOppositeGender = "Male";
                        }
                        getMatches();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void getMatches(){
        DatabaseReference matchesDb = FirebaseDatabase.getInstance().getReference().child("Users");
        matchesDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()
                        && dataSnapshot.child("userGender").getValue().toString().equals(userOppositeGender)
                        && !dataSnapshot.child("connections")
                                        .child("rejectedBy").hasChild(currentUserId)
                        && !dataSnapshot.child("connections")
                                        .child("acceptedBy").hasChild(currentUserId)){
                    String profileImageUrl = "default";
                    if (!dataSnapshot.child("profileImageUrl").getValue().equals("default")){
                        profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                    }
                    Cards item = new Cards(dataSnapshot.getKey(),
                                           dataSnapshot.child("userName").getValue().toString(),
                                           profileImageUrl);
                    rowItems.add(item);
                    arrayAdapter.notifyDataSetChanged();
                }
                progressDialog.dismiss();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    private void isConnectionMatch(String userId){
        DatabaseReference currentUserConnectionsDb =
                databaseReference.child(currentUserId).child("connections")
                                 .child("acceptedBy").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
//                    Toast.makeText(getActivity(), "New Connection", Toast.LENGTH_SHORT).show();

                    String key = FirebaseDatabase.getInstance().getReference().child("Chat")
                                                                         .push().getKey();

                    databaseReference.child(dataSnapshot.getKey())
                            .child("connections").child("matches").child(currentUserId)
                            .child("ChatId").setValue(key);
                    databaseReference.child(currentUserId).child("connections")
                            .child("matches").child(dataSnapshot.getKey())
                            .child("ChatId").setValue(key);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
