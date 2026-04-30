package model;

public class Token{
    private String head;
    private String tail;

    protected Token(String h, String t){
        head = h;
        tail = t;
    }

    public String head(){
        return this.head;
    }

    public String tail(){
        return this.tail;
    }

    public void turn(){
        String tmp = this.tail;
        this.tail = this.head;
        this.head = tmp;
    }
}