import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Menu extends JFrame{
    public static void main(String[] args) {
        Menu window = new Menu();                           // buat object window
        window.setSize(600, 560);               // atur ukuran window
        window.setLocationRelativeTo(null);                 // letakkan window di tengah layar
        window.setContentPane(window.mainPanel);            // isi window
        window.getContentPane().setBackground(Color.white); // ubah warna background
        window.setVisible(true);                            // tampilkan window
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // agar program ikut berhenti saat window diclose
    }

    private int selectedIndex = -1;                             // index baris yang diklik (di set -1 karena ada index 0)
    private ArrayList<Mahasiswa> listMahasiswa;                 // list untuk menampung semua mahasiswa
    private Database database;                                  // atribut database

    private JPanel mainPanel;
    private JTextField nimField;
    private JTextField namaField;
    private JTable mahasiswaTable;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JComboBox jenisKelaminComboBox;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JLabel nimLabel;
    private JLabel namaLabel;
    private JLabel jenisKelaminLabel;
    private JRadioButton aktifRadioButton, cutiRadioButton, lulusRadioButton;
    private String selectedStatus;
    private String nimLama;
    private javax.swing.ButtonGroup statusButton = new javax.swing.ButtonGroup();

    // constructor
    public Menu() {
        listMahasiswa = new ArrayList<>();                                          // inisialisasi listMahasiswa
        database = new Database();                                                  // populatelist() digantikan dengan objek database
        mahasiswaTable.setModel(setTable());                                        // isi GUI tabel mahasiswa
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));    // ubah styling title

        // atur combo box
        String[] jenisKelaminData = {"", "Laki-laki", "Perempuan"};                  // atur isi combo box
        jenisKelaminComboBox.setModel(new DefaultComboBoxModel(jenisKelaminData));

        // deklarasikan radio button kedalam grup
        statusButton.add(aktifRadioButton);
        statusButton.add(cutiRadioButton);
        statusButton.add(lulusRadioButton);

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedIndex == -1){                                            // jika tidak ada yang dipilih
                    insertData();                                                       // maka lakukan insert data
                } else{                                                             // jika selainnya
                    updateData();                                                       // maka lakukan update data
                }
            }
        });

        // saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedIndex >= 0){                                             // jika index yang dipilih >= 0 maka lakukan delete data
                    int result = JOptionPane.showConfirmDialog(                     // gunakan JoptionPane.showConfirmDialog untuk opsi yes/no
                            null,
                            "Yakin untuk menghapus data?",
                            "Konfirmasi Hapus",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (result == JOptionPane.YES_OPTION) {                         // jika result == JOptionPane.Yes_Option maka lakukan delete
                        deleteData();
                    }
                }
            }
        });

        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // buat listener untuk radio button
        ActionListener radioListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JRadioButton selectedButton = (JRadioButton) e.getSource();
                selectedStatus = selectedButton.getText();
            }
        };

        // terapkan radiolistener
        aktifRadioButton.addActionListener(radioListener);
        cutiRadioButton.addActionListener(radioListener);
        lulusRadioButton.addActionListener(radioListener);

        // saat salah satu baris tabel ditekan
        mahasiswaTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = mahasiswaTable.getSelectedRow();

                // simpan value textfield dan combo box
                String selectedNim = mahasiswaTable.getModel().getValueAt(selectedIndex, 1).toString();
                String selectedNama = mahasiswaTable.getModel().getValueAt(selectedIndex, 2).toString();
                String selectedJenisKelamin = mahasiswaTable.getModel().getValueAt(selectedIndex, 3).toString();
                String selectedStatus = mahasiswaTable.getModel().getValueAt(selectedIndex, 4).toString();

                // store nim sebelumnya ke nim lama (utk handle kalau ada yang mau ubah nimnya, misal nimnya salah)
                nimLama = selectedNim;

                // ubah isi textfield dan combo box
                nimField.setText(selectedNim);
