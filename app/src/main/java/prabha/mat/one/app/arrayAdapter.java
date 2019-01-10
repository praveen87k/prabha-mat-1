package prabha.mat.one.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;

public class arrayAdapter extends ArrayAdapter<Cards>{
    Context context;

    public arrayAdapter(Context context, int resourceId, List<Cards> items){
        super(context, resourceId, items);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        Cards card_item = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);

        name.setText(card_item.getName());
//        image.setImageResource(R.mipmap.ic_launcher);
        Log.i("card_item.getProfile", " : " + card_item.getProfileImageUrl());
        switch(card_item.getProfileImageUrl()){
            case "default":
                Glide.clear(image);
                Glide.with(convertView.getContext()).load(R.drawable.default_user_pic).into(image);
                Log.i("card_item.getProfile", " : " + true);
                break;
            default:
                Glide.clear(image);
                Log.i("card_item.getProfile", " : " + false);
                Glide.with(convertView.getContext()).load(card_item.getProfileImageUrl()).into(image);
                break;
        }

        return convertView;
    }
}
