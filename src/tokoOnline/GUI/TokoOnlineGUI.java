package tokoOnline.GUI;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.RowFilter;
import tokoOnline.BackEnd.Barang;
import tokoOnline.BarangController.*;


public class TokoOnlineGUI extends JFrame {
    private Barang barang = new Barang();
    private BarangController controller = new BarangController(barang); 
    
    private DefaultTableModel model, modelLog;
    private JTable tabelProduk, tabelLogGudang;
    private JTextField txtSearch;
    private JButton btnNotif;

    private JLabel lblTotalVal, lblMasukVal, lblKeluarVal;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    Color primaryColor = new Color(41, 128, 185);
    Color successColor = new Color(39, 174, 96);
    Color warningColor = new Color(243, 156, 18);
    Color dangerColor = new Color(192, 57, 43);
    Color bgColor = new Color(236, 240, 241);
    Color headerBlue = new Color(52, 152, 219); 

    public TokoOnlineGUI() {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        
        setTitle("Inventory Management System");
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

    // --- METODE PENDUKUNG IKON ---
    private ImageIcon getScaledIcon(String path, int width, int height) {
        try {
            java.net.URL imgURL = getClass().getResource("/tokoOnline/" + path);
            ImageIcon icon = (imgURL != null) ? new ImageIcon(imgURL) : new ImageIcon(path);
            if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) return null;
            Image img = icon.getImage(); 
            Image newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH); 
            return new ImageIcon(newimg);
        } catch (Exception e) { return null; }
    }

    private DefaultTableCellRenderer getBlueHeaderRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                setBackground(headerBlue); 
                setForeground(Color.WHITE); 
                setFont(new Font("Segoe UI", Font.BOLD, 15));
                setHorizontalAlignment(JLabel.CENTER); 
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.WHITE)); 
                return this;
            }
        };
    }

    private JPanel createCard(String title) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.setForeground(Color.GRAY);
        JLabel v = new JLabel("0");
        v.setFont(new Font("Segoe UI", Font.BOLD, 28));
        v.setForeground(headerBlue);
        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        if (title.equals("Total Produk")) lblTotalVal = v;
        else if (title.equals("Barang Masuk")) lblMasukVal = v;
        else if (title.equals("Barang Keluar")) lblKeluarVal = v;
        return card;
    }

    private String getWaktuSekarang() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
    }

    private JPanel panelAdmin() {
        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topControlPanel = new JPanel(new BorderLayout());
        topControlPanel.setOpaque(false);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        leftPanel.setOpaque(false);
        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 35));
        
        // --- LOGIKA IKON NOTIFIKASI ANDA ---
        btnNotif = new JButton("0");
        btnNotif.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNotif.setFocusPainted(false);
        btnNotif.setContentAreaFilled(false); 
        btnNotif.setBorderPainted(false);
        ImageIcon bellIcon = getScaledIcon("notification.png", 24, 24); 
        if(bellIcon != null) btnNotif.setIcon(bellIcon);
        btnNotif.addActionListener(e -> tampilkanNotifikasi());

        leftPanel.add(new JLabel("Cari: "));
        leftPanel.add(txtSearch);
        leftPanel.add(btnNotif);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnPanel.setOpaque(false);
        JButton btnAdd = createStyledButton("Tambah Produk", Color.WHITE, headerBlue);
        JButton btnStockIn = createStyledButton("Barang Masuk (+)", Color.WHITE, headerBlue);
        JButton btnUpdate = createStyledButton("Update Harga", Color.WHITE, headerBlue);
        btnPanel.add(btnAdd); btnPanel.add(btnStockIn); btnPanel.add(btnUpdate);

        topControlPanel.add(leftPanel, BorderLayout.WEST);
        topControlPanel.add(btnPanel, BorderLayout.EAST);

        JPanel dashboardPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        dashboardPanel.setOpaque(false);
        dashboardPanel.setPreferredSize(new Dimension(100, 100));
        dashboardPanel.add(createCard("Total Produk"));
        dashboardPanel.add(createCard("Barang Masuk"));
        dashboardPanel.add(createCard("Barang Keluar"));

        JPanel northContainer = new JPanel(new BorderLayout(0, 15));
        northContainer.setOpaque(false);
        northContainer.add(topControlPanel, BorderLayout.NORTH);
        northContainer.add(dashboardPanel, BorderLayout.CENTER);

        setupTableAdmin();
        JScrollPane scroll = new JScrollPane(tabelProduk);

        btnAdd.addActionListener(e -> tampilkanFormTambahProduk());
        
        btnStockIn.addActionListener(e -> {
    int row = tabelProduk.getSelectedRow();
    if(row != -1) {
        JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField txtJumlah = new JTextField();
        JSpinner spinTanggal = new JSpinner(new SpinnerDateModel());
        spinTanggal.setEditor(new JSpinner.DateEditor(spinTanggal, "dd-MM-yyyy"));

        form.add(new JLabel("Jumlah Masuk:")); form.add(txtJumlah);
        form.add(new JLabel("Tanggal Masuk:")); form.add(spinTanggal);

        if (JOptionPane.showConfirmDialog(this, form, "Barang Masuk", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                int qty = Integer.parseInt(txtJumlah.getText());
                Date tanggal = (Date) spinTanggal.getValue();
                controller.prosesBarangMasuk(tabelProduk.convertRowIndexToModel(row), qty, tanggal);
                updateTabel();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        }
    } else { JOptionPane.showMessageDialog(this, "Pilih barang terlebih dahulu!"); }
});


        btnUpdate.addActionListener(e -> {
            int row = tabelProduk.getSelectedRow();
            if(row != -1) {
                int modelRow = tabelProduk.convertRowIndexToModel(row);
                String val = JOptionPane.showInputDialog("Harga Baru:", controller.getModel().getHarga(modelRow));
                if (val != null) {
                    controller.getModel().editHarga(modelRow, Integer.parseInt(val));
                    updateTabel();
                }
            }
        });

        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { searchTable(txtSearch.getText()); }
        });

        panel.add(northContainer, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void tampilkanFormTambahProduk() {
    JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
    JTextField txtNama = new JTextField();
    JComboBox<String> cbKategori = new JComboBox<>(new String[]{"Aksesoris", "Cincin", "Gelang", "Kalung", "Anting"});
    JSpinner spinStok = new JSpinner(new SpinnerNumberModel(1, 0, 9999, 1));
    JSpinner spinHarga = new JSpinner(new SpinnerNumberModel(1000, 0, 100000000, 500));
    
    // --- Tambahan tanggal ---
    JSpinner spinTanggal = new JSpinner(new SpinnerDateModel());
    spinTanggal.setEditor(new JSpinner.DateEditor(spinTanggal, "dd-MM-yyyy"));

    form.add(new JLabel("Nama Produk:")); form.add(txtNama);
    form.add(new JLabel("Kategori:")); form.add(cbKategori);
    form.add(new JLabel("Stok:")); form.add(spinStok);
    form.add(new JLabel("Harga:")); form.add(spinHarga);
    form.add(new JLabel("Tanggal:")); form.add(spinTanggal);

    if (JOptionPane.showConfirmDialog(this, form, "Tambah Produk", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
        Date tanggal = (Date) spinTanggal.getValue();
        controller.prosesTambahProduk(txtNama.getText(), (String)cbKategori.getSelectedItem(), 
                                      (int)spinStok.getValue(), (int)spinHarga.getValue(), tanggal);
        updateTabel();
    }
}


    private JPanel panelGudang() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JButton btnOut = createStyledButton("KURANGI STOK", Color.WHITE, dangerColor);
        btnOut.setPreferredSize(new Dimension(200, 60));
        
        String[] kolomLog = {"Waktu", "ID", "Nama Barang", "Qty", "Ket"};
        modelLog = new DefaultTableModel(kolomLog, 0);
        tabelLogGudang = new JTable(modelLog);
        tabelLogGudang.setRowHeight(40);
        tabelLogGudang.getTableHeader().setPreferredSize(new Dimension(100, 45));
        tabelLogGudang.getTableHeader().setDefaultRenderer(getBlueHeaderRenderer());

        btnOut.addActionListener(e -> tampilkanFormBarangKeluar());
        panel.add(btnOut, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabelLogGudang), BorderLayout.CENTER);
        return panel;
    }

    private void tampilkanFormBarangKeluar() {
    int row = tabelProduk.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Pilih barang di tabel Admin!");
        return;
    }
    int modelRow = tabelProduk.convertRowIndexToModel(row);
    int stokSaatIni = controller.getModel().getStok(modelRow); 

    JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
    JSpinner spinQty = new JSpinner(new SpinnerNumberModel(1, 1, Math.max(1, stokSaatIni), 1));
    JComboBox<String> cbKet = new JComboBox<>(new String[]{"Dijual", "Dipindahkan"});
    JSpinner spinTanggal = new JSpinner(new SpinnerDateModel());
    spinTanggal.setEditor(new JSpinner.DateEditor(spinTanggal, "dd-MM-yyyy"));

    form.add(new JLabel("Barang:")); form.add(new JLabel(controller.getModel().getNamaBarang(modelRow)));
    form.add(new JLabel("Jumlah:")); form.add(spinQty);
    form.add(new JLabel("Ket:")); form.add(cbKet);
    form.add(new JLabel("Tanggal:")); form.add(spinTanggal);

    if (JOptionPane.showConfirmDialog(this, form, "Kurangi Stok", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
        try {
            int qty = (int) spinQty.getValue();
            Date tanggal = (Date) spinTanggal.getValue();
            controller.prosesBarangKeluar(modelRow, qty, tanggal);
            modelLog.insertRow(0, new Object[]{dateFormat.format(tanggal), modelRow, controller.getModel().getNamaBarang(modelRow), qty, cbKet.getSelectedItem()});
            updateTabel();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }
}


    private void setupTableAdmin() {
        String[] kolom = {"ID", "Nama Produk", "Stok", "Harga", "Status", "Kategori", "Terakhir Masuk", "Terakhir Keluar"};
        model = new DefaultTableModel(kolom, 0);
        tabelProduk = new JTable(model);
        tabelProduk.setRowHeight(40);
        tabelProduk.getTableHeader().setPreferredSize(new Dimension(100, 45));
        tabelProduk.getTableHeader().setDefaultRenderer(getBlueHeaderRenderer());
        tabelProduk.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                int modelRow = table.convertRowIndexToModel(row);
                int stokVal = (int) table.getModel().getValueAt(modelRow, 2);
                if (stokVal == 0) c.setForeground(dangerColor);
                else if (stokVal < 5) c.setForeground(warningColor);
                else c.setForeground(new Color(44, 62, 80));
                c.setBackground(isSelected ? new Color(212, 230, 241) : Color.WHITE);
                return c;
            }
        });
    }

    private JButton createStyledButton(String text, Color bgColor, Color textColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor); btn.setForeground(textColor);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(textColor, 1), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        btn.setFocusPainted(false); btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void tampilkanNotifikasi() {
        StringBuilder pesan = new StringBuilder();
        int count = 0;
        for (int i = 0; i < controller.getModel().getJmlBarang(); i++) {
            if (controller.getModel().getStok(i) < 5) {
                pesan.append("⚠ ").append(controller.getModel().getNamaBarang(i))
                     .append(" (Stok: ").append(controller.getModel().getStok(i)).append(")\n");
                count++;
            }
        }
        JOptionPane.showMessageDialog(this, count == 0 ? "Stok Aman ✅" : pesan.toString(), "Notifikasi", JOptionPane.WARNING_MESSAGE);
    }

    private void updateTabel() {
        model.setRowCount(0);
        int warningCount = 0;
        for (int i = 0; i < controller.getModel().getJmlBarang(); i++) {
            int s = controller.getModel().getStok(i);
            if (s < 5) warningCount++;
            String status = (s == 0) ? "HABIS" : (s < 5) ? "WARNING" : "AMAN";
            model.addRow(new Object[]{i, controller.getModel().getNamaBarang(i), s, "Rp " + controller.getModel().getHarga(i), status, 
                controller.getModel().getKategori(i), controller.getModel().getWaktuMasuk(i), controller.getModel().getWaktuKeluar(i)});
        }

        if (lblTotalVal != null) lblTotalVal.setText(String.valueOf(controller.getTotalJenisProduk()));
        if (lblMasukVal != null) lblMasukVal.setText(String.valueOf(controller.getTotalQtyMasuk()));
        if (lblKeluarVal != null) lblKeluarVal.setText(String.valueOf(controller.getTotalQtyKeluar()));

        // --- LOGIKA NOTIFIKASI DI PERTAHANKAN (MENYESUAIKAN ADA IKON ATAU TIDAK) ---
        if (btnNotif != null) {
            btnNotif.setText((btnNotif.getIcon() == null ? "🔔 " : "") + warningCount); 
            btnNotif.setForeground(warningCount > 0 ? Color.RED : Color.DARK_GRAY);
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