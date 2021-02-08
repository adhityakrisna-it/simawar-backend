package id.seringiskering.simawar.request.warga;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SaveKeluargaRequest {
	private Long id;
	private String familyName;
	private String note;
	private String persilId;
	private String kepemilikanStatus;
	private List<String> familyMemberId;
}
