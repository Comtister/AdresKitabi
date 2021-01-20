package com.example.adreskitab;

import android.graphics.Bitmap;

public class Adres {

    int id;
    byte adresImage[];
    String adresBaslik;
    String adresDetay;
    String adresKordinat;


    public Adres(int id,String adresBaslik, String adresDetay,String adresKordinat, byte adresImage[]) {
        this.id = id;
        this.adresImage = adresImage;
        this.adresBaslik = adresBaslik;
        this.adresDetay = adresDetay;
        this.adresKordinat = adresKordinat;
    }

    public Adres(String adresBaslik,String adresDetay,String adresKordinat,byte adresImage[]) {
        this.adresImage = adresImage;
        this.adresBaslik = adresBaslik;
        this.adresDetay = adresDetay;
        this.adresKordinat = adresKordinat;
    }

    public String getAdresKordinat() {
        return adresKordinat;
    }

    public void setAdresKordinat(String adresKordinat) {
        this.adresKordinat = adresKordinat;
    }

    public byte[] getAdresImage() {
        return adresImage;
    }

    public void setAdresImage(byte[] adresImage) {
        this.adresImage = adresImage;
    }

    public String getAdresBaslik() {
        return adresBaslik;
    }

    public void setAdresBaslik(String adresBaslik) {
        this.adresBaslik = adresBaslik;
    }

    public String getAdresDetay() {
        return adresDetay;
    }

    public void setAdresDetay(String adresDetay) {
        this.adresDetay = adresDetay;
    }
}
