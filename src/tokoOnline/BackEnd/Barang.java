package com.mycompany.tokoOnline;

import java.util.ArrayList;

public class Barang {

    private ArrayList<String> namaBarang = new ArrayList<>();
    private ArrayList<Integer> stok = new ArrayList<>();
    private ArrayList<Integer> harga = new ArrayList<>();
    private ArrayList<String> terakhirMasuk = new ArrayList<>();
    private ArrayList<String> terakhirKeluar = new ArrayList<>();

    // 🔹 INTEGRASI KATEGORI
    private KategoriBarang kategoriBarang = new KategoriBarang();

    public Barang() {
        tambahDataBaru("Kalung Mutiara", "Aksesoris", 10, 9000);
        tambahDataBaru("Cincin Manik", "Cincin", 20, 2000);
        tambahDataBaru("Gelang Tali", "Gelang", 1, 6000);
    }

    private void tambahDataBaru(String nama, String kategori, int s, int h) {
        namaBarang.add(nama);
        stok.add(s);
        harga.add(h);
        terakhirMasuk.add("-");
        terakhirKeluar.add("-");
        kategoriBarang.tambahKategori(kategori);
    }

    public int getJmlBarang() { return namaBarang.size(); }
    public String getNamaBarang(int id) { return namaBarang.get(id); }
    public int getStok(int id) { return stok.get(id); }
    public int getHarga(int id) { return harga.get(id); }

    public void setNamaBarang(String nama, String kategori) {
        namaBarang.add(nama);
        stok.add(0);
        harga.add(0);
        terakhirMasuk.add("-");
        terakhirKeluar.add("-");
        kategoriBarang.tambahKategori(kategori);
    }

    public String getKategori(int id) {
        return kategoriBarang.getKategori(id);
    }

    public void editStok(int id, int jumlah) throws StokHabisException {
        if (jumlah < 0) {
            throw new StokHabisException(namaBarang.get(id));
        }
        stok.set(id, jumlah);
    }

    public void editHarga(int id, int h) {
        harga.set(id, h);
    }

    public String getWaktuMasuk(int id) { return terakhirMasuk.get(id); }
    public void setWaktuMasuk(int id, String waktu) { terakhirMasuk.set(id, waktu); }

    public String getWaktuKeluar(int id) { return terakhirKeluar.get(id); }
    public void setWaktuKeluar(int id, String waktu) { terakhirKeluar.set(id, waktu); }
}
