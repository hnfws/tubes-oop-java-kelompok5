# Sistem Inventori Toko Online
Proyek ini adalah aplikasi manajemen inventori berbasis desktop yang dikembangkan menggunakan **Java Swing** untuk memenuhi Tugas Besar mata kuliah Pemrograman Java Berbasis Framework. Aplikasi ini dirancang untuk mempermudah pengelolaan stok, pemantauan barang masuk/keluar, serta memberikan notifikasi otomatis saat stok menipis.

# Anggota Kelompok 5
**Muhana Fawwas Sausan** 
**Nisa Alfiatur Rohmah** 
**Fatimah Nur'aini** 

# Fitur Utama

Sesuai dengan tema **Sistem Inventori Toko Online**, aplikasi ini memiliki fitur:
**Manajemen Produk:** Menambah data produk baru, memperbarui harga, dan mengelola stok melalui method khusus.
**Panel Admin & Gudang:** Pemisahan antarmuka untuk tugas administratif dan pencatatan log barang keluar secara *real-time*.
**Sistem Notifikasi Stok:** Ikon lonceng interaktif yang menunjukkan jumlah barang dengan stok kritis (di bawah 5 unit).
**Pencarian Cepat:** Fitur pencarian produk menggunakan teknik *string compare* pada tabel inventori.
**Dashboard Statistik:** Ringkasan visual untuk total jenis produk, total barang masuk, dan total barang keluar.


## 🛠️ Implementasi OOP & Teknis

Aplikasi ini menerapkan prinsip-prinsip utama PBO sesuai instruksi:
**1. Inheritance & Relasi:** Penggunaan kelas `KategoriBarang` untuk mendukung pengelompokan produk.
**2. Encapsulation:** Penyembunyian data pada atribut kelas `Barang` dengan akses melalui getter dan setter.
**3. Exception Handling:** Implementasi `StokHabisException` untuk menangani error saat stok tidak mencukupi.
**4. Data Handling:** Penggunaan `ArrayList` untuk penyimpanan data produk yang dinamis.
**5. Swing GUI:** Penggunaan `BorderLayout`, `JTable`, dan `JTabbedPane` untuk antarmuka yang interaktif.

# 📂 Struktur Repositori

sistem_inventory
├── src/
│   └── tokoOnline/
│       ├── BackEnd/            # Logika Inti & Model (Barang, Kategori)
│       ├── BarangController/   # Penghubung Logic & UI
│       └── GUI/                # Antarmuka Java Swing
├── notification.png            # Aset Ikon Notifikasi
├── .gitignore                  # File pengabaian (out/, *.class)
└── README.md                   # Dokumentasi proyek

# ⚙️ Cara Menjalankan

**1. Compile Proyek:**
```powershell
javac -d out src/tokoOnline/BackEnd/*.java src/tokoOnline/BarangController/*.java src/tokoOnline/GUI/*.java
```
**2. Jalankan Aplikasi:**
```powershell
java -cp out tokoOnline.GUI.TokoOnlineGUI
```

**Catatan:** Proyek ini dikembangkan menggunakan kontrol versi GitHub. Riwayat commit dan kolaborasi anggota dapat dilihat pada tab *Commit History*.

