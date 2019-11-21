package kr.co.ajsoft.imoim.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kr.co.ajsoft.imoim.MainFragment.PostDetailFragment;
import kr.co.ajsoft.imoim.Model.Post;
import kr.co.ajsoft.imoim.R;

import static android.content.Context.MODE_PRIVATE;

public class MyPhotoAdapter extends RecyclerView.Adapter<MyPhotoAdapter.VH>{

    private Context context;
    private ArrayList<Post> posts;

    public MyPhotoAdapter(Context context, ArrayList<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.photos_item,parent,false);

        return new MyPhotoAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH viewHolder, int position) {

        final Post post= posts.get(position);

        Glide.with(context).load(post.getPostimage()).into(viewHolder.post_image);

        viewHolder.post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                editor.putString("postid", post.getPostid());
                editor.apply();

                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PostDetailFragment()).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class VH extends RecyclerView.ViewHolder{

        public ImageView post_image;

        public VH(@NonNull View itemView) {
            super(itemView);

            post_image=itemView.findViewById(R.id.post_image);

        }
    }

}
