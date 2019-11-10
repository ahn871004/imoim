package kr.co.ajsoft.imoim.Adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.ajsoft.imoim.MainFragment.ProfileFragment;
import kr.co.ajsoft.imoim.Model.User;
import kr.co.ajsoft.imoim.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.VH> {

    private Context mContext;
    private List<User> mUsers;

    private FirebaseUser firebaseUser;

    public UserAdapter(Context mContext, List<User> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.user_item,viewGroup,false);


        return new UserAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VH holder, int position) {

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        final User user=mUsers.get(position);

        holder.btnFollow.setVisibility(View.VISIBLE);

        holder.username.setText(user.getUserName());
        holder.fullname.setText(user.getFullname());

        Glide.with(mContext).load(user.getImageurl()).into(holder.imageProfile);
        isFollowing(user.getId(),holder.btnFollow);

        if(user.getId().equals(firebaseUser.getUid())){
            holder.btnFollow.setVisibility(View.GONE);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",user.getId());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();

            }
        });

        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.btnFollow.getText().toString().equals("follow")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(user.getId()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                            .child("followers").child(firebaseUser.getUid()).setValue(true);


                }else {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(user.getId()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                            .child("followers").child(firebaseUser.getUid()).removeValue();

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class VH extends RecyclerView.ViewHolder {

        public TextView username;
        public TextView fullname;
        public CircleImageView imageProfile;
        public Button btnFollow;


        public VH(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.userName);
            fullname=itemView.findViewById(R.id.fullname);
            imageProfile=itemView.findViewById(R.id.image_profile);
            btnFollow=itemView.findViewById(R.id.btn_follow);

        }
    }

    private void isFollowing(final String userid, final Button button){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(userid).exists()){
                    button.setText("following");

                }else {
                    button.setText("follow");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
