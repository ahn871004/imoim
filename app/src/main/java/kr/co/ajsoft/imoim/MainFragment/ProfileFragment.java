package kr.co.ajsoft.imoim.MainFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kr.co.ajsoft.imoim.Adapter.MyPhotoAdapter;
import kr.co.ajsoft.imoim.Model.Post;
import kr.co.ajsoft.imoim.Model.User;
import kr.co.ajsoft.imoim.R;


public class ProfileFragment extends Fragment {

    ImageView imageProfile,options;
    TextView posts,followers,following,fullname,bio,username;
    Button editProfile;

    private List<String> mySaves;

    RecyclerView recyclerView_saves;
    MyPhotoAdapter myPhotoAdapter_saves;
    List<Post> postList_saves;

    RecyclerView recyclerView;
    MyPhotoAdapter myPhotoAdapter;
    List<Post> postList;

    FirebaseUser firebaseUser;
    String profileid;

    ImageButton myPhotos,savedPhotos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences prefs=getContext().getSharedPreferences("PREFS",Context.MODE_PRIVATE);
        profileid=prefs.getString("profileid","none");

        imageProfile=view.findViewById(R.id.image_profile);
        options=view.findViewById(R.id.options);
        posts=view.findViewById(R.id.followers);
        followers=view.findViewById(R.id.followers);
        following=view.findViewById(R.id.following);
        fullname=view.findViewById(R.id.fullname);
        bio=view.findViewById(R.id.bio);
        username=view.findViewById(R.id.username);
        editProfile=view.findViewById(R.id.edit_profile);
        myPhotos=view.findViewById(R.id.my_photos);
        savedPhotos=view.findViewById(R.id.saved_photos);

        recyclerView=view.findViewById(R.id.recycler_view1);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(linearLayoutManager);
        postList=new ArrayList<>();
        myPhotoAdapter=new MyPhotoAdapter(getContext(),postList);
        recyclerView.setAdapter(myPhotoAdapter);

        recyclerView_saves=view.findViewById(R.id.recycler_view_save);
        recyclerView_saves.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_saves=new GridLayoutManager(getContext(),3);
        recyclerView_saves.setLayoutManager(linearLayoutManager_saves);
        postList_saves=new ArrayList<>();
        myPhotoAdapter_saves=new MyPhotoAdapter(getContext(),postList_saves);
        recyclerView_saves.setAdapter(myPhotoAdapter_saves);

        recyclerView.setVisibility(View.VISIBLE);
        recyclerView_saves.setVisibility(View.GONE);

        userInfo();
        getFollowers();
        getNumPosts();
        myPhotos();
        mysaves();

        if(profileid.equals(firebaseUser.getUid())){
            editProfile.setText("프로필 수정");

        }else {
            checkFollow();
            savedPhotos.setVisibility(View.GONE);

        }


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn=editProfile.getText().toString();

                if(btn.equals("Edit Profile")){
                    //editprofile로 이동

                }else {
                    if(btn.equals("follow")){
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                                .child("following").child(profileid).setValue(true);
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid)
                                .child("followers").child(firebaseUser.getUid()).setValue(true);

                    }else{
                        if(btn.equals("following")){
                            FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                                    .child("following").child(profileid).removeValue();
                            FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid)
                                    .child("followers").child(firebaseUser.getUid()).removeValue();


                        }


                    }


                }

            }
        });

        myPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView_saves.setVisibility(View.GONE);

            }
        });

        savedPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
                recyclerView_saves.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private void userInfo(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users").child(profileid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(getContext()==null){
                    return;
                }

                User user=dataSnapshot.getValue(User.class);

                Glide.with(getContext()).load(user.getImageurl()).into(imageProfile);
                username.setText(user.getUserName());
                fullname.setText(user.getFullname());
                bio.setText(user.getBio());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkFollow(){

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(profileid).exists()){
                    editProfile.setText("following");

                }else {
                    editProfile.setText("follow");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getFollowers(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(profileid).child("followers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followers.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference referencel=FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(profileid).child("following");
        referencel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                following.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getNumPosts(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int i=0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post=snapshot.getValue(Post.class);
                    if (post.getPublisher().equals(profileid)){
                        i++;

                    }

                }


                posts.setText(""+i);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void myPhotos(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post=snapshot.getValue(Post.class);
                    if(post.getPublisher().equals(profileid)){
                        postList.add(post);

                    }

                }
                Collections.reverse(postList);
                myPhotoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void mysaves(){
        mySaves=new ArrayList<>();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Saves")
                .child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    mySaves.add(snapshot.getKey());


                }
                readSaves();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void readSaves(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList_saves.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Post post=snapshot.getValue(Post.class);
                    for(String id:mySaves){
                        if(post.getPostid().equals(id)){
                            postList_saves.add(post);

                        }

                    }

                }
                myPhotoAdapter_saves.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
