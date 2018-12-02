package prabha.mat.one.app;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Tab1Matches extends Fragment{

    private FirebaseDatabase firebaseDatabase;
    private ProgressDialog progressDialog;
    private ArrayList<String> userNames = new ArrayList<String>();
    private ArrayList<String> userAges = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab1_matches, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        progressDialog.show();

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userNames.clear();
                userAges.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String userName = ds.child("userName").getValue(String.class);
                    userNames.add(userName);
                    String userAge = ds.child("userAge").getValue(String.class);
                    userAges.add(userAge);
                }
                ListView listView = (ListView)rootView.findViewById(R.id.lvUsers);
                CustomAdapter customAdapter = new CustomAdapter();
                listView.setAdapter(customAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
//                commenting as an error is thrown during logout
//                Toast.makeText(getActivity(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }

            class CustomAdapter extends BaseAdapter{

                @Override
                public int getCount() {
                    return userNames.size();
                }

                @Override
                public Object getItem(int position) {
                    return null;
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    convertView = getLayoutInflater().inflate(R.layout.matching_user, null);
                    TextView userName = (TextView)convertView.findViewById(R.id.tvUserName);
                    TextView userAge = (TextView)convertView.findViewById(R.id.tvUserAge);
                    userName.setText(userNames.get(position));
                    userAge.setText(userAges.get(position));
                    return convertView;
                }
            }
        });

        return rootView;
    }
}