//                nimField.setEditable(false);                                            // jika masuk ke mode ketika baris pada tabel dipilih nim tidak bisa diubah
                namaField.setText(selectedNama);
                jenisKelaminComboBox.setSelectedItem(selectedJenisKelamin);
                selectRadioButton(selectedStatus);

                // ubah button "Add" menjadi "Update"
                addUpdateButton.setText("Update");
                // tampilkan button delete
                deleteButton.setVisible(true);
            }
        });
    }

    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] column = {"No", "NIM", "Nama", "Jenis Kelamin", "Status"};

        // buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel temp = new DefaultTableModel(null, column);

        try{
            ResultSet resultSet = database.selectQuery("SELECT * FROM mahasiswa");
            if(!resultSet.next()){
                System.out.println("table kosong");
                return null;
            }

            // isi tabel dengan resultset
            int i = 0;
            while(resultSet.next()){
                Object[] row = new Object[5];
                row[0] = i + 1;
                row[1] = resultSet.getString("nim");
                row[2] = resultSet.getString("nama");
                row[3] = resultSet.getString("jenis_kelamin");
                row[4] = resultSet.getString("status");

                temp.addRow(row);
                i++;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return temp; // return juga harus diganti
    }

    public void insertData() {
        // ambil value dari textfield dan combobox
        String nim = nimField.getText();
        String nama = namaField.getText();
        String jeniskelamin = jenisKelaminComboBox.getSelectedItem().toString();

        // utk menangani input radio button
        String status = statusListener();

        // Pastikan input tidak kosong
        nullChecker(nim, nama, jeniskelamin, status);

        if(!checkNimAvailability(nim)){
            // tambahkan data kedalam database
            String sql = "INSERT INTO mahasiswa VAlUES (null, '" + nim + "', '" + nama + "', '" + jeniskelamin + "', '" + status + "');";
            database.insertUpdateDeleteQuery(sql);

            // feedback
            System.out.println("Insert berhasil!");
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");

            // update tabel
            mahasiswaTable.setModel(setTable());

            // bersihkan form
            clearForm();
        }
        else{
            System.out.print("insert gagal cek kembali input data.");
            JOptionPane.showMessageDialog(null, "NIM sudah tersedia, mohon masukan kombinasi NIM lainnya.");
        }

    }

    public void updateData() {
        // ambil data dari form
        String nim = nimField.getText();
        String nama = namaField.getText();
        String jeniskelamin = jenisKelaminComboBox.getSelectedItem().toString();

        // utk menangani input radio button
        String status = statusListener();

        // Pastikan input tidak kosong
        nullChecker(nim, nama, jeniskelamin, status);

        // ubah data mahasiswa di database
        if(nim.equals(nimLama) || !checkNimAvailability(nim)){                                          // cek apabila nim baru sudah ada atau belum di database
            String sql = "UPDATE mahasiswa SET nim = '" + nim + "', " +
                    "                          nama = '" + nama + "', " +
                    "                          jenis_kelamin = '" + jeniskelamin + "', " +
                    "                          status = '" + status +"' " +
                    "                          WHERE nim = '" + nimLama + "';";
            database.insertUpdateDeleteQuery(sql);
            // update tabel
            mahasiswaTable.setModel(setTable());
            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Update berhasil!");
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
        }
        else{
            System.out.print("insert gagal cek kembali input data.");
            JOptionPane.showMessageDialog(null, "NIM sudah tersedia, mohon masukan kombinasi NIM lainnya.");
        }

    }

    public void deleteData() {
        // hapus data dari list
        String nim = nimField.getText();
        String sql = "DELETE FROM mahasiswa WHERE nim = '" + nim + "';";
        database.insertUpdateDeleteQuery(sql);

        // update tabel
        mahasiswaTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Delete berhasil!");
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
    }

    public void clearForm() {
        // kosongkan semua texfield dan combo box
        nimField.setText("");
        namaField.setText("");
        jenisKelaminComboBox.setSelectedItem("");

        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");
        // sembunyikan button delete
        deleteButton.setVisible(false);

        // atur radio button menjadi kosong
        statusButton.clearSelection();

        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;

        System.out.println("form dibersihkan.");
    }

    private void nullChecker(String nim, String nama, String jeniskelamin, String status){
        if (nim.isEmpty()) {
            JOptionPane.showMessageDialog(null, "NIM harus diisi!");
            return;
        }if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nama harus diisi!");
            return;
        }if (jeniskelamin.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pilih jenis kelamin!");
            return;
        }if (status.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pilih status terlebih dahulu!");
            return;
        }
    }

    private void selectRadioButton(String selectedStatus) {
        if (selectedStatus.equals("Aktif")) aktifRadioButton.setSelected(true);
        else if (selectedStatus.equals("Cuti")) cutiRadioButton.setSelected(true);
        else if (selectedStatus.equals("Lulus")) lulusRadioButton.setSelected(true);
    }

    private String statusListener(){
        String status = "";
        if (aktifRadioButton.isSelected()) status = "Aktif";
        else if (cutiRadioButton.isSelected()) status = "Cuti";
        else if (lulusRadioButton.isSelected()) status = "Lulus";
        return status;
    }

    // untuk mengecek apakah nim sudah ada atau belum
    private boolean checkNimAvailability(String nim){
        String sql = "SELECT COUNT(*) FROM mahasiswa where nim = '" + nim + "'";
        try{
            ResultSet rs = database.checkNimExist(sql);
            if (rs.next()) return rs.getInt(1) > 0;                             // jika ada hasil yang lebih dari 0 berarti nim sudah ada dan fungsi akan mereturn 1 atau true
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;                                                                // jika nim belum ada fungsi akan mereturn 0 atau false
    }
}
