package kr.co.ajsoft.imoim.MainFragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import kr.co.ajsoft.imoim.Adapter.TablayoutAdapter;
import kr.co.ajsoft.imoim.MainActivity;
import kr.co.ajsoft.imoim.R;


public class GroupFragment extends Fragment {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private TabLayout tabLayout;

    private ViewPager pager;
    private TablayoutAdapter adapter;
    private Toolbar toolbar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_group, container, false);

        toolbar=view.findViewById(R.id.toolbar);
        MainActivity activity= (MainActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        drawerLayout=view.findViewById(R.id.layout_drawer);
        //drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,"채팅","채팅");
        tabLayout=view.findViewById(R.id.layout_tab);
        pager=view.findViewById(R.id.pager);
        adapter=new TablayoutAdapter(activity.getSupportFragmentManager());
        pager.setAdapter(adapter);

        tabLayout.setupWithViewPager(pager);


        return view;

    }

}
