package id.seringiskering.simawar.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.seringiskering.simawar.entity.FamilyMember;
import id.seringiskering.simawar.repository.FamilyMemberRepository;
import id.seringiskering.simawar.response.warga.ListWargaResponse;
import id.seringiskering.simawar.response.warga.WargaResponse;
import id.seringiskering.simawar.service.WargaService;

@Service
public class WargaServiceImpl implements WargaService {
	
	@Autowired
	private FamilyMemberRepository familyMemberRepository;

	@Override
	public List<ListWargaResponse> findFamilyMember(String username) {
		// TODO Auto-generated method stub
		
		List<FamilyMember> familyMember = familyMemberRepository.findAll();
		
		if (familyMember.size()>0) {
			List<ListWargaResponse> listWarga = new ArrayList<ListWargaResponse>();
			for (FamilyMember member: familyMember) {
				ListWargaResponse item = new ListWargaResponse();
				BeanUtils.copyProperties(member, item);
				listWarga.add(item);
			}
			return listWarga;
		}
		
		return null;
	}

	@Override
	public WargaResponse findFamilyMemberById(String username, Long id) {
		// TODO Auto-generated method stub
		Optional<FamilyMember> member = familyMemberRepository.findById(id);
		
		if (member.isPresent()) {
			FamilyMember warga = member.get();
			WargaResponse response = new WargaResponse();
			BeanUtils.copyProperties(warga, response);
			
			return response;
		}
		
		return null;
	}

}
