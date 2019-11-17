package kr.co.ajsoft.imoim.Model;

import java.util.HashMap;
import java.util.Map;

public class Chat {

    //채팅방 유저들
    public Map<String,Boolean> users=new HashMap<>();

    //채팅방 대화내용
    public Map<String,Comment> comments=new HashMap<>();
    public static class Comment{

        public String id;
        public String message;


    }


}
