package kr.co.ajsoft.imoim.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.ajsoft.imoim.Model.ChatUser;
import kr.co.ajsoft.imoim.R;

public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.VH> {

    private Context context;
    private ArrayList<ChatUser> users;



    public ChatUserAdapter(Context context, ArrayList<ChatUser> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.chatuser_item,parent,false);


        return new ChatUserAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {


        final ChatUser chatUser=users.get(position);
        holder.username.setText(chatUser.getUsername());


        if(chatUser.getImageurl().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);

        }else {

            Glide.with(context).load(chatUser.getImageurl()).into(holder.profile_image);

        }


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class VH extends RecyclerView.ViewHolder{

        public TextView username;
        public CircleImageView profile_image;

        public VH(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.chatuser_username);
            profile_image=itemView.findViewById(R.id.chatuser_profile_image);

        }
    }

}
