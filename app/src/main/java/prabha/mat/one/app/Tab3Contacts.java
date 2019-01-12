package prabha.mat.one.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import prabha.mat.one.app.contacts.ContactsAdapter;
import prabha.mat.one.app.contacts.ContactsObject;


public class Tab3Contacts extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mMatchesAdapter;
    private RecyclerView.LayoutManager mMatchesLayoutManager;
    private String currentUserId;
    private ArrayList<ContactsObject> resultsMatches = new ArrayList<ContactsObject>();
    private List<ContactsObject> getDataSetMatches() {
        return resultsMatches;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3_contacts, container, false);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_contacts);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mMatchesLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mMatchesLayoutManager);
        mMatchesAdapter = new ContactsAdapter(getDataSetMatches(), getActivity());
        mRecyclerView.setAdapter(mMatchesAdapter);

        loadData();

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) { // fragment is visible and have created
            loadData();
        }
    }

    public void loadData(){
        // data for fragment when it visible here
        resultsMatches.clear();
        getContactedByUsers();
        getContactedUsers();
    }

    private void getContactedUsers() {
        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(currentUserId).child("connections").child("contacted");
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot match : dataSnapshot.getChildren()){
                        fetchMatchInformation(match.getKey(), "contacted",
                                                           match.getChildrenCount());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getContactedByUsers() {
        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(currentUserId).child("connections").child("contactedBy");
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot match : dataSnapshot.getChildren()){
                        fetchMatchInformation(match.getKey(), "contactedBy",
                                                               match.getChildrenCount());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void fetchMatchInformation(String key, final String type, final Long childCount) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(key);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String userId = dataSnapshot.getKey();
                    String userName = "";
                    String userAge = "";
                    String profileImageUrl = "";
                    String requestType = "";
                    if(dataSnapshot.child("userName").getValue() != null){
                        userName = dataSnapshot.child("userName").getValue().toString();
                    }
                    if(dataSnapshot.child("userAge").getValue() != null){
                        userAge = dataSnapshot.child("userAge").getValue().toString();
                    }
                    if(dataSnapshot.child("profileImageUrl").getValue() != null){
                        profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                    }
                    if(type.equals("contactedBy") && childCount > 1){
                        requestType = "Contact Request approved by me";
                    }else if(type.equals("contactedBy") && childCount > 0){
                        requestType = "Contact Request rejected by me";
                    }else if(type.equals("contactedBy") && childCount < 1){
                        requestType = "Contact Request received";
                    }else if(type.equals("contacted") && childCount > 1){
                        requestType = "Contact Request approved";
                    }else if(type.equals("contacted") && childCount > 0){
                        requestType = "Contact Request rejected";
                    }else if(type.equals("contacted") && childCount < 1){
                        requestType = "Contact Request sent";
                    }
                    ContactsObject obj = new ContactsObject(userId, userName, userAge,
                                                                 profileImageUrl, requestType);
                    resultsMatches.add(obj);
                    mMatchesAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
