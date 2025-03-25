public class Mahasiswa {
    private String nim;
    private String nama;
    private String jenisKelamin;
    private String statusMahasiswa;

    public Mahasiswa(String nim, String nama, String jenisKelamin, String statusMahasiswa) {
        this.nim = nim;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.statusMahasiswa = statusMahasiswa;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public void setStatusMahasiswa(String statusMahasiswa) { this.statusMahasiswa = statusMahasiswa; }

    public String getNim() {
        return this.nim;
    }

    public String getNama() {
        return this.nama;
    }

    public String getJenisKelamin() {
        return this.jenisKelamin;
    }

    public String getStatusMahasiswa() { return this.statusMahasiswa; }
}
