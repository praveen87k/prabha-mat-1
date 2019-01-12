package prabha.mat.one.app.contacts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import prabha.mat.one.app.R;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsViewHolder>  {

    private List<ContactsObject> matchesList;
    private Context context;

    public ContactsAdapter(List<ContactsObject> matchesList, Context context){
        this.matchesList = matchesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View layoutView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_contact, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ContactsViewHolder rcv = new ContactsViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        holder.mMatchId.setText(matchesList.get(position).getUserId());
        holder.mMatchName.setText(matchesList.get(position).getUserName());
        holder.mMatchAge.setText(matchesList.get(position).getUserAge());
        holder.mRequestType.setText(matchesList.get(position).getRequestType());
        if(!matchesList.get(position).getProfileImageUrl().equals("default")){
            Glide.with(context).load(matchesList.get(position).getProfileImageUrl())
                    .into(holder.mMatchImage);
        }
    }

    @Override
    public int getItemCount() {
        return matchesList.size();
    }
}
