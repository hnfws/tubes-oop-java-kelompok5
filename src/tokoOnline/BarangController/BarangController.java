package tokoOnline.BarangController;
import tokoOnline.BackEnd.Barang;
import tokoOnline.BackEnd.StokHabisException;

public class BarangController {
    private Barang barang;
    private int totalQtyMasuk = 0;
    private int totalQtyKeluar = 0;

    public BarangController(Barang barang) {
        this.barang = barang;
    }

    public void prosesTambahProduk(String nama, String kategori, int stok, int harga) {
        barang.tambahDataBaru(nama, kategori, stok, harga);
    }

    // Menambahkan throws StokHabisException agar bisa ditangkap oleh GUI
    public void prosesBarangMasuk(int modelRow, int qty) throws StokHabisException {
        barang.editStok(modelRow, barang.getStok(modelRow) + qty);
        barang.setWaktuMasuk(modelRow, java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")));
        totalQtyMasuk += qty;
    }

    public void prosesBarangKeluar(int modelRow, int qty) throws StokHabisException {
        barang.editStok(modelRow, barang.getStok(modelRow) - qty);
        barang.setWaktuKeluar(modelRow, java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")));
        totalQtyKeluar += qty;
    }

    public int getTotalJenisProduk() { return barang.getJmlBarang(); }
    public int getTotalQtyMasuk() { return totalQtyMasuk; }
    public int getTotalQtyKeluar() { return totalQtyKeluar; }
    public Barang getModel() { return barang; }
}
