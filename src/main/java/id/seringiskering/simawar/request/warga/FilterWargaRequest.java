package id.seringiskering.simawar.request.warga;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterWargaRequest {
	private String isUmur;
	private String umurAwal;
	private String umurAkhir;
	private String isJenisKelamin;
	private String sex;
	private String isReligion;
	private String religion;
	
	public String toString() {
		return "isUmur = " + isUmur + " \n" +
			   "isJenisKelamin = " + isJenisKelamin + " \n";
	}
}
