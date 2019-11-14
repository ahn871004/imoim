package kr.co.ajsoft.imoim.GroupFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import kr.co.ajsoft.imoim.MessageActivity;
import kr.co.ajsoft.imoim.Model.ChatUser;
import kr.co.ajsoft.imoim.R;


public class ChatUserFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chat_user, container, false);

        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new ChatUserAdapter());
        return view;

    }

    class ChatUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


        List<ChatUser> chatUserList;

        public ChatUserAdapter(){
            chatUserList=new ArrayList<>();
            FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        chatUserList.add(snapshot.getValue(ChatUser.class));
                    }
                    notifyDataSetChanged();
                    //새로고침
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chatuser_item,parent,false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            Glide.with(holder.itemView.getContext()).load(chatUserList.get(position)
                    .getImageurl()).apply(new RequestOptions().circleCrop()).into(((CustomViewHolder)holder).imageView);

            ((CustomViewHolder)holder).textView.setText(chatUserList.get(position).getUsername());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getView().getContext(), MessageActivity.class);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return chatUserList.size();
        }
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textView;

        public CustomViewHolder(View view) {
            super(view);
            imageView=view.findViewById(R.id.profile_image);
            textView=view.findViewById(R.id.username);

        }
    }
}