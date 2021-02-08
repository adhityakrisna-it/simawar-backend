package id.seringiskering.simawar.response.warga;

import id.seringiskering.simawar.entity.Persil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListKeluargaResponse {
	private Long id;
	private String blok;
	private String cluster;
	private String familyName;
	private String note;
	private Persil persil;
	private String rt;
	private String rw;
	private String kepemilikanStatus;
}
