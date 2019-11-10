package kr.co.ajsoft.imoim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import kr.co.ajsoft.imoim.MainFragment.HomeFragment;
import kr.co.ajsoft.imoim.MainFragment.NotificationFragment;
import kr.co.ajsoft.imoim.MainFragment.ProfileFragment;
import kr.co.ajsoft.imoim.MainFragment.SearchFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    Fragment fragment=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView=findViewById(R.id.bottom_navi);
        navigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();



    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){

                        case R.id.navi_home :
                            fragment=new HomeFragment();
                            break;

                        case R.id.navi_search :
                            fragment=new SearchFragment();

                            break;

                        case R.id.navi_add :
                            fragment=null;
                            startActivity(new Intent(MainActivity.this, PostActivity.class));

                            break;

                        case R.id.navi_noti :
                            fragment=new NotificationFragment();
                            break;

                        case R.id.navi_person :
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

}
