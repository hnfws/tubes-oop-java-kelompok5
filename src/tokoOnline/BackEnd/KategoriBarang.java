package com.mycompany.tokoOnline;

import java.util.ArrayList;

public class KategoriBarang {

    private ArrayList<String> kategori = new ArrayList<>();

    public void tambahKategori(String kategoriBarang) {
        kategori.add(kategoriBarang);
    }

    public String getKategori(int id) {
        return kategori.get(id);
    }

    public void setKategori(int id, String kategoriBarang) {
        kategori.set(id, kategoriBarang);
    }
}
