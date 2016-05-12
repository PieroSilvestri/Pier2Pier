package com.example.piero.postnote1;

/**
 * Created by Adriano on 12/05/2016.
 */
public class PostItem {
    private String titolo,testo;
    private int id;

    public PostItem() {

    }

    public PostItem(String titolo, String testo, int id) {
        this.titolo = titolo;
        this.testo = testo;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
}
