package tokoOnline;

public class BarangController {

    private Barang barang;

    public BarangController() {
        barang = new Barang();
    }

    // ===== GET DATA =====
    public int getJumlahBarang() {
        return barang.getJmlBarang();
    }

    public String getNamaBarang(int id) {
        return barang.getNamaBarang(id);
    }

    public int getStok(int id) {
        return barang.getStok(id);
    }

    public int getHarga(int id) {
        return barang.getHarga(id);
    }

    public String getWaktuMasuk(int id) {
        return barang.getWaktuMasuk(id);
    }

    public String getWaktuKeluar(int id) {
        return barang.getWaktuKeluar(id);
    }

    // ===== TAMBAH BARANG =====
    public void tambahBarang(String nama, int stok, int harga) {
        barang.setNamaBarang(nama);
        barang.setStok(stok);
        barang.setHarga(harga);
    }

    // ===== BARANG MASUK =====
    public boolean barangMasuk(int id, int jumlah, String waktu) {
        if (jumlah <= 0) return false;

        int stokBaru = barang.getStok(id) + jumlah;
        barang.editStok(id, stokBaru);
        barang.setWaktuMasuk(id, waktu);
        return true;
    }

    // ===== BARANG KELUAR =====
    public boolean barangKeluar(int id, int jumlah, String waktu) {
        if (jumlah <= 0) return false;

        int stokSekarang = barang.getStok(id);
        if (stokSekarang < jumlah) return false;

        barang.editStok(id, stokSekarang - jumlah);
        barang.setWaktuKeluar(id, waktu);
        return true;
    }

    // ===== STATUS =====
    public String getStatus(int id) {
        int stok = barang.getStok(id);
        if (stok == 0) return "HABIS";
        if (stok < 5) return "MENIPIS";
        return "AMAN";
    }
}
