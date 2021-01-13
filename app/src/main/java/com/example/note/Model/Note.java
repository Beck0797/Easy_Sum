package com.example.note.Model;

public class Note {
    private int id;
    private String content;
    private int amount;

    public Note(String content, int amount) {
        this.content = content;
        this.amount = amount;
    }

    public Note(int id, String content, int amount) {
        this.id = id;
        this.content = content;
        this.amount = amount;
    }

    public Note() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getContent() {
        return content;
    }

    public int getAmount() {
        return amount;
    }

}
