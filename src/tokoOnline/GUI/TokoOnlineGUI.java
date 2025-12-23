package tokoOnline.GUI;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TokoOnlineGUI extends JFrame {
    private Barang barang = new Barang();
    private DefaultTableModel model, modelLog;
    private JTable tabelProduk, tabelLogGudang;
    private JTextField txtSearch;
    private JButton btnNotif;

    Color primaryColor = new Color(41, 128, 185);
    Color successColor = new Color(39, 174, 96);
    Color warningColor = new Color(243, 156, 18);
    Color dangerColor = new Color(192, 57, 43);
    Color bgColor = new Color(236, 240, 241);

    public TokoOnlineGUI() {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        
        setTitle("Inventory Management System v2.0");
        setSize(1150, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(bgColor);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(primaryColor);
        headerPanel.setPreferredSize(new Dimension(100, 80));
        JLabel title = new JLabel("WHS - INVENTORY SYSTEM");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(title);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabs.addTab("Admin Inventory", panelAdmin());
        tabs.addTab("Petugas Gudang", panelGudang());

        add(headerPanel, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        
        updateTabel();
    }

    private ImageIcon getScaledIcon(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(path); 
            Image img = icon.getImage(); 
            Image newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH); 
            return new ImageIcon(newimg);
        } catch (Exception e) {
            System.out.println("Gagal memuat gambar: " + e.getMessage());
            return null;
        }
    }

    private String getWaktuSekarang() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
    }

    private JPanel panelAdmin() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topControlPanel = new JPanel(new BorderLayout());
        topControlPanel.setOpaque(false);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        leftPanel.setOpaque(false);
        
        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 35));
        
        btnNotif = new JButton("0");
        btnNotif.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNotif.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnNotif.setContentAreaFilled(false); 
        btnNotif.setBorderPainted(false);
        btnNotif.setFocusPainted(false);
        btnNotif.setOpaque(false);

        ImageIcon bellIcon = getScaledIcon("notification.png", 24, 24); 
        if(bellIcon != null) btnNotif.setIcon(bellIcon); 
        
        btnNotif.setHorizontalTextPosition(SwingConstants.RIGHT);

        btnNotif.addActionListener(e -> tampilkanNotifikasi());

        leftPanel.add(new JLabel("Cari: "));
        leftPanel.add(txtSearch);
        leftPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        leftPanel.add(btnNotif);
        
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { searchTable(txtSearch.getText()); }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnPanel.setOpaque(false);
        JButton btnAdd = createStyledButton("Tambah Produk", successColor);
        JButton btnStockIn = createStyledButton("Barang Masuk (+)", primaryColor);
        JButton btnUpdate = createStyledButton("Update Harga", warningColor);
        
        btnPanel.add(btnAdd);
        btnPanel.add(btnStockIn);
        btnPanel.add(btnUpdate);

        topControlPanel.add(leftPanel, BorderLayout.WEST);
        topControlPanel.add(btnPanel, BorderLayout.EAST);

        setupTable();
        JScrollPane scroll = new JScrollPane(tabelProduk);

        btnAdd.addActionListener(e -> {
            try {
                String nama = JOptionPane.showInputDialog("Nama Produk:");
                if (nama != null && !nama.isEmpty()) {
                    int stk = Integer.parseInt(JOptionPane.showInputDialog("Stok Awal:"));
                    int hrg = Integer.parseInt(JOptionPane.showInputDialog("Harga:"));
                    barang.setNamaBarang(nama);
                    barang.setStok(stk);
                    barang.setHarga(hrg);
                    updateTabel();
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Input tidak valid!"); }
        });

        btnStockIn.addActionListener(e -> {
            int row = tabelProduk.getSelectedRow();
            if(row != -1) {
                int modelRow = tabelProduk.convertRowIndexToModel(row);
                String val = JOptionPane.showInputDialog("Jumlah Masuk:");
                if (val != null) {
                    barang.editStok(modelRow, barang.getStok(modelRow) + Integer.parseInt(val));
                    barang.setWaktuMasuk(modelRow, getWaktuSekarang());
                    updateTabel();
                }
            } else { JOptionPane.showMessageDialog(this, "Pilih barang di tabel dulu!"); }
        });

        btnUpdate.addActionListener(e -> {
            int row = tabelProduk.getSelectedRow();
            if(row != -1) {
                int modelRow = tabelProduk.convertRowIndexToModel(row);
                String val = JOptionPane.showInputDialog("Harga Baru:", barang.getHarga(modelRow));
                if (val != null) {
                    barang.editHarga(modelRow, Integer.parseInt(val));
                    updateTabel();
                }
            }
        });

        panel.add(topControlPanel, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel panelGudang() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel actionPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        actionPanel.setOpaque(false);
        
        JPanel infoText = new JPanel(new GridLayout(2, 1));
        infoText.setOpaque(false);
        JLabel lblGudang = new JLabel("MODE PETUGAS GUDANG");
        lblGudang.setFont(new Font("Segoe UI", Font.BOLD, 18));
        JLabel lblDesc = new JLabel("Riwayat pengurangan stok barang.");
        infoText.add(lblGudang);
        infoText.add(lblDesc);

        JButton btnOut = createStyledButton("KURANGI STOK (BARANG KELUAR)", dangerColor);
        btnOut.setPreferredSize(new Dimension(200, 60));

        actionPanel.add(infoText);
        actionPanel.add(btnOut);

        String[] kolomLog = {"Waktu", "ID Barang", "Nama Barang", "Jumlah Keluar", "Keterangan"};
        modelLog = new DefaultTableModel(kolomLog, 0);
        tabelLogGudang = new JTable(modelLog);
        tabelLogGudang.setRowHeight(30);
        JScrollPane scrollLog = new JScrollPane(tabelLogGudang);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Riwayat Barang Keluar"));

        btnOut.addActionListener(e -> {
            int row = tabelProduk.getSelectedRow();
            if (row != -1) {
                int modelRow = tabelProduk.convertRowIndexToModel(row);
                try {
                    String val = JOptionPane.showInputDialog("Jumlah Keluar:");
                    if (val != null) {
                        int qty = Integer.parseInt(val);
                        if (barang.getStok(modelRow) >= qty) {
                            String ket = JOptionPane.showInputDialog("Keterangan:");
                            if (ket == null) ket = "-";
                            barang.editStok(modelRow, barang.getStok(modelRow) - qty);
                            barang.setWaktuKeluar(modelRow, getWaktuSekarang());
                            modelLog.insertRow(0, new Object[]{getWaktuSekarang(), modelRow, barang.getNamaBarang(modelRow), qty, ket.toUpperCase()});
                            updateTabel();
                        } else { JOptionPane.showMessageDialog(this, "Stok tidak cukup!"); }
                    }
                } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Input tidak valid!"); }
            } else { JOptionPane.showMessageDialog(this, "Pilih barang di tabel Admin!"); }
        });

        panel.add(actionPanel, BorderLayout.NORTH);
        panel.add(scrollLog, BorderLayout.CENTER);
        return panel;
    }

    private void tampilkanNotifikasi() {
        StringBuilder pesan = new StringBuilder();
        int count = 0;
        for (int i = 0; i < barang.getJmlBarang(); i++) {
            int s = barang.getStok(i);
            if (s == 0) {
                pesan.append("❌ ").append(barang.getNamaBarang(i)).append(": STOK HABIS!\n\n");
                count++;
            } else if (s < 5) {
                pesan.append("⚠️ ").append(barang.getNamaBarang(i)).append(": Stok menipis (").append(s).append(").\n\n");
                count++;
            }
        }
        if (count == 0) {
            JOptionPane.showMessageDialog(this, "Semua stok aman! ✅", "Notifikasi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JTextArea textArea = new JTextArea(pesan.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            JScrollPane scroll = new JScrollPane(textArea);
            scroll.setPreferredSize(new Dimension(350, 200));
            JOptionPane.showMessageDialog(this, scroll, "Peringatan Stok", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void setupTable() {
        String[] kolom = {"ID", "Nama Produk", "Stok", "Harga", "Status", "Terakhir Masuk", "Terakhir Keluar"};
        model = new DefaultTableModel(kolom, 0);
        tabelProduk = new JTable(model);
        tabelProduk.setRowHeight(35);
        tabelProduk.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabelProduk.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                int modelRow = table.convertRowIndexToModel(row);
                int stokVal = (int) table.getModel().getValueAt(modelRow, 2);
                if (stokVal == 0) c.setForeground(dangerColor);
                else if (stokVal < 5) c.setForeground(warningColor);
                else c.setForeground(new Color(44, 62, 80));
                if (isSelected) c.setBackground(new Color(212, 230, 241));
                else c.setBackground(Color.WHITE);
                return c;
            }
        });
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void updateTabel() {
        model.setRowCount(0);
        int warningCount = 0;
        for (int i = 0; i < barang.getJmlBarang(); i++) {
            int s = barang.getStok(i);
            if (s < 5) warningCount++;
            String status = (s == 0) ? "HABIS" : (s < 5) ? "WARNING" : "AMAN";
            model.addRow(new Object[]{i, barang.getNamaBarang(i), s, "Rp " + barang.getHarga(i), status, barang.getWaktuMasuk(i), barang.getWaktuKeluar(i)});
        }
        
        if (btnNotif != null) {
            btnNotif.setText(String.valueOf(warningCount)); 
            // Ubah warna angka: merah jika ada peringatan, abu-abu jika 0
            btnNotif.setForeground(warningCount > 0 ? Color.RED : Color.GRAY);
        }
    }

    private void searchTable(String query) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tabelProduk.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TokoOnlineGUI().setVisible(true));
    }
}