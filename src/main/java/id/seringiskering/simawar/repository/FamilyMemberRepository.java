package id.seringiskering.simawar.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import id.seringiskering.simawar.entity.FamilyMember;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long>, JpaSpecificationExecutor<FamilyMember> {

	Optional<List<FamilyMember>> findByFamilyId(Long id);
	
	@Query("SELECT COUNT(u.id) from FamilyMember u")
    int getJumlahWarga();
	
	@Query(value = "SELECT COUNT(u.id) from family_member u where timestampdiff( YEAR, birth_date, now() ) < 5", nativeQuery = true)
    int getJumlahBalitaNative();
	
	@Query("SELECT COUNT(u.id) from FamilyMember u where birthDate between :now AND :start ")
    Optional<Integer> getJumlahBalitaNonNative(Optional<Date> now, Optional<Date> start);

	@Query("SELECT COUNT(u.id) from FamilyMember u where birthDate < :start ")
    Optional<Integer> getJumlahLansia(Optional<Date> start);

	@Query("SELECT COUNT(u.id) from FamilyMember u where birthDate between :start AND :now ")
    Optional<Integer> getJumlahWargaRangeUsia(Optional<Date> start, Optional<Date> now);
	
	Optional<List<FamilyMember>> findByFamilyIdOrderByFamilyStatus(Long id);
	
	Optional<List<FamilyMember>> findByFamilyIdOrderByKedudukanAscBirthDateAsc(Long id);
	
	@Query("SELECT a from FamilyMember a INNER JOIN FamilyMemberUserOwner b "
			+ "ON a.id=b.id.id where b.id.userId=:userId")
	Optional<List<FamilyMember>> findByUserIdOrderByFamilyName(String userId);
	
}
