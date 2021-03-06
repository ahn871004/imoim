package kr.co.ajsoft.imoim.MainFragment;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import kr.co.ajsoft.imoim.Adapter.UserAdapter;
import kr.co.ajsoft.imoim.MainActivity;
import kr.co.ajsoft.imoim.Model.User;
import kr.co.ajsoft.imoim.R;


public class SearchFragment extends Fragment implements MainActivity.OnBackPressedListener {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private ArrayList<User> users;
    HomeFragment homeFragment;


    EditText searchBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_search, container,                            false);

        recyclerView=view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchBar=view.findViewById(R.id.search_bar);

        users=new ArrayList<>();
        userAdapter=new UserAdapter(getContext(),users);
        recyclerView.setAdapter(userAdapter);

        homeFragment=new HomeFragment();

        readUsers();
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searchUsers(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void searchUsers(String s){
        Query query= FirebaseDatabase.getInstance().getReference("Users").orderByChild("username").startAt(s).endAt(s+"\uf8ff");


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user=snapshot.getValue(User.class);
                    users.add(user);
                }
                userAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readUsers(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(searchBar.getText().toString().equals("")){
                    users.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        User user=snapshot.getValue(User.class);
                        users.add(user);

                    }
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
