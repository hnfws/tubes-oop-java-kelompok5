package tokoOnline.BarangController;

import java.util.Date;
import tokoOnline.BackEnd.Barang;
import tokoOnline.BackEnd.StokHabisException;

public class BarangController {
    private Barang barang;
    private int totalQtyMasuk = 0;
    private int totalQtyKeluar = 0;

    public BarangController(Barang barang) {
        this.barang = barang;
    }

   // --- Tambah produk baru dengan tanggal ---
    public void prosesTambahProduk(String nama, String kategori, int stok, int harga, Date tanggal) {
        barang.tambahDataBaru(nama, kategori, stok, harga, tanggal);
    }

    // --- Barang masuk dengan tanggal ---
    public void prosesBarangMasuk(int modelRow, int qty, Date tanggal) throws StokHabisException {
        barang.editStok(modelRow, barang.getStok(modelRow) + qty);
        barang.setWaktuMasuk(modelRow, tanggal);
        totalQtyMasuk += qty;
    }

    // --- Barang keluar dengan tanggal ---
    public void prosesBarangKeluar(int modelRow, int qty, Date tanggal) throws StokHabisException {
        barang.editStok(modelRow, barang.getStok(modelRow) - qty);
        barang.setWaktuKeluar(modelRow, tanggal);
        totalQtyKeluar += qty;
    }

    public int getTotalJenisProduk() { return barang.getJmlBarang(); }
    public int getTotalQtyMasuk() { return totalQtyMasuk; }
    public int getTotalQtyKeluar() { return totalQtyKeluar; }
    public Barang getModel() { return barang; }
}