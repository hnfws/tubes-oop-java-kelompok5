package com.mycompany.tokoOnline;

public class StokHabisException extends Exception {
    public StokHabisException(String namaBarang) {
        super("Stok barang \"" + namaBarang + "\" sudah HABIS!");
    }
}
