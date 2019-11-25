package kr.co.ajsoft.imoim.GroupFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;

import kr.co.ajsoft.imoim.R;


public class ChatGroupFragment extends Fragment {

    FloatingActionButton actionButton;
    Context context;
    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chat_group, container, false);

        actionButton=view.findViewById(R.id.floating_button_chatroom);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("그룹 이름 입력");

                final EditText groupNameField=new EditText(getActivity());

                builder.setView(groupNameField);

                builder.setPositiveButton("만들기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String groupName=groupNameField.getText().toString();
                        if(TextUtils.isEmpty(groupName)){
                            Toast.makeText(context, "그룹이름을 입력해주세요.", Toast.LENGTH_SHORT).show();

                        }else{

                            CreateNewGroup(groupName);

                        }

                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();

                    }
                });

                builder.create().show();

            }
        });

        return view;
    }

    private void CreateNewGroup(final String groupName) {

        reference.child("Groups").child(groupName).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Toast.makeText(context, groupName+" 채팅 그룹이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


}
