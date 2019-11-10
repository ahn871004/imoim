package kr.co.ajsoft.imoim.Model;

public class Comment {

    private String commnet;
    private String publisher;

    public Comment(String commnet, String publisher) {
        this.commnet = commnet;
        this.publisher = publisher;
    }

    public Comment() {
    }

    public String getCommnet() {
        return commnet;
    }

    public void setCommnet(String commnet) {
        this.commnet = commnet;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
