package kr.co.ajsoft.imoim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {

    MaterialEditText email, password;
    TextView textSignup;
    Button login;

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.et_email);
        password=findViewById(R.id.et_password);
        textSignup=findViewById(R.id.tv_signup);
        login=findViewById(R.id.btn_login2);

        auth=FirebaseAuth.getInstance();

        textSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pd= new ProgressDialog(LoginActivity.this);
                pd.setMessage("기다려주세요..");
                pd.show();

                String strEmail=email.getText().toString();
                String strPassword=password.getText().toString();

                if(TextUtils.isEmpty(strEmail)||TextUtils.isEmpty(strPassword)){
                    Toast.makeText(LoginActivity.this, "모든 빈칸의 항목을 기입해 주세요.", Toast.LENGTH_SHORT).show();

                }else{
                    //로그인 complete여부 확인하는 리스너
                    auth.signInWithEmailAndPassword(strEmail,strPassword)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid());

                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                //pd.dismiss();
                                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                pd.dismiss();

                                            }
                                        });

                                    }else{
                                        pd.dismiss();
                                        Toast.makeText(LoginActivity.this, "인증 실패하셨습니다.", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                }
            }
        });

    }



}
