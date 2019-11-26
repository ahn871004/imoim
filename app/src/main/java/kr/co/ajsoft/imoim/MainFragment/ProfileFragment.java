package kr.co.ajsoft.imoim.MainFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import kr.co.ajsoft.imoim.EditProfileActivity;
import kr.co.ajsoft.imoim.IntroActivity;
import kr.co.ajsoft.imoim.MainActivity;
import kr.co.ajsoft.imoim.Model.Post;
import kr.co.ajsoft.imoim.Model.User;
import kr.co.ajsoft.imoim.OptionActivity;
import kr.co.ajsoft.imoim.R;
import kr.co.ajsoft.imoim.StartActivity;


public class ProfileFragment extends Fragment implements MainActivity.OnBackPressedListener {

    ImageView imageProfile, options, close;
    TextView posts, followers, following, fullname, bio, username;
    Button editProfile;

    HomeFragment homeFragment;

    private ArrayList<String> mySaves;

    RecyclerView recyclerView_saves;
    MyPhotoAdapter myPhotoAdapter_saves;
    ArrayList<Post> postList_saves;

    RecyclerView recyclerView;
    MyPhotoAdapter myPhotoAdapter;
    ArrayList<Post> postList;

    FirebaseUser firebaseUser;
    String profileid;

    ImageButton myPhotos, savedPhotos;

    Context context;




    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Toolbar toolbar=view.findViewById(R.id.toolbar);

        MainActivity activity=(MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileid = prefs.getString("profileid", "none");

        imageProfile = view.findViewById(R.id.image_profile);
//        options = view.findViewById(R.id.options);
        posts = view.findViewById(R.id.posts);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);
        fullname = view.findViewById(R.id.fullname);
        bio = view.findViewById(R.id.bio);
        username = view.findViewById(R.id.profile_username);
        editProfile = view.findViewById(R.id.edit_profile);
        myPhotos = view.findViewById(R.id.my_photos);
        savedPhotos = view.findViewById(R.id.saved_photos);

        recyclerView = view.findViewById(R.id.recycler_view1);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        myPhotoAdapter = new MyPhotoAdapter(getContext(), postList);
        recyclerView.setAdapter(myPhotoAdapter);

        recyclerView_saves = view.findViewById(R.id.recycler_view_save);
        recyclerView_saves.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_saves = new GridLayoutManager(getContext(), 3);
        recyclerView_saves.setLayoutManager(linearLayoutManager_saves);
        postList_saves = new ArrayList<>();
        myPhotoAdapter_saves = new MyPhotoAdapter(getContext(), postList_saves);
        recyclerView_saves.setAdapter(myPhotoAdapter_saves);

        recyclerView.setVisibility(View.VISIBLE);
        recyclerView_saves.setVisibility(View.GONE);

        close = view.findViewById(R.id.profile_close);

        homeFragment = new HomeFragment();

        setHasOptionsMenu(true);

        userInfo();
        getFollowers();
        getNumPosts();
        myPhotos();
        mysaves();

        //
        if (profileid.equals(firebaseUser.getUid())) {
            editProfile.setText("프로필 수정");

        } else {
            checkFollow();
            savedPhotos.setVisibility(View.GONE);
            close.setVisibility(View.VISIBLE);

        }


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn = editProfile.getText().toString();

                if (btn.equals("프로필 수정")) {
                    //editprofile로 이동
                    startActivity(new Intent(getContext(), EditProfileActivity.class));


                } else {
                    if (btn.equals("follow")) {
                        //"follow"버튼 누르면
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                                .child("following").child(profileid).setValue(true);
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid)
                                .child("followers").child(firebaseUser.getUid()).setValue(true);

                    } else {
                        if (btn.equals("following")) {
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profilefg_option,menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.menu_setting:

                return true;

            case R.id.menu_logout:

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), IntroActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


                return true;

                default:
                    return false;

        }


    }

    private void userInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(profileid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getContext() == null) {
                    return;
                }

                User user = dataSnapshot.getValue(User.class);

                Glide.with(getContext()).load(user.getImageurl()).into(imageProfile);
                username.setText(user.getUsername());
                fullname.setText(user.getFullname());
                bio.setText(user.getBio());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkFollow() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(profileid).exists()) {
                    editProfile.setText("following");

                } else {
                    editProfile.setText("follow");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getFollowers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(profileid).child("followers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followers.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(profileid).child("following");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                following.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getNumPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (post.getPublisher().equals(profileid)) {
                        i++;

                    }

                }


                posts.setText("" + i);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void myPhotos() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (post.getPublisher().equals(profileid)) {
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

    private void mysaves() {
        mySaves = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Saves")
                .child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mySaves.add(snapshot.getKey());


                }
                readSaves();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readSaves() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList_saves.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    for (String id : mySaves) {
                        if (post.getPostid().equals(id)) {
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

    @Override
    public void onBack() {
        Log.e("Other", "onBack");
        MainActivity activity = (MainActivity) getActivity();
        activity.setOnBackPressedListener(null);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("Other", "onAttach()");
        ((MainActivity) context).setOnBackPressedListener(this);
    }

}