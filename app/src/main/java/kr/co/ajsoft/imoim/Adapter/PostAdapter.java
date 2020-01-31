package kr.co.ajsoft.imoim.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

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

import kr.co.ajsoft.imoim.CommentsActivity;
import kr.co.ajsoft.imoim.MainFragment.PostDetailFragment;
import kr.co.ajsoft.imoim.MainFragment.ProfileFragment;
import kr.co.ajsoft.imoim.Model.Post;
import kr.co.ajsoft.imoim.Model.User;
import kr.co.ajsoft.imoim.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.VH>{

    public Context context;
    public ArrayList<Post> posts;

    private FirebaseUser firebaseUser;

    public PostAdapter(Context context, ArrayList<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.post_item,viewGroup,false);

        return new PostAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VH viewHolder, int position) {

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        final Post post= posts.get(position);


        Glide.with(context).load(post.getPostimage()).into(viewHolder.postImage);


        if(post.getDescription().equals("")){
            viewHolder.description.setVisibility(View.VISIBLE);

        }else{
            viewHolder.description.setVisibility(View.VISIBLE);
            viewHolder.description.setText(post.getDescription());

        }

        publisherInfo(viewHolder.imagProfile,viewHolder.userName,viewHolder.publisher,post.getPublisher());
        isLiked(post.getPostid(),viewHolder.like);
        numLikes(viewHolder.likes,post.getPostid());
//        getComments(post.getPostid(),viewHolder.comments);
        isSaved(post.getPostid(),viewHolder.save);

        viewHolder.imagProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor= context.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",post.getPublisher());
                editor.apply();

                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
            }
        });

        viewHolder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor= context.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",post.getPublisher());
                editor.apply();

                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
            }
        });

        viewHolder.publisher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor= context.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",post.getPublisher());
                editor.apply();

                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
            }
        });

        viewHolder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor= context.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("postid",post.getPostid());
                editor.apply();

                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PostDetailFragment()).commit();
            }
        });



        viewHolder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.save.getTag().equals("save")){
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid())
                            .child(post.getPostid()).setValue(true);

                }else{
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid())
                            .child(post.getPostid()).removeValue();

                }
            }
        });

        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid())
                            .child(firebaseUser.getUid()).setValue(true);

                }else {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid())
                            .child(firebaseUser.getUid()).removeValue();

                }
            }
        });

//        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(context, CommentsActivity.class);
//                intent.putExtra("postid",post.getPostid());
//                intent.putExtra("publisherid",post.getPublisher());
//                context.startActivity(intent);
//            }
//        });

//        viewHolder.comments.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(context, CommentsActivity.class);
//                intent.putExtra("postid",post.getPostid());
//                intent.putExtra("publisherid",post.getPublisher());
//                context.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class VH extends RecyclerView.ViewHolder{

        public ImageView imagProfile,postImage,like,comment,save;
        public TextView userName,likes,publisher,description,comments;

        public VH(@NonNull View itemView) {
            super(itemView);

            imagProfile=itemView.findViewById(R.id.image_profile);
            postImage=itemView.findViewById(R.id.post_image);
            like=itemView.findViewById(R.id.like);
//            comment=itemView.findViewById(R.id.comment);
            save=itemView.findViewById(R.id.save);

            userName=itemView.findViewById(R.id.username);
            likes=itemView.findViewById(R.id.likes);
            publisher=itemView.findViewById(R.id.publisher);
            description=itemView.findViewById(R.id.description);
//            comments=itemView.findViewById(R.id.comments);



        }
    }

//    private void getComments(String postid, final TextView comments){
//        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Comments").child(postid);
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                comments.setText("View All "+dataSnapshot.getChildrenCount()+" Comments");
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }

    private void isLiked(String postid, final ImageView imageView){

        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.ic_liked);
                    imageView.setTag("liked");

                }else {
                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

        private void numLikes(final TextView likes, String postid){
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Likes")
                    .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount()+"likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void publisherInfo(final ImageView imageProfile, final TextView userName, final TextView publisher, String userid){

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                Glide.with(context).load(user.getImageurl()).into(imageProfile);
                userName.setText(user.getUsername());
                publisher.setText(user.getUsername());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void isSaved(final String postid, final ImageView imageView){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Saves")
                .child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(postid).exists()){
                    imageView.setImageResource(R.drawable.ic_savepost);
                    imageView.setTag("saved");


                }else {
                    imageView.setTag("save");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}