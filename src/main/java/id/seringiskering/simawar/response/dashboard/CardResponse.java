package id.seringiskering.simawar.response.dashboard;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CardResponse {
	private String jumlahWarga;
	private String jumlahRumah;
	private String jumlahBalita;
	private String jumlahUsiaProduktif;
	private String prosentaseBalita;
	private String prosentaseProduktif;
}
