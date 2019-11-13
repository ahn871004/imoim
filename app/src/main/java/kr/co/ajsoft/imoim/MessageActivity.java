package kr.co.ajsoft.imoim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.ajsoft.imoim.Adapter.ChatUserAdapter;
import kr.co.ajsoft.imoim.Model.ChatUser;
import kr.co.ajsoft.imoim.Model.User;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

//        Toolbar toolbar =findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        //toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        profile_image=findViewById(R.id.profile_image);
//        username=findViewById(R.id.username);
//
//        intent=getIntent();
//        String userid=intent.getStringExtra("userid");
//
//        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
//        reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                ChatUser user=dataSnapshot.getValue(ChatUser.class);
//                username.setText(user.getUsername());
//                if(user.getImageurl().equals("default")){
//                    profile_image.setImageResource(R.mipmap.ic_launcher);
//
//                }else{
//                    Glide.with(MessageActivity.this).load(user.getImageurl()).into(profile_image);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
    }
}
