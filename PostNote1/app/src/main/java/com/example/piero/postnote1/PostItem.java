package com.example.piero.postnote1;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;

import java.io.Serializable;

public class PostItem implements Parcelable, Serializable {
    private String titolo,testo;
    private int id, secondi;

    public PostItem() {

    }
    public PostItem(String titolo, String testo, int id) {
        this.titolo = titolo;
        this.testo = testo;
        this.id = id;
    }

    public PostItem(String titolo, String testo, int secondi, int id) {
        this.secondi = secondi;
        this.titolo = titolo;
        this.testo = testo;
        this.id = id;
    }

    public PostItem(Editable text, Editable text1, int id) {
    }

    public int getSecondi() {return secondi;}

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

    public PostItem(Parcel in) {
        super();
        readFromParcel(in);
    }

    public static final Parcelable.Creator<PostItem> CREATOR = new Parcelable.Creator<PostItem>() {
        public PostItem createFromParcel(Parcel in) {
            return new PostItem(in);
        }

        public PostItem[] newArray(int size) {

            return new PostItem[size];
        }

    };

    public void readFromParcel(Parcel in) {
        testo = in.readString();
        titolo = in.readString();
        id = in.readInt();
    }
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(testo);
        dest.writeString(titolo);
        dest.writeInt(id);
    }

}
