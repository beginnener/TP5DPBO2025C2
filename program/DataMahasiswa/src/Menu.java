import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private String status;

    // constructor
    public Menu() {
        listMahasiswa = new ArrayList<>();                                          // inisialisasi listMahasiswa
        populateList();                                                             // isi listMahasiswa
        mahasiswaTable.setModel(setTable());                                        // isi GUI tabel mahasiswa
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));    // ubah styling title

        // atur combo box
        String[] jenisKelaminData = {"", "Laki-laki", "Perempuan"};                  // atur isi combo box
        jenisKelaminComboBox.setModel(new DefaultComboBoxModel(jenisKelaminData));

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
                status = selectedButton.getText();
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
                String status = mahasiswaTable.getModel().getValueAt(selectedIndex, 4).toString();

                // ubah isi textfield dan combo box
                nimField.setText(selectedNim);
                namaField.setText(selectedNama);
                jenisKelaminComboBox.setSelectedItem(selectedJenisKelamin);
                selectRadioButton(status);

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

        // isi tabel dengan listMahasiswa
        for(int i = 0; i < listMahasiswa.size(); i++){
            Object[] row = new Object[5];
            row[0] = i + 1;
            row[1] = listMahasiswa.get(i).getNim();
            row[2] = listMahasiswa.get(i).getNama();
            row[3] = listMahasiswa.get(i).getJenisKelamin();
            row[4] = listMahasiswa.get(i).getStatusMahasiswa();

            temp.addRow(row);
        }

        return temp; // return juga harus diganti
    }

    public void insertData() {
        // ambil value dari textfield dan combobox
        String nim = nimField.getText();
        String nama = namaField.getText();
        String jeniskelamin = jenisKelaminComboBox.getSelectedItem().toString();

        if (aktifRadioButton.isSelected()) {
            status = "Aktif";
        } else if (cutiRadioButton.isSelected()) {
            status = "Cuti";
        } else if (lulusRadioButton.isSelected()) {
            status = "Lulus";
        }

        // Pastikan status tidak kosong
        if (status.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pilih status terlebih dahulu!");
            return;
        }

        // tambahkan data ke dalam list
        listMahasiswa.add(new Mahasiswa(nim, nama, jeniskelamin, status));

        // update tabel
        mahasiswaTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Insert berhasil!");
        JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
    }

    public void updateData() {
        // ambil data dari form
        String nim = nimField.getText();
        String nama = namaField.getText();
        String jeniskelamin = jenisKelaminComboBox.getSelectedItem().toString();

        if (aktifRadioButton.isSelected()) {
            status = "Aktif";
        } else if (cutiRadioButton.isSelected()) {
            status = "Cuti";
        } else if (lulusRadioButton.isSelected()) {
            status = "Lulus";
        }

        // Pastikan status tidak kosong
        if (status.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pilih status terlebih dahulu!");
            return;
        }

        // ubah data mahasiswa di list
        listMahasiswa.get(selectedIndex).setNim(nim);
        listMahasiswa.get(selectedIndex).setNama(nama);
        listMahasiswa.get(selectedIndex).setJenisKelamin(jeniskelamin);
        listMahasiswa.get(selectedIndex).setStatusMahasiswa(status);

        // update tabel
        mahasiswaTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Update berhasil!");
        JOptionPane.showMessageDialog(null, "Data berhasil diubah");

    }

    public void deleteData() {
        // hapus data dari list
        listMahasiswa.remove(selectedIndex);

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

        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;
    }

    private void selectRadioButton(String status) {
        if (status.equals("Aktif")) {
            aktifRadioButton.setSelected(true);
        } else if (status.equals("Cuti")) {
            cutiRadioButton.setSelected(true);
        } else if (status.equals("Lulus")) {
            lulusRadioButton.setSelected(true);
        }
    }

    private void populateList() {
        listMahasiswa.add(new Mahasiswa("2203999", "Amelia Zalfa Julianti", "Perempuan", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2202292", "Muhammad Iqbal Fadhilah", "Laki-laki", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2202346", "Muhammad Rifky Afandi", "Laki-laki", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2210239", "Muhammad Hanif Abdillah", "Laki-laki", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2202046", "Nurainun", "Perempuan", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2205101", "Kelvin Julian Putra", "Laki-laki", "Cuti"));
        listMahasiswa.add(new Mahasiswa("2200163", "Rifanny Lysara Annastasya", "Perempuan", "Cuti"));
        listMahasiswa.add(new Mahasiswa("2202869", "Revana Faliha Salma", "Perempuan", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2209489", "Rakha Dhifiargo Hariadi", "Laki-laki", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2203142", "Roshan Syalwan Nurilham", "Laki-laki", "Cuti"));
        listMahasiswa.add(new Mahasiswa("2200311", "Raden Rahman Ismail", "Laki-laki", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2200978", "Ratu Syahirah Khairunnisa", "Perempuan", "Cuti"));
        listMahasiswa.add(new Mahasiswa("2204509", "Muhammad Fahreza Fauzan", "Laki-laki", "Cuti"));
        listMahasiswa.add(new Mahasiswa("2205027", "Muhammad Rizki Revandi", "Laki-laki", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2203484", "Arya Aydin Margono", "Laki-laki", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2200481", "Marvel Ravindra Dioputra", "Laki-laki", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2209889", "Muhammad Fadlul Hafiizh", "Laki-laki", "Cuti"));
        listMahasiswa.add(new Mahasiswa("2206697", "Rifa Sania", "Perempuan", "Cuti"));
        listMahasiswa.add(new Mahasiswa("2207260", "Imam Chalish Rafidhul Haque", "Laki-laki", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2204343", "Meiva Labibah Putri", "Perempuan", "Aktif"));
    }
}
