package kr.co.ajsoft.imoim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText userName,fullname,email,password;
    TextView textLogin;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName=findViewById(R.id.et_username);
        fullname=findViewById(R.id.et_fullname);
        email=findViewById(R.id.et_email);
        password=findViewById(R.id.et_password);
        textLogin=findViewById(R.id.tv_login);

        auth=FirebaseAuth.getInstance();
        
        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
        
        
        
    }


    public void clickRegister2(View view) {
        pd=new ProgressDialog(RegisterActivity.this);
        pd.setMessage("기다려주세요..");
        pd.show();
        
        String strUsername=userName.getText().toString();
        String strFullname=fullname.getText().toString();
        String strEmail=email.getText().toString();
        String strPassword=password.getText().toString();
        
        if(TextUtils.isEmpty(strUsername) ||TextUtils.isEmpty(strFullname)|| TextUtils.isEmpty(strEmail) || TextUtils.isEmpty(strPassword)){
            Toast.makeText(this, "모든 빈칸의 항목을 기입해 주세요.", Toast.LENGTH_SHORT).show();
            
        }else if(strPassword.length()<6){
            Toast.makeText(this, "비밀번호는 6자리 이상입니다.", Toast.LENGTH_SHORT).show();

        }else{
            register(strUsername,strFullname,strEmail,strPassword);

        }
        
    }

    private void register(final String userName, final String fullname, String email, String password){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser=auth.getCurrentUser();
                            String userid=firebaseUser.getUid();

                            reference= FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                            HashMap<String,Object> hashMap=new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username", userName.toLowerCase());
                            hashMap.put("fullname",fullname);
                            hashMap.put("bio","");
                            hashMap.put("imageurl","https://firebasestorage.googleapis.com/v0/b/imoim-fdd76.appspot.com/o/seoul.png?alt=media&token=a233be5d-7188-44e6-8ec4-8190ce944e66");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        pd.dismiss();
                                        Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);

                                    }
                                }
                            });

                        }else{
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this, "이메일 또는 비밀번호 입력이 잘못 되었습니다.", Toast.LENGTH_SHORT).show();
                            
                        }

                    }
                });


    }

}
