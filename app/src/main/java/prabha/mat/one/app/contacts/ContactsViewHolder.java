package prabha.mat.one.app.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import prabha.mat.one.app.R;
import prabha.mat.one.app.chat.ChatActivity;

public class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    public TextView mMatchId, mMatchName, mMatchAge, mRequestType, mPhoneNum;
    public ImageView mMatchImage;

    public ContactsViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mMatchId = (TextView) itemView.findViewById(R.id.ContactId);
        mMatchName = (TextView) itemView.findViewById(R.id.ContactName);
        mMatchAge = (TextView) itemView.findViewById(R.id.ContactAge);
        mRequestType = (TextView) itemView.findViewById(R.id.ContactRequestType);
        mMatchImage = (ImageView) itemView.findViewById(R.id.ContactImage);
        mPhoneNum = (TextView) itemView.findViewById(R.id.ContactPhoneNum);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if(mRequestType.getText().toString().equals("Contact Request approved by me")){
            intent = new Intent(view.getContext(), ContactDetailsActivity1.class);
        }else if(mRequestType.getText().toString().equals("Contact Request rejected by me")){
            intent = new Intent(view.getContext(), ContactDetailsActivity2.class);
        }else if(mRequestType.getText().toString().equals("Contact Request received")){
            intent = new Intent(view.getContext(), ContactDetailsActivity3.class);
        }else if(mRequestType.getText().toString().equals("Contact Request approved")){
            intent = new Intent(view.getContext(), ContactDetailsActivity4.class);
        }else if(mRequestType.getText().toString().equals("Contact Request rejected")){
            intent = new Intent(view.getContext(), ContactDetailsActivity5.class);
        }else if(mRequestType.getText().toString().equals("Contact Request sent")){
            intent = new Intent(view.getContext(), ContactDetailsActivity6.class);
        }else {
            intent = new Intent(view.getContext(), ContactDetailsActivity.class);
        }

        Bundle bundle = new Bundle();
        bundle.putString("matchName", mMatchName.getText().toString());
        bundle.putString("matchId", mMatchId.getText().toString());
        bundle.putString("matchPhoneNum", mPhoneNum.getText().toString());

        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
    }
}
