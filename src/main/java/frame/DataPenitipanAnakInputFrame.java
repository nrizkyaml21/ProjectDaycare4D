package frame;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import helpers.ComboBoxItem;
import helpers.Koneksi;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class DataPenitipanAnakInputFrame extends JFrame {
    private JPanel mainPanel;
    private JTextField idTextField;
    private JTextField namaorangtuaTextField;
    private JButton batalButton;
    private JButton simpanButton;
    private JComboBox namaanakComboBox;
    private JTextField usiaTextField;
    private JRadioButton bayiRadioButton;
    private JRadioButton balitaRadioButton;
    private JRadioButton anakRadioButton;
    private JRadioButton lakiLakiRadioButton;
    private JRadioButton perempuanRadioButton;
    private DatePicker tanggalPenitipanDatePicker;
    private JTextField waktuTextField;
    private JTextField hargaTextField;
    private ButtonGroup statusButtonGroup;
    private ButtonGroup jeniskelaminButtonGroup;

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public void isiKomponen() {
        idTextField.setText(String.valueOf(id));
        String findSQL = "SELECT * FROM data_penitipan WHERE id =?";
        Connection c = Koneksi.getConnection();
        PreparedStatement ps;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                idTextField.setText(String.valueOf(rs.getInt("id")));
                namaorangtuaTextField.setText(rs.getString("nama"));
                int anakId = rs.getInt("anak_id");
                for (int i = 0; i < namaanakComboBox.getItemCount(); i++) {
                    namaanakComboBox.setSelectedIndex(i);
                    ComboBoxItem item = (ComboBoxItem) namaanakComboBox.getSelectedItem();
                    if (anakId == item.getValue()) {
                        break;
                    }
                }
                usiaTextField.setText(rs.getString("usia"));
                String status = rs.getString("status");
                if(status != null) {
                    if(status.equals("Bayi")){
                        bayiRadioButton.setSelected(true);
                    } else if (status.equals("Balita")){
                        balitaRadioButton.setSelected(true);
                    } else if (status.equals("Anak")){
                        anakRadioButton.setSelected(true);
                    }
                }
                String jeniskelamin = rs.getString("jenis_kelamin");
                if (jeniskelamin != null) {
                    if(jeniskelamin.equals("Laki laki")){
                        lakiLakiRadioButton.setSelected(true);
                    }else if (jeniskelamin.equals("Perempuan")){
                        perempuanRadioButton.setSelected(true);
                    }
                }
                tanggalPenitipanDatePicker.setText(rs.getString("tanggal_penitipan"));
                waktuTextField.setText(rs.getString("waktu"));
                hargaTextField.setText(String.valueOf(rs.getInt("harga")));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void kustomisasiKomponen(){
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT * FROM anak ORDER BY nama";
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);
            namaanakComboBox.addItem(new ComboBoxItem(0, "Pilih Nama Anak"));
            while (rs.next()) {
                namaanakComboBox.addItem(new ComboBoxItem(
                        rs.getInt("id"),
                        rs.getString("nama")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        statusButtonGroup = new ButtonGroup();
        statusButtonGroup.add(bayiRadioButton);
        statusButtonGroup.add(balitaRadioButton);
        statusButtonGroup.add(anakRadioButton);

        jeniskelaminButtonGroup = new ButtonGroup();
        jeniskelaminButtonGroup.add(lakiLakiRadioButton);
        jeniskelaminButtonGroup.add(perempuanRadioButton);

        DatePickerSettings dps = new DatePickerSettings();
        dps.setFormatForDatesCommonEra("yyyy-MM-dd");
        tanggalPenitipanDatePicker.setSettings(dps);

        hargaTextField.setHorizontalAlignment(SwingConstants.RIGHT);
        hargaTextField.setText("0");
        hargaTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                hargaTextField.setEditable(
                        (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') ||
                                ke.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
                                ke.getKeyCode() == KeyEvent.VK_LEFT ||
                                ke.getKeyCode() == KeyEvent.VK_RIGHT);
            }
        });
    }

    public DataPenitipanAnakInputFrame() {
        simpanButton.addActionListener(e -> {
            String nama = namaorangtuaTextField.getText();
            if(nama.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi Nama Orangtua",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                namaorangtuaTextField.requestFocus();
                return;
            }
            ComboBoxItem item = (ComboBoxItem) namaanakComboBox.getSelectedItem();
            int anakId = item.getValue();
            if(anakId == 0) {
                JOptionPane.showMessageDialog(null,
                        "Pilih nama anak",
                        "Validasi Combobox",JOptionPane.WARNING_MESSAGE);
                namaanakComboBox.requestFocus();
                return;
            }
            String usia = usiaTextField.getText();
            if(usia.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi Usia Anak",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                usiaTextField.requestFocus();
                return;
            }
            String status = "";
            if(bayiRadioButton.isSelected()){
                status = "Bayi";
            }
            else if (balitaRadioButton.isSelected()){
                status = "Balita";
            }
            else if (anakRadioButton.isSelected()){
                status = "Anak";
            }else{
                JOptionPane.showMessageDialog(null,
                        "Pilih status",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String jeniskelamin = "";
            if (lakiLakiRadioButton.isSelected()){
                jeniskelamin = "Laki laki";
            }
            else if (perempuanRadioButton.isSelected()){
                jeniskelamin = "Perempuan";
            }else{
                JOptionPane.showMessageDialog(null,
                        "Pilih jenis kelamin",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String tanggalPenitipan = tanggalPenitipanDatePicker.getText();
            if (tanggalPenitipan.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi Tanggal penitipan",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                tanggalPenitipanDatePicker.requestFocus();
                return;
            }
            String waktu =  waktuTextField.getText();
            if(waktu.equals(" ")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi Waktu",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                    waktuTextField.requestFocus();
                    return;
                }
            if(hargaTextField.getText().equals("")){
                hargaTextField.setText("0");
            }
            int harga = Integer.parseInt(hargaTextField.getText());
            if(harga == 0 ){
                JOptionPane.showMessageDialog(
                        null,
                        "Isi Harga",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE);
                waktuTextField.requestFocus();
                return;
            }

            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            try {
                if (id == 0) {
                    String cekSQL = "SELECT * FROM data_penitipan WHERE nama = ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()){
                        JOptionPane.showMessageDialog(
                                null,
                                "Nama orangtua sudah ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                    String insertSQL = "INSERT INTO data_penitipan (id, nama, anak_id, usia, status, jenis_kelamin," +
                            "tanggal_penitipan, waktu, harga) " +
                            "VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?)";
                    ps = c.prepareStatement(insertSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, anakId);
                    ps.setString(3, usia);
                    ps.setString(4, status);
                    ps.setString(5, jeniskelamin);
                    ps.setString(6, tanggalPenitipan);
                    ps.setString(7, waktu);
                    ps.setInt(8, harga);
                    ps.executeUpdate();
                    dispose();
                } else {
                    String cekSQL = "SELECT * FROM data_penitipan WHERE nama=? AND id!=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, id);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()){
                        JOptionPane.showMessageDialog(
                                null,
                                "Nama sama sudah ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    String updateSQL = "UPDATE data_penitipan SET nama=?, anak_id = ?, usia = ?, status = ?, " +
                            "jenis_kelamin = ?, tanggal_penitipan = ?, waktu = ?, harga = ? WHERE id=?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, anakId);
                    ps.setString(3, usia);
                    ps.setString(4, status);
                    ps.setString(5, jeniskelamin);
                    ps.setString(6, tanggalPenitipan);
                    ps.setString(7, waktu);
                    ps.setInt(8, harga);
                    ps.setInt(9, id);
                    ps.executeUpdate();
                    dispose();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        batalButton.addActionListener(e -> {
            dispose();
        });
        kustomisasiKomponen();
        init();
    }

    public void init() {
        setContentPane(mainPanel);
        pack();
        setTitle("Input Data Penitipan");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}