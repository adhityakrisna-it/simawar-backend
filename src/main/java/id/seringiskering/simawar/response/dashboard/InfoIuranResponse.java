package id.seringiskering.simawar.response.dashboard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfoIuranResponse {
	private String nilaiSaldoKasWarga;
	private String posisiSaldoKasWarga;
	private String nilaiPengeluaranBulanLalu;
	private String posisiPengeluaranBulanLalu;
	private String nilaiPemasukanKasWarga;
	private String posisiPemasukanKasWarga;
	private String nilaiBelumBayarIuran;
	private String posisiBelumBayarIuran;
}
