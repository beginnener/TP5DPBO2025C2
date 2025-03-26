_saya Natasha Adinda Cantika dengan NIM 2312120 mengerjakan TP4 dalam mata kuliah DPBO untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin_

## Desain Program
Struktur program terdiri dari 3 file kelas, kelas Menu merupakan kelas utama yang bertugas untuk mengatur tampilan dan logika untuk GUI. Terdapat beberapa komponen yang digunakan pada kelas ini,
**Komponen utama yang digunakan:**
1. **JTextField**: Input teks untuk NIM dan Nama mahasiswa,
2. **JComboBox**: Dropdown untuk memilih jenis kelamin,
3. **JRadioButton**: Pilihan status mahasiswa (Aktif/Cuti/Lulus),
4. **JScrollPane & JTable**: Menampilkan daftar mahasiswa dalam bentuk tabel yang dapat di scroll,
5. **JButton**: Tombol untuk Add/Update, Delete, dan Cancel,
6. **JPanel**: Panel utama untuk menampung komponen GUI,
7. **DefaultTableModel**: Model data untuk tabel.

Selain itu, terdapat pula kelas Mahasiswa yang merupakan kelas untuk objek mahasiswa.
**Komponen/Atribut dari kelas Mahasiswa:**
1. **NIM**, yang menampung string NIM mahasiswa,
2. **Nama**, menampung string nama mahasiswa,
3. **Jenis kelamin**, menampung string jenis kelamin mahasiswa (laki-laki/perempuan),
4. **Status**, menampung status perkuliahan mahasiswa (Aktif/Cuti/Lulus).

Terakhir, ada kelas Database yang merupakan kelas untuk objek database. Kelas ini juga bertugas untuk menghubungkan program dengan database mySQL.
**Komponen/Atribut dari kelas Mahasiswa:**
1. **connection**, untuk menyimpan hasil koneksi dengan database,
2. **statement**, untuk mengeksekusi query/perintah berdasarkan query di program.

**Method utama dalam program:**
<br>**Kelas menu**
1. Konstruktor Menu(), berfungsi untuk mengatur tampilan GUI.
2. setTable(), untuk mengatur tampilan table mahasiswa, metode ini memanfaatkan metode getter dari kelas mahasiswa.
3. insertData(), untuk menangani penambahan data mahasiswa dengan mengambil value dari JTextField, JComboBox, dan JRadioButton.
4. updateData(), untuk menangani perubahan data, cara kerjanya mirip dengan insert data.
5. deleteData(), untuk menangani penghapusan data mahasiswa, metode ini bekerja dengan menghapus salah satu elemen listmahasiswa berdasarkan selected index.
6. clearForm(), bertugas untuk mengosongkan form setelah melakukan input, update, hapus, atau menekan cancel.
7. selectRadioButton(string Status), metode ini digunakan untuk mengatur tampilan dari radio button ketika salah satu index pada tabel mahasiswa diklik, cara kerjanya adalah dengan mengambil dan mengecek string status mahasiswa, setelah melalui pengecekan if else akan menentukan radio button mana yang akan menyala atau dalam kondisi true.
8. nullChecker(), berfungsi untuk mengecek apakah field input kosong atau tidak.
9. statusListener(), berfungsi untuk menangani input pada radio button.
10. checkNimAvailability(String nim), berfungsi untuk mengecek apakah nim yangg diinput sudah ada atau belum dalam database.

**Kelas Mahasiswa**
1. Contructor,
2. Getter Setter.

**Kelas database**
1. Constructor,
2. selectQuery(), utk mengembalikan result set khusus untuk query SELECT,
3. insertUpdateDeleteQuery(), untuk mengeksekusi query berkaitan dengan insert update delete,
4. checkNimExist(), untuk mengeksekusi query khusus untuk mencari NIM tertentu.
5. getStatement(), getter.

## Alur Program
**Insert**
1. Aplikasi dijalankan, jendela GUI muncul.
2. User melakukan input NIM, Nama, Jenis kelamin, dan status.
3. Program akan mengecek terlebih dahulu apakah ada field yang kosong atau tidak, jika ada program akan menampilkan jendela dengan warning "Field tidak boleh kosong.".
4. Setelah input di cek, program akan mengecek ketersediaan NIM apakah sudah ada di database atau belum dengan checkNimAvailability(), fungsi ini bekerja dengan menjalankan query lalu memanggil objek database untuk menggunakan statement supaya query dieksekusi.
5. Setelah validasi nim selesai, program akan mengeksekusi query untuk insert kedalam database.

<br>**Update**
1. Jendela GUI muncul.
2. User memilih salah satu data dengan cara mengklik salah satu baris dari Jtabel.
3. Data akan muncul di field, user diminta untuk melakukan perubahan.
4. Setelah melakukan perubahan dan menekan update,
5. Program mengecek apakah NIM berubah, jika tidak maka program tidak akan mengecek ketersediaan NIM.
6. Jika NIM berubah maka program akan mengecek ketersediaan NIM.
7. Setelah validasi selesai, query dieksekusi.

<br>**Delete**
1. Jendela GUI muncul.
2. User memiliki salah satu data dengan mengklik salah satu baris Jtable.
3. User mengklik tombol delete,
4. Jendela konfirmasi akan muncul untuk memastikan apakah user benar ingin menghapus data.
5. Jika user menekan YES, data akan dihapus.
6. Jika menekan NO, data tidak akan dihapus.

## ScreenRecord
[![Tonton Video](https://img.youtube.com/vi/PfRNS75jqPA/hqdefault.jpg)](https://www.youtube.com/watch?v=PfRNS75jqPA)
