package kr.co.ajsoft.imoim.GroupFragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import kr.co.ajsoft.imoim.Adapter.ChatUserAdapter;
import kr.co.ajsoft.imoim.MessageActivity;
import kr.co.ajsoft.imoim.Model.Chat;
import kr.co.ajsoft.imoim.Model.ChatUser;
import kr.co.ajsoft.imoim.R;


public class ChatUserFragment extends Fragment {

    private RecyclerView recyclerView;

    private ChatUserAdapter chatUserAdapter;
    private ArrayList<ChatUser> users;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chat_user, container, false);

        recyclerView=view.findViewById(R.id.cu_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        users=new ArrayList<>();



        
        readUsers();


        return view;

    }

    private void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    users.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ChatUser chatUser = snapshot.getValue(ChatUser.class);



                        if (!chatUser.getId().equals(firebaseUser.getUid())) {
                            users.add(chatUser);
                        }

                    }


                chatUserAdapter= new ChatUserAdapter(getContext(),users);
                recyclerView.setAdapter(chatUserAdapter);

                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }




}