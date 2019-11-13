package kr.co.ajsoft.imoim.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import kr.co.ajsoft.imoim.Model.Message;

public class ChatAdapter extends BaseAdapter {

    private ArrayList<Message> messageItems;
    private LayoutInflater layoutInflater;

    public ChatAdapter(ArrayList<Message> messageItems, LayoutInflater layoutInflater) {
        this.messageItems = messageItems;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return messageItems.size();
    }

    @Override
    public Object getItem(int position) {
        return messageItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Message item=messageItems.get(position);

        View itemView=null;





        return null;
    }
}
