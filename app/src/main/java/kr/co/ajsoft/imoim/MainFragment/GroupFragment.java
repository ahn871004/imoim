package kr.co.ajsoft.imoim.MainFragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import kr.co.ajsoft.imoim.Adapter.TablayoutAdapter;
import kr.co.ajsoft.imoim.GroupFragment.ChatGroupFragment;
import kr.co.ajsoft.imoim.GroupFragment.ChatUserFragment;
import kr.co.ajsoft.imoim.MainActivity;
import kr.co.ajsoft.imoim.R;


public class GroupFragment extends Fragment implements MainActivity.OnBackPressedListener{

    private TabLayout tabLayout;
    private ViewPager pager;
    private TablayoutAdapter adapter;
    private Toolbar toolbar;

    HomeFragment homeFragment;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_group, container, false);

        toolbar=view.findViewById(R.id.toolbar);
        MainActivity activity= (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().hide();

        tabLayout=view.findViewById(R.id.layout_tab);
        pager=view.findViewById(R.id.pager);

        adapter=new TablayoutAdapter(activity.getSupportFragmentManager());
        pager.setAdapter(adapter);

        tabLayout.setupWithViewPager(pager);

        homeFragment=new HomeFragment();


        return view;

    }




    @Override
    public void onBack() {
        Log.e("Other","onBack");
        MainActivity activity=(MainActivity)getActivity();
        activity.setOnBackPressedListener(null);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,homeFragment).commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("Other","onAttach()");
        ((MainActivity)context).setOnBackPressedListener(this);
    }

}
