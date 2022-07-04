package frame;

import helpers.JasperDataSourceBuilder;
import helpers.Koneksi;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.text.NumberFormat;
import java.util.Locale;

public class DataPenitipanAnakViewFrame extends JFrame{
    private JasperViewer JasperViewer;
    private JPanel mainPanel;
    private JPanel cariPanel;
    private JScrollPane viewScrollPane;
    private JPanel buttonPanel;
    private JTextField cariTextField;
    private JButton cariButton;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton tutupButton;
    private JTable viewTable;

    public DataPenitipanAnakViewFrame(){
        cetakButton.addActionListener(e -> {
            Connection c = Koneksi.getConnection();
            String selectSQL = "SELECT A.*,B.nama AS nama_anak FROM data_penitipan AS A " +
                    "LEFT JOIN anak AS B ON A.anak_id = B.id";
            Object[][] row;
            try {
                Statement s = c.createStatement(
                        ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = s.executeQuery(selectSQL);
                rs.last();
                int jumlah = rs.getRow();
                row = new Object[jumlah][9];
                int i = 0;
                rs.beforeFirst();
                while (rs.next()){
                    row[i][0] = rs.getInt("id");
                    row[i][1] = rs.getString("nama");
                    row[i][2] = rs.getString("nama_anak");
                    row[i][3] = rs.getString("usia");
                    row[i][4] = rs.getString("status");
                    row[i][5] = rs.getString("jenis_kelamin");
                    row[i][6] = rs.getString("tanggal_penitipan");
                    row[i][7] = rs.getString("waktu");
                    row[i][8] = rs.getInt("harga");
                    i++;
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            try {

                JasperReport jasperReport = JasperCompileManager.compileReport("/Users/user/IdeaProjects/ProjectDaycare4D/src/main/resources/laporan_penitipan.jrxml");
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,null, new JasperDataSourceBuilder(row));
                JasperViewer= new JasperViewer(jasperPrint, false);
                JasperViewer.setVisible(true);
            } catch (JRException ex) {
                throw new RuntimeException(ex);
            }
        });

        tambahButton.addActionListener(e->{
          DataPenitipanAnakInputFrame inputFrame = new DataPenitipanAnakInputFrame();
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

            DataPenitipanAnakInputFrame inputFrame = new DataPenitipanAnakInputFrame();
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

                String deleteSQL = "DELETE FROM data_penitipan WHERE id = ?";
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
            String keyword = cariTextField.getText();
            if(keyword.equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi kata kunci pencarian", "" +
                        "Validasi kata kunci kosong",
                        JOptionPane.WARNING_MESSAGE);
                cariTextField.requestFocus();
                return;
            }
            Connection c = Koneksi.getConnection();
            keyword = "%" + cariTextField.getText() + "%";
            String searchSQL = "SELECT A.*,B.nama AS nama_anak FROM data_penitipan AS A " +
                               "LEFT JOIN anak AS B ON A.anak_id = B.id " +
                                "WHERE B.nama like ? OR A.nama like ?";
            try {
                PreparedStatement ps = c.prepareStatement(searchSQL);
                ps.setString(1,keyword);
                ps.setString(2,keyword);
                ResultSet rs = ps.executeQuery();

                DefaultTableModel dtm = (DefaultTableModel) viewTable.getModel();
                dtm.setRowCount(0);
                Object[] row = new Object[9];
                while (rs.next()){
                    row[0] = rs.getInt("id");
                    row[1] = rs.getString("nama");
                    row[2] = rs.getString("nama_anak");
                    row[3] = rs.getString("usia");
                    row[4] = rs.getString("status");
                    row[5] = rs.getString("jenis_kelamin");
                    row[6] = rs.getString("tanggal_penitipan");
                    row[7] = rs.getString("waktu");
                    row[8] = rs.getInt("harga");
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
        setTitle("Data Penitipan Anak");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void isiTable() {
        String selectSQL = "SELECT A.*,B.nama AS nama_anak FROM data_penitipan AS A " +
                     "LEFT JOIN anak AS B ON A.anak_id = B.id";
        Connection c = Koneksi.getConnection();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);

            String[] header = {"Id", "Nama Orang Tua", "Nama Anak", "Usia", "Status", "Jenis Kelamin", "Tanggal Penitipan", "Waktu", "Harga"};

            DefaultTableModel dtm = new DefaultTableModel(header,0);
            viewTable.setModel(dtm);


            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            TableColumnModel columns = viewTable.getColumnModel();
            columns.getColumn(0).setCellRenderer(centerRenderer);
            columns.getColumn(1).setCellRenderer(centerRenderer);
            columns.getColumn(2).setCellRenderer(centerRenderer);
            columns.getColumn(3).setCellRenderer(centerRenderer);
            columns.getColumn(4).setCellRenderer(centerRenderer);
            columns.getColumn(5).setCellRenderer(centerRenderer);
            columns.getColumn(6).setCellRenderer(centerRenderer);
            columns.getColumn(7).setCellRenderer(centerRenderer);


            viewTable.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
            viewTable.setModel(dtm);
            viewTable.getColumnModel().getColumn(0).setWidth(70);
            viewTable.getColumnModel().getColumn(0).setMaxWidth(70);
            viewTable.getColumnModel().getColumn(0).setMinWidth(70);
            viewTable.getColumnModel().getColumn(0).setPreferredWidth(70);
            viewTable.setModel(dtm);
            viewTable.getColumnModel().getColumn(1).setWidth(150);
            viewTable.getColumnModel().getColumn(1).setMaxWidth(150);
            viewTable.getColumnModel().getColumn(1).setMinWidth(150);
            viewTable.getColumnModel().getColumn(1).setPreferredWidth(150);
            viewTable.setModel(dtm);
            viewTable.getColumnModel().getColumn(2).setWidth(140);
            viewTable.getColumnModel().getColumn(2).setMaxWidth(140);
            viewTable.getColumnModel().getColumn(2).setMinWidth(140);
            viewTable.getColumnModel().getColumn(2).setPreferredWidth(140);
            viewTable.setModel(dtm);
            viewTable.getColumnModel().getColumn(3).setWidth(80);
            viewTable.getColumnModel().getColumn(3).setMaxWidth(80);
            viewTable.getColumnModel().getColumn(3).setMinWidth(80);
            viewTable.getColumnModel().getColumn(3).setPreferredWidth(80);
            viewTable.setModel(dtm);
            viewTable.getColumnModel().getColumn(4).setWidth(80);
            viewTable.getColumnModel().getColumn(4).setMaxWidth(80);
            viewTable.getColumnModel().getColumn(4).setMinWidth(80);
            viewTable.getColumnModel().getColumn(4).setPreferredWidth(80);
            viewTable.setModel(dtm);
            viewTable.getColumnModel().getColumn(5).setWidth(110);
            viewTable.getColumnModel().getColumn(5).setMaxWidth(110);
            viewTable.getColumnModel().getColumn(5).setMinWidth(110);
            viewTable.getColumnModel().getColumn(5).setPreferredWidth(110);
            viewTable.setModel(dtm);
            viewTable.getColumnModel().getColumn(6).setWidth(110);
            viewTable.getColumnModel().getColumn(6).setMaxWidth(110);
            viewTable.getColumnModel().getColumn(6).setMinWidth(110);
            viewTable.getColumnModel().getColumn(6).setPreferredWidth(110);
            viewTable.setModel(dtm);
            viewTable.getColumnModel().getColumn(7).setWidth(80);
            viewTable.getColumnModel().getColumn(7).setMaxWidth(80);
            viewTable.getColumnModel().getColumn(7).setMinWidth(80);
            viewTable.getColumnModel().getColumn(7).setPreferredWidth(80);
            viewTable.setModel(dtm);
            viewTable.getColumnModel().getColumn(8).setWidth(80);
            viewTable.getColumnModel().getColumn(8).setMaxWidth(80);
            viewTable.getColumnModel().getColumn(8).setMinWidth(80);
            viewTable.getColumnModel().getColumn(8).setPreferredWidth(80);
            Object[] row = new Object[9];
            while (rs.next()){

                NumberFormat nf = NumberFormat.getInstance(Locale.US);
                String rowHarga = nf.format(rs.getInt("harga"));
                row[0] = rs.getInt("id");
                row[1] = rs.getString("nama");
                row[2] = rs.getString("nama_anak");
                row[3] = rs.getString("usia");
                row[4] = rs.getString("status");
                row[5] = rs.getString("jenis_kelamin");
                row[6] = rs.getString("tanggal_penitipan");
                row[7] = rs.getString("waktu");
                row[8] = rowHarga;
                dtm.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

