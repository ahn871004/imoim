package kr.co.ajsoft.imoim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser!=null){
            startActivity(new Intent(StartActivity.this,MainActivity.class));

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


    }

    public void clickLogin(View view) {
        startActivity(new Intent(StartActivity.this,LoginActivity.class));

    }

    public void clickRegister(View view) {
        startActivity(new Intent(StartActivity.this,RegisterActivity.class));

    }
}
