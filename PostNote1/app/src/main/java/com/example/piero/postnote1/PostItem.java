package com.example.piero.postnote1;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;

import java.io.Serializable;

public class PostItem implements Parcelable, Serializable {
    private String titolo,testo, creationDate;
    private int id;
    int audio, immagine = 0;
    private String audioPosition;
    private int flagged;

    public int isFlagged() {
        return flagged;
    }

    public void setFlagged(int flagged) {
        this.flagged = flagged;
    }



    public void setCreationDate(String date){
        creationDate = date;
    }

    public String getPosizioneAudio(){
        return  audioPosition;
    }

    public void setPosizioneAudio(String audioPosition){
        this.audioPosition = audioPosition;
    }

    public PostItem() {
        this.titolo = "";
        this.testo = "";
    }



    public PostItem(String titolo, String testo, String creationDate, int id, int audio, int immagine, int flagged) {
        this.titolo = titolo;
        this.testo = testo;
        this.creationDate = creationDate;
        this.id = id;
        this.audio = audio;
        this.immagine = immagine;
        this.flagged = flagged;
    }

    public PostItem(Editable text, Editable text1, int id) {
    }

    public int getId() {
        return id;
    }

    public int getAudio() {
        return audio;
    }

    public void setAudio(int audio) {
        this.audio = audio;
    }

    public int getImmagine() {
        return immagine;
    }

    public void setImmagine(int immagine) {
        this.immagine = immagine;
    }

    public String getcreationDate() {return creationDate;};

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
        creationDate = in.readString();
        id = in.readInt();
        audio = in.readInt();
        immagine = in.readInt();
    }
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(testo);
        dest.writeString(titolo);
        dest.writeString(creationDate);
        dest.writeInt(id);
        dest.writeInt(audio);
        dest.writeInt(immagine);
    }

}
