package kr.co.ajsoft.imoim.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import kr.co.ajsoft.imoim.Model.Post;
import kr.co.ajsoft.imoim.R;

public class MyPhotoAdapter extends RecyclerView.Adapter<MyPhotoAdapter.VH>{

    private Context context;
    private ArrayList<Post> mPost;

    public MyPhotoAdapter(Context context, ArrayList<Post> mPost) {
        this.context = context;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.photos_item,parent,false);

        return new MyPhotoAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH viewHolder, int position) {

        Post post=mPost.get(position);

        Glide.with(context).load(post.getPostimage()).into(viewHolder.post_image);
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class VH extends RecyclerView.ViewHolder{

        public ImageView post_image;

        public VH(@NonNull View itemView) {
            super(itemView);

            post_image=itemView.findViewById(R.id.post_image);

        }
    }

}
