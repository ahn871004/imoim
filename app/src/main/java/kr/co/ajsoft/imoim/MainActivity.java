package kr.co.ajsoft.imoim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.security.acl.Group;

import kr.co.ajsoft.imoim.GroupFragment.ChatUserFragment;
import kr.co.ajsoft.imoim.MainFragment.GroupFragment;
import kr.co.ajsoft.imoim.MainFragment.HomeFragment;
import kr.co.ajsoft.imoim.MainFragment.NotificationFragment;
import kr.co.ajsoft.imoim.MainFragment.PostDetailFragment;
import kr.co.ajsoft.imoim.MainFragment.ProfileFragment;
import kr.co.ajsoft.imoim.MainFragment.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    Fragment fragment=null;

    private long pressedTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView=findViewById(R.id.bottom_navi);
        navigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


        Bundle intent=getIntent().getExtras();
        if(intent!=null){

            String publisher=intent.getString("publisherid");

            SharedPreferences.Editor editor=getSharedPreferences("PREFS",MODE_PRIVATE).edit();
            editor.putString("profileid",publisher);
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
        }else{

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();

        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){

                        case R.id.navi_home :
                            fragment=new HomeFragment();
                            item.setChecked(true);

                            break;

                        case R.id.navi_search :
                            fragment=new SearchFragment();
                            item.setChecked(true);
                            break;

                        case R.id.navi_group :

                            fragment=new GroupFragment();
                            item.setChecked(true);


                            break;

                        case R.id.navi_noti :
                            fragment=new NotificationFragment();
                            item.setChecked(true);
                            break;

                        case R.id.navi_person :
                            item.setChecked(true);
                            SharedPreferences.Editor editor=getSharedPreferences("PREFS",MODE_PRIVATE).edit();
                            editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            editor.apply();
                            fragment=new ProfileFragment();
                            break;

                    }
                    if(fragment!=null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();

                    }

                    return false;
                }
            };

    public interface OnBackPressedListener{
        public void onBack();

    }

    private OnBackPressedListener mBackListener;

    public void setOnBackPressedListener(OnBackPressedListener listener){
        mBackListener=listener;



    }

    //뒤로가기 버튼누르면 앱 종료 & 다른 Fragment에서 뒤로가기 누르면 main으로 오는 메소드
    @Override
    public void onBackPressed(){
        if(mBackListener!=null){
            mBackListener.onBack();
            Log.e("!!!","Listener is not null");


        }else{
            Log.e("!!!","Listener is null");
            if(pressedTime==0){
                Snackbar.make(findViewById(R.id.main_layout),"한 번 더 누르면 종료됩니다.",Snackbar.LENGTH_LONG).show();
                pressedTime=System.currentTimeMillis();

            }else{
                int seconds=(int)(System.currentTimeMillis()-pressedTime);
                if(seconds>2000){
                    Snackbar.make(findViewById(R.id.main_layout),"한 번 더 누르면 종료됩니다.",Snackbar.LENGTH_LONG).show();
                    pressedTime=0;

                }else{
                    super.onBackPressed();
                    Log.e("!!!","onBackPressed : finish, killProcess");
                    finish();
                    //android.os.Process.killProcess(android.os.Process.myPid());
                    finishAffinity();
                    System.runFinalization();
                    System.exit(0);

                }

            }

        }

    }
}