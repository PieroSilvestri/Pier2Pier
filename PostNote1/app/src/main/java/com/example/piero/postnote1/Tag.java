package com.example.piero.postnote1;

import android.graphics.drawable.Icon;

public class Tag {

    private String nome;
    private String tagLink;

    public Tag() {

    }

    public Tag(String nome, String tagLink) {
        this.nome = nome;
        this.tagLink = tagLink;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getTagLink() {
        return tagLink;
    }

    public void setTagLink(String tagLink) {
        this.tagLink = tagLink;
    }
}
