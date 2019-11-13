package kr.co.ajsoft.imoim.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;

import java.util.ArrayList;

import kr.co.ajsoft.imoim.MessageActivity;
import kr.co.ajsoft.imoim.Model.ChatUser;
import kr.co.ajsoft.imoim.Model.User;
import kr.co.ajsoft.imoim.R;

public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.VH>{

    private Context mContext;
    private ArrayList<ChatUser> mUsers;

    public ChatUserAdapter(Context mContext, ArrayList<ChatUser> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.chatuser_item,parent,false);

        return new ChatUserAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

        final ChatUser user=mUsers.get(position);
        holder.username.setText(user.getUsername());
        if(user.getImageurl().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);

        }else{
            Glide.with(mContext).load(user.getImageurl()).into(holder.profile_image);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid",user.getId());
                mContext.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class VH extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_image;

        public VH(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.username);
            profile_image=itemView.findViewById(R.id.profile_image);

        }
    }

}
