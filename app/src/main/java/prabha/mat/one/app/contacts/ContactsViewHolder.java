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

    public TextView mMatchId, mMatchName, mMatchAge, mRequestType;
    public ImageView mMatchImage;

    public ContactsViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mMatchId = (TextView) itemView.findViewById(R.id.ContactId);
        mMatchName = (TextView) itemView.findViewById(R.id.ContactName);
        mMatchAge = (TextView) itemView.findViewById(R.id.ContactAge);
        mRequestType = (TextView) itemView.findViewById(R.id.ContactRequestType);
        mMatchImage = (ImageView) itemView.findViewById(R.id.ContactImage);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("matchId", mMatchId.getText().toString());
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
    }
}
