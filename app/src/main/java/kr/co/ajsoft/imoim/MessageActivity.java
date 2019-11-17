package kr.co.ajsoft.imoim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import kr.co.ajsoft.imoim.Model.Chat;

public class MessageActivity extends AppCompatActivity {

    private String destinationid;
    private EditText editText;
    private Button buttonSend;

    private RecyclerView recyclerView;

    private String id;
    private String chatRoomid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //채팅을 요청하는 id
        id=FirebaseAuth.getInstance().getCurrentUser().getUid();
        //채팅을 받은 id
        destinationid=getIntent().getStringExtra("destinationid");
        editText=findViewById(R.id.et);
        buttonSend=findViewById(R.id.btn_send);
        recyclerView=findViewById(R.id.message_recyclerview);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chat chat=new Chat();
                chat.users.put(id,true);
                chat.users.put(destinationid,true);

                if(chatRoomid==null){
                    //buttonSend.setEnabled(false);
                    //ChatRoom 이름 만들어서 생성
                    FirebaseDatabase.getInstance().getReference().child("ChatRooms").
                            push().setValue(chat).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            checkChatRoom();

                        }
                    });
                }else{
                    Chat.Comment comment=new Chat.Comment();
                    //comment.id=id;
                    //comment.message=editText.getText().toString();
                    FirebaseDatabase.getInstance().getReference().child("ChatRooms").child(chatRoomid).child("comments").push().setValue(comment);


                }


            }
        });



    }

    void checkChatRoom(){

        //채팅방 중복여부 확인
        FirebaseDatabase.getInstance().getReference().child("ChatRomms").orderByChild("user/"+id).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    Chat chat=item.getValue(Chat.class);
                    if(chat.users.containsKey(destinationid)){
                        chatRoomid=item.getKey();
                        //buttonSend.setEnabled(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
                        recyclerView.setAdapter(new RecyclerViewAdapter());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        List<Chat.Comment> comments;

        public RecyclerViewAdapter(){

            comments=new ArrayList<>();

            FirebaseDatabase.getInstance().getReference().child("ChatRoom").child(chatRoomid).child("comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    comments.clear();

                    for(DataSnapshot item : dataSnapshot.getChildren()){
                        comments.add(item.getValue(Chat.Comment.class));

                    }
                    notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);


            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            ((MessageViewHolder)holder).textView_mymsg.setText(comments.get(position).message);

        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        private class MessageViewHolder extends RecyclerView.ViewHolder {

            public TextView textView_mymsg;

            public MessageViewHolder(View view) {
                super(view);
                textView_mymsg=(TextView)view.findViewById(R.id.tv_message_item);

            }
        }
    }

}
