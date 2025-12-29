package tokoOnline.BarangController;

import java.util.Date;
import tokoOnline.BackEnd.Barang;
import tokoOnline.BackEnd.StokHabisException;

public class BarangController {
    private Barang barang;
    private int totalQtyMasuk = 0;
    private int totalQtyKeluar = 0;
    
    // Tambahan untuk mencatat tanggal terakhir secara global
    private Date tglTerakhirMasuk = null;
    private Date tglTerakhirKeluar = null;

    public BarangController(Barang barang) {
        this.barang = barang;
    }

    public void prosesTambahProduk(String nama, String kategori, int stok, int harga, Date tanggal) {
        barang.tambahDataBaru(nama, kategori, stok, harga, tanggal);
        if (stok > 0) {
            totalQtyMasuk += stok;
            tglTerakhirMasuk = tanggal;
        }
    }

    public void prosesBarangMasuk(int modelRow, int qty, Date tanggal) throws StokHabisException {
        barang.editStok(modelRow, barang.getStok(modelRow) + qty);
        barang.setWaktuMasuk(modelRow, tanggal);
        totalQtyMasuk += qty;
        tglTerakhirMasuk = tanggal; // Update tgl masuk global
    }

    public void prosesBarangKeluar(int modelRow, int qty, Date tanggal) throws StokHabisException {
        barang.editStok(modelRow, barang.getStok(modelRow) - qty);
        barang.setWaktuKeluar(modelRow, tanggal);
        totalQtyKeluar += qty;
        tglTerakhirKeluar = tanggal; // Update tgl keluar global
    }

    public int getTotalJenisProduk() { return barang.getJmlBarang(); }
    public int getTotalQtyMasuk() { return totalQtyMasuk; }
    public int getTotalQtyKeluar() { return totalQtyKeluar; }
    public Date getTglTerakhirMasuk() { return tglTerakhirMasuk; }
    public Date getTglTerakhirKeluar() { return tglTerakhirKeluar; }
    public Barang getModel() { return barang; }
}