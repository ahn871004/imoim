package kr.co.ajsoft.imoim.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import kr.co.ajsoft.imoim.GroupFragment.ChatGroupFragment;
import kr.co.ajsoft.imoim.GroupFragment.ChatUserFragment;

public class TablayoutAdapter extends FragmentPagerAdapter {

    Fragment[] fragments=new Fragment[2];
    String[] pageTitles=new String[]{"ChatRoom","Users"};



    public TablayoutAdapter(@NonNull FragmentManager fm) {
        super(fm);

        fragments[0]=new ChatGroupFragment();
        fragments[1]=new ChatUserFragment();
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    //뷰페이저와 연동된 탭레이아웃에 탭버튼들의
    //글씨를 리턴해주는 메소드
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return pageTitles[position];
    }


}
