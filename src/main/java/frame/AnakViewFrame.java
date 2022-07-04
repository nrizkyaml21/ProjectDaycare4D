package frame;

import helpers.Koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class AnakViewFrame extends JFrame{
    private JPanel mainPanel;
    private JPanel cariPanel;
    private JTextField cariTextField;
    private JButton cariButton;
    private JTable viewTable;
    private JPanel buttonPanel;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton tutupButton;

    public AnakViewFrame(){
        tambahButton.addActionListener(e->{
            AnakInputFrame inputFrame = new AnakInputFrame();
            inputFrame.setVisible(true);
        });
        ubahButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih < 0){
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih data",
                        "Validasi Pilih Data",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            TableModel tm = viewTable.getModel();
            String idString = tm.getValueAt(barisTerpilih,0).toString();
            int id = Integer.parseInt(idString);

            AnakInputFrame inputFrame = new AnakInputFrame();
            inputFrame.setId(id);
            inputFrame.isiKomponen();
            inputFrame.setVisible(true);
        });
        tutupButton.addActionListener(e -> {
            dispose();
        });
        batalButton.addActionListener(e -> {
            isiTable();
        });
        hapusButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih < 0){
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih data",
                        "Validasi Pilih Data",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            int pilihan = JOptionPane.showConfirmDialog(
                    null,
                    "Yakin?",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION
            );
            if(pilihan == 0){ //pilihan YES
                TableModel tm = viewTable.getModel();
                String idString = tm.getValueAt(barisTerpilih,0).toString();
                int id = Integer.parseInt(idString);

                String deleteSQL = "DELETE FROM data_anak WHERE id = ?";
                Connection c= Koneksi.getConnection();
                PreparedStatement ps;
                try {
                    ps = c.prepareStatement(deleteSQL);
                    ps.setInt(1,id);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        cariButton.addActionListener(e -> {
            if(cariTextField.getText().equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi kata kunci pencarian",
                        "Validasi kata kunci kosong",
                        JOptionPane.WARNING_MESSAGE);
                cariTextField.requestFocus();
                return;
            }
            String keyword = "%"+cariTextField.getText()+"%";
            String searchSQL = "SELECT * FROM data_anak WHERE nama like ?";
            Connection c = Koneksi.getConnection();
            try {
                PreparedStatement ps = c.prepareStatement(searchSQL);
                ps.setString(1, keyword);
                ResultSet rs = ps.executeQuery();

                String[] header = {"Id", "Nama Anak"};
                DefaultTableModel dtm = new DefaultTableModel(header,0);
                viewTable.setModel(dtm);
                viewTable.getColumnModel().getColumn(0).setWidth(55);
                viewTable.getColumnModel().getColumn(0).setMaxWidth(55);
                viewTable.getColumnModel().getColumn(0).setMinWidth(55);
                viewTable.getColumnModel().getColumn(0).setPreferredWidth(55);
                Object[] row = new Object[2];
                while (rs.next()){
                    row[0] = rs.getInt("id");
                    row[1] = rs.getString("nama");
                    dtm.addRow(row);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                isiTable();
            }
        });
        isiTable();
        init();
    }

    public void init(){
        setContentPane(mainPanel);
        pack();
        setTitle("Data Anak");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void isiTable() {
        String selectSQL = "SELECT * FROM data_anak";
        Connection c = Koneksi.getConnection();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);

            String[] header = {"Id", "Nama Anak"};
            DefaultTableModel dtm = new DefaultTableModel(header,0);
            viewTable.setModel(dtm);
            viewTable.getColumnModel().getColumn(1).setWidth(360);
            viewTable.getColumnModel().getColumn(1).setMaxWidth(360);
            viewTable.getColumnModel().getColumn(1).setMinWidth(360);
            viewTable.getColumnModel().getColumn(1).setPreferredWidth(360);
            Object[] row = new Object[2];
            while (rs.next()){
                row[0] = rs.getInt("id");
                row[1] = rs.getString("nama");
                dtm.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
