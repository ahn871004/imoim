package kr.co.ajsoft.imoim.Model;

public class Comment {

    private String commnet;
    private String publisher;
    private String commentid;

    public Comment(String commnet, String publisher, String commentid) {
        this.commnet = commnet;
        this.publisher = publisher;
        this.commentid = commentid;
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

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }
}
