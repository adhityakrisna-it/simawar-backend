package id.seringiskering.simawar.response.warga;

import java.util.List;

import id.seringiskering.simawar.entity.Persil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfoKeluargaResponse {
	private Long id;
	private String blok;
	private String cluster;
	private String familyName;
	private String note;
	private Persil persil;
	private String rt;
	private String rw;
	private String kepemilikanStatus;
	private String profileUrl;
	private String kkUrl;
	private String address;
	private String nomor;
	private List<InfoWargaResponse> familyMember;
	private String familyProfileUrl;
	private String greeting;
}
