package id.seringiskering.simawar.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.seringiskering.simawar.entity.Family;
import id.seringiskering.simawar.entity.FamilyMember;
import id.seringiskering.simawar.entity.MasterInformasi;
import id.seringiskering.simawar.repository.FamilyMemberRepository;
import id.seringiskering.simawar.repository.FamilyRepository;
import id.seringiskering.simawar.repository.MasterInfoRepository;
import id.seringiskering.simawar.repository.PersilRepository;
import id.seringiskering.simawar.response.dashboard.CardResponse;
import id.seringiskering.simawar.response.dashboard.InfoIuranResponse;
import id.seringiskering.simawar.response.warga.InfoKeluargaResponse;
import id.seringiskering.simawar.response.warga.InfoWargaResponse;
import id.seringiskering.simawar.response.warga.ListKeluargaResponse;
import id.seringiskering.simawar.response.warga.ListWargaResponse;
import id.seringiskering.simawar.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private FamilyMemberRepository familyMemberRepository;
	
	@Autowired
	private PersilRepository persilRepository;
	
	@Autowired
	private MasterInfoRepository masterInfoRepository;
	
	@Autowired
	private FamilyRepository familyRepository;
	
	@Override
	public CardResponse findCardResponse(String username) {
		// TODO Auto-generated method stub
		
		CardResponse response = new CardResponse();
		
		int jumlahWarga = familyMemberRepository.getJumlahWarga();
		response.setJumlahWarga(String.valueOf(jumlahWarga));
		
		int jumlahPersil = persilRepository.getJumlahPersil();
		response.setJumlahRumah(String.valueOf(jumlahPersil));
		
		int jumlahBalita = familyMemberRepository.getJumlahBalitaNative();
		response.setJumlahBalita(String.valueOf(jumlahBalita));

		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		cal.add(Calendar.YEAR, -5);
		Date start = cal.getTime();
		
		Optional<Date> optoday = Optional.of(today);
		Optional<Date> opstart = Optional.of(start);
		
		Optional<Integer> cekJumlahBalita = familyMemberRepository.getJumlahBalitaNonNative(opstart, optoday); 
		
		jumlahBalita = cekJumlahBalita.get(); 
		response.setJumlahBalita(String.valueOf(jumlahBalita));
		
		cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 50);
		Date fiftyyearsago = cal.getTime();
		
		Optional<Integer> cekJumlahLansia = familyMemberRepository.getJumlahLansia(Optional.of(fiftyyearsago));
		int jumlahLansia = cekJumlahLansia.get();
		
		cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -15);
		Date usiaMuda = cal.getTime();
		Optional<Date> opusiamuda = Optional.of(usiaMuda);
		cal.add(Calendar.YEAR, -65);
		Date usiaTua = cal.getTime();
		Optional<Date> opusiatua = Optional.of(usiaTua);
		Optional<Integer> cekusiaproduktif = familyMemberRepository.getJumlahWargaRangeUsia(opusiatua, opusiamuda);
		response.setJumlahUsiaProduktif(String.valueOf(cekusiaproduktif.get()));

		String prosentaseBalita = String.valueOf(jumlahBalita * 100 / jumlahWarga);
		response.setProsentaseBalita(prosentaseBalita);
		
		String prosentaseProduktif = String.valueOf(cekusiaproduktif.get() * 100 / jumlahWarga);
		response.setProsentaseProduktif(prosentaseProduktif);

		return response;
	}

	@Override
	public InfoIuranResponse findRekapIuranResponse(String username) {
		// TODO Auto-generated method stub
		InfoIuranResponse info = new InfoIuranResponse();
		
		Optional<MasterInformasi> cekinformasi = masterInfoRepository.findById("saldokaswarga");
		if (cekinformasi.isPresent()) {
			info.setNilaiSaldoKasWarga(cekinformasi.get().getInfo1());
			info.setPosisiSaldoKasWarga(cekinformasi.get().getInfo2());
		}
		
		cekinformasi = masterInfoRepository.findById("pengeluaranbulanlalu");
		if (cekinformasi.isPresent()) {
			info.setNilaiPengeluaranBulanLalu(cekinformasi.get().getInfo1());
			info.setPosisiPengeluaranBulanLalu(cekinformasi.get().getInfo2());
		}
		
		cekinformasi = masterInfoRepository.findById("pemasukankaswarga");
		if (cekinformasi.isPresent()) {
			info.setNilaiPemasukanKasWarga(cekinformasi.get().getInfo1());
			info.setPosisiPemasukanKasWarga(cekinformasi.get().getInfo2());
		}

		cekinformasi = masterInfoRepository.findById("belumbayariuran");
		if (cekinformasi.isPresent()) {
			info.setNilaiBelumBayarIuran(cekinformasi.get().getInfo1());
			info.setPosisiBelumBayarIuran(cekinformasi.get().getInfo2());
		}

		return info;
	}

	@Override
	public List<ListKeluargaResponse> findAllKeluarga(String username) {
		// TODO Auto-generated method stub
		List<Family> listFamily = familyRepository.findAll();
		if (listFamily.size() > 0) {

			List<ListKeluargaResponse> listKeluarga = new ArrayList<ListKeluargaResponse>();
			for (Family family : listFamily) {
				ListKeluargaResponse item = new ListKeluargaResponse();
				BeanUtils.copyProperties(family, item);
				
				if (family.getFamilyMembers().size()>0) {
					List<ListWargaResponse> members = new ArrayList<ListWargaResponse>();
					for (FamilyMember member: family.getFamilyMembers()) {
						ListWargaResponse listwarga = new ListWargaResponse();
						BeanUtils.copyProperties(member, listwarga);
						
						members.add(listwarga);
					}
					item.setFamilyMember(members);
				}
				
				item.setAddress(family.getCluster().toUpperCase() + " " + family.getBlok() + " " + family.getNomor());
				
				listKeluarga.add(item);
			}
			return listKeluarga;

		}

		return null;
	}

	@Override
	public InfoKeluargaResponse findFamilyById(String username, Long id) {
		// TODO Auto-generated method stub
		//		Optional<Family> cekfamily = familyRepository.findByIdorderbyfamily(id);
		Optional<Family> cekfamily = familyRepository.findById(id);
		Family family = cekfamily.get();

		InfoKeluargaResponse item = new InfoKeluargaResponse();
		BeanUtils.copyProperties(family, item);
		
		if (family.getFamilyMembers().size()>0) {
			List<InfoWargaResponse> members = new ArrayList<InfoWargaResponse>();
			Optional<List<FamilyMember>> cekfamilies = familyMemberRepository.findByFamilyIdOrderByBirthDate(id);
			List<FamilyMember> families = cekfamilies.get();
			//for (FamilyMember member: family.getFamilyMembers()) {
			for (FamilyMember member: families) {
				InfoWargaResponse listwarga = new InfoWargaResponse();
				BeanUtils.copyProperties(member, listwarga);
				
				members.add(listwarga);
			}
			item.setFamilyMember(members);
		}
		
		item.setAddress(family.getCluster().toUpperCase() + " " + family.getBlok() + " " + family.getNomor());		

		return item;
	}

}
