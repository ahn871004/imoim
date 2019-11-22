package kr.co.ajsoft.imoim.MainFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.co.ajsoft.imoim.MainActivity;
import kr.co.ajsoft.imoim.R;


public class NotificationFragment extends Fragment implements MainActivity.OnBackPressedListener{

    HomeFragment homeFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_notification, container, false);

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
