package id.seringiskering.simawar.controller;

import static id.seringiskering.simawar.constant.FileConstant.DOT;
import static id.seringiskering.simawar.constant.FileConstant.JPG_EXTENSION;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import id.seringiskering.simawar.constant.FileConstant;
import id.seringiskering.simawar.domain.HttpResponse;
import id.seringiskering.simawar.exception.domain.InvalidDataException;
import id.seringiskering.simawar.exception.domain.NotAnImageFileException;
import id.seringiskering.simawar.filter.JwtAuthorizationFilter;
import id.seringiskering.simawar.request.warga.FilterWargaRequest;
import id.seringiskering.simawar.request.warga.SaveKeluargaRequest;
import id.seringiskering.simawar.request.warga.SaveWargaRequest;
import id.seringiskering.simawar.response.user.UserResponse;
import id.seringiskering.simawar.response.warga.ListKeluargaResponse;
import id.seringiskering.simawar.response.warga.ListWargaResponse;
import id.seringiskering.simawar.response.warga.WargaResponse;
import id.seringiskering.simawar.service.WargaService;

@RestController
@RequestMapping(path = { "/warga" })
public class WargaController {
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private JwtAuthorizationFilter jwtAuthorizationFilter;
	
	@Autowired
	private WargaService wargaService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@GetMapping("/findFamilyMemberById/{id}")
	@PreAuthorize("hasAnyAuthority('warga:update')")
	public ResponseEntity<WargaResponse> findFamilyMemberById(@PathVariable("id") String id) {
		String username = jwtAuthorizationFilter.getValidUsername();
		WargaResponse response = wargaService.findFamilyMemberById(username, Long.parseLong(id));
		return new ResponseEntity<WargaResponse>(response, HttpStatus.OK);
	}
	
	@GetMapping("/findSimpleFamilyMemberById/{id}")
	@PreAuthorize("hasAnyAuthority('warga:update')")
	public ResponseEntity<ListWargaResponse>findSimpleFamilyMemberById(@PathVariable("id") String id) {
		String username = jwtAuthorizationFilter.getValidUsername();
		ListWargaResponse response = wargaService.findListFamilyMemberById(username, Long.parseLong(id));
		return new ResponseEntity<ListWargaResponse>(response, HttpStatus.OK);
	}
	
	@GetMapping("/findFamilyById/{id}")
	public ResponseEntity<ListKeluargaResponse> findFamilyById(@PathVariable("id") String id) {
		String username = jwtAuthorizationFilter.getValidUsername();
		ListKeluargaResponse response = wargaService.findFamilyById(Long.parseLong(id));
		return new ResponseEntity<ListKeluargaResponse>(response, HttpStatus.OK);
	}
	
	@GetMapping("/findFamilyMemberByFilter")
	@PreAuthorize("hasAnyAuthority('warga:update')")
	public ResponseEntity<List<ListWargaResponse>> findFamilyMemberByFilter(@RequestBody FilterWargaRequest filter) {
		String username = jwtAuthorizationFilter.getValidUsername();
		List<ListWargaResponse> response = wargaService.findFamilyMemberByFilter(filter);
		return new ResponseEntity<> (response, HttpStatus.OK);
	}

	@GetMapping("/findFamilyMemberByFilterParam")
	@PreAuthorize("hasAnyAuthority('warga:update')")
	public ResponseEntity<List<ListWargaResponse>> findFamilyMemberByFilterParam(@RequestParam Map<String, String> params) {
		String username = jwtAuthorizationFilter.getValidUsername();
		FilterWargaRequest filter = new FilterWargaRequest();
		filter.setIsJenisKelamin(params.get("isJenisKelamin"));
		filter.setSex(params.get("sex"));
		filter.setIsUmur(params.get("isUmur"));
		filter.setUmurAwal(params.get("umurAwal"));
		filter.setUmurAkhir(params.get("umurAkhir"));
		filter.setIsReligion(params.get("isReligion"));
		filter.setReligion(params.get("religion"));
		List<ListWargaResponse> response = wargaService.findFamilyMemberByFilter(filter);
		return new ResponseEntity<> (response, HttpStatus.OK);
	}
	
	@PostMapping("/addKeluarga")
	@PreAuthorize("hasAnyAuthority('warga:update')")
	public ResponseEntity<HttpResponse> addKeluarga(@RequestBody SaveKeluargaRequest request) 
												throws InvalidDataException {
		
		String username  = jwtAuthorizationFilter.getValidUsername();
		wargaService.saveKeluarga("Add", username, request);
		return response(HttpStatus.OK,"Data warga berhasil disimpan");
	}
	
	@PostMapping("/updateKeluarga")
	@PreAuthorize("hasAnyAuthority('warga:update')")
	public ResponseEntity<HttpResponse> updateKeluarga(@RequestBody SaveKeluargaRequest request) 
												throws InvalidDataException {
		
		String username  = jwtAuthorizationFilter.getValidUsername();
		wargaService.saveKeluarga("Edit", username, request);
		return response(HttpStatus.OK,"Data warga berhasil disimpan");
	}	
	
	@DeleteMapping("/deleteWarga/{id}")
	@PreAuthorize("hasAnyAuthority('warga:update')")
	public ResponseEntity<HttpResponse> deleteWarga(@PathVariable(name = "id") String id) {
		String username  = jwtAuthorizationFilter.getValidUsername();
		wargaService.deleteDataWarga(username, Long.parseLong(id));
		return response(HttpStatus.OK,"Data warga berhasil dihapus");
	}
	
	@DeleteMapping("/deleteKeluarga/{id}")
	@PreAuthorize("hasAnyAuthority('warga:update')")
	public ResponseEntity<HttpResponse> deleteKeluarga(@PathVariable(name = "id") String id) {
		String username  = jwtAuthorizationFilter.getValidUsername();
		wargaService.deleteDataKeluarga(username, Long.parseLong(id));
		return response(HttpStatus.OK,"Data keluarga berhasil dihapus");
	}
	
	@PostMapping("/updateWarga")
	@PreAuthorize("hasAnyAuthority('warga:update')")
	public ResponseEntity<HttpResponse> updateWarga(
			@RequestParam("id") String id,
			@RequestParam("addressAsId") String addressAsId,
			@RequestParam("familyStatus") String familyStatus,
			@RequestParam("kependudukanStatus") String kependudukanStatus,
			@RequestParam("name") String name,
			@RequestParam("noKk") String noKk,
			@RequestParam("noKtp") String noKtp,
			@RequestParam("phoneNumber1") String phoneNumber1,
			@RequestParam("phoneNumber2") String phoneNumber2,
			@RequestParam("phoneNumber3") String phoneNumber3,
			@RequestParam("religion") String religion,
			@RequestParam("sex") String sex,
			@RequestParam("work") String work,
			@RequestParam("address") String address,	
			@RequestParam("birthDate") String birthDate,
			@RequestParam("note") String note,
			@RequestParam("bpjsNo") String bpjsNo,
			@RequestParam("kisNo") String kisNo,
			@RequestParam("bloodType") String bloodType,
			@RequestParam("lastEducation") String lastEducation,
			@RequestParam("email") String email,
			@RequestParam(value = "fotoProfile", required = false) MultipartFile fotoProfile,
			@RequestParam(value = "fotoKtp", required = false) MultipartFile fotoKtp,
			@RequestParam(value = "fotoKK", required = false) MultipartFile fotoKK			
			) 
			throws InvalidDataException, IOException, NotAnImageFileException, ParseException {
		String username = jwtAuthorizationFilter.getValidUsername();
		
		SaveWargaRequest request = new SaveWargaRequest();
		request.setId(Long.parseLong(id));
		request.setAddressAsId(addressAsId);
		request.setFamilyStatus(familyStatus);
		request.setKependudukanStatus(kependudukanStatus);
		request.setName(name);
		request.setNoKk(noKk);
		request.setNoKtp(noKtp);
		request.setPhoneNumber1(phoneNumber1);
		request.setPhoneNumber2(phoneNumber2);
		request.setPhoneNumber3(phoneNumber3);
		request.setReligion(religion);
		request.setSex(sex);
		request.setWork(work);
		request.setAddress(address);
		request.setEmail(email);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Date date = formatter.parse(birthDate);
		
		request.setBirthDate(date);
		
		request.setNote(note);
		request.setBpjsNo(bpjsNo);
		request.setKisNo(kisNo);
		request.setBloodType(bloodType);
		request.setLastEducation(lastEducation);		
		
		wargaService.saveDataWarga("Edit", username, request, fotoProfile, fotoKtp, fotoKK);
		return response(HttpStatus.OK,"Data warga berhasil disimpan");
	}
	
	@PostMapping("/addWarga")
	@PreAuthorize("hasAnyAuthority('warga:update')")
	public ResponseEntity<ListWargaResponse> addWarga(
			@RequestParam("id") String id,
			@RequestParam("addressAsId") String addressAsId,
			@RequestParam("familyStatus") String familyStatus,
			@RequestParam("kependudukanStatus") String kependudukanStatus,
			@RequestParam("name") String name,
			@RequestParam("noKk") String noKk,
			@RequestParam("noKtp") String noKtp,
			@RequestParam("phoneNumber1") String phoneNumber1,
			@RequestParam("phoneNumber2") String phoneNumber2,
			@RequestParam("phoneNumber3") String phoneNumber3,
			@RequestParam("religion") String religion,
			@RequestParam("sex") String sex,
			@RequestParam("work") String work,
			@RequestParam("address") String address,	
			@RequestParam("birthDate") String birthDate,
			@RequestParam("note") String note,
			@RequestParam("bpjsNo") String bpjsNo,
			@RequestParam("kisNo") String kisNo,
			@RequestParam("bloodType") String bloodType,
			@RequestParam("lastEducation") String lastEducation,
			@RequestParam("email") String email,
			@RequestParam(value = "fotoProfile", required = false) MultipartFile fotoProfile,
			@RequestParam(value = "fotoKtp", required = false) MultipartFile fotoKtp,
			@RequestParam(value = "fotoKK", required = false) MultipartFile fotoKK			
			) 
			throws InvalidDataException, IOException, NotAnImageFileException, ParseException {
		String username = jwtAuthorizationFilter.getValidUsername();
		
		SaveWargaRequest request = new SaveWargaRequest();
		request.setAddressAsId(addressAsId);
		request.setFamilyStatus(familyStatus);
		request.setKependudukanStatus(kependudukanStatus);
		request.setName(name);
		request.setNoKk(noKk);
		request.setNoKtp(noKtp);
		request.setPhoneNumber1(phoneNumber1);
		request.setPhoneNumber2(phoneNumber2);
		request.setPhoneNumber3(phoneNumber3);
		request.setReligion(religion);
		request.setSex(sex);
		request.setWork(work);
		request.setAddress(address);
		request.setEmail(email);
		
		LOGGER.info("PARSE BIRTHDATE : " + birthDate);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Date date = formatter.parse(birthDate);
		
		request.setBirthDate(date);
		
		request.setNote(note);
		request.setBpjsNo(bpjsNo);
		request.setKisNo(kisNo);
		request.setBloodType(bloodType);
		request.setLastEducation(lastEducation);		
		
		ListWargaResponse response = wargaService.saveWarga("Add", username, request, fotoProfile, fotoKtp, fotoKK);
		return new ResponseEntity<ListWargaResponse>(response, HttpStatus.OK);
	}	

	@GetMapping(path = "/data/profile/{noktp}", produces = MediaType.IMAGE_JPEG_VALUE)
	@PreAuthorize("hasAnyAuthority('warga:update')")
	public byte[] getProfileImage(@PathVariable("noktp") String noktp) throws IOException
	{
		String fileLocation = FileConstant.WARGA_FOLDER  + noktp + FileConstant.FORWARD_SLASH + "fotoProfile" + DOT + JPG_EXTENSION;
		LOGGER.info("Path File : " + fileLocation);
		return Files.readAllBytes(Paths.get(fileLocation));
	}
	
	@GetMapping(path = "/data/profile/{noktp}/{view}", produces = MediaType.IMAGE_JPEG_VALUE)
	@PreAuthorize("hasAnyAuthority('warga:update')")
	public byte[] getProfileImageCustomView(@PathVariable("noktp") String noktp, 
											@PathVariable("view") String view) throws IOException
	{
		String fileLocation = FileConstant.WARGA_FOLDER  + noktp + FileConstant.FORWARD_SLASH + "fotoProfile" + DOT + JPG_EXTENSION;
		LOGGER.info("Path File : " + fileLocation);
		//return Files.readAllBytes(Paths.get(fileLocation));
		return getCustomWargaImage(noktp, view, "fotoProfile"); 
	}
	
	@GetMapping(path = "/data/ktp/{noktp}", produces = MediaType.IMAGE_JPEG_VALUE)
	@PreAuthorize("hasAnyAuthority('warga:update')")
	public byte[] getKtpImage(@PathVariable("noktp") String noktp) throws IOException
	{
		String fileLocation = FileConstant.WARGA_FOLDER  + noktp + FileConstant.FORWARD_SLASH + "fotoKtp" + DOT + JPG_EXTENSION;
		LOGGER.info("Path File : " + fileLocation);
		return Files.readAllBytes(Paths.get(fileLocation));

	}
	
	@GetMapping(path = "/data/kk/{noktp}", produces = MediaType.IMAGE_JPEG_VALUE)
	@PreAuthorize("hasAnyAuthority('warga:update')")
	public byte[] getKkImage(@PathVariable("noktp") String noktp) throws IOException
	{
		String fileLocation = FileConstant.WARGA_FOLDER  + noktp + FileConstant.FORWARD_SLASH + "fotoKK" + DOT + JPG_EXTENSION;
		LOGGER.info("Path File : " + fileLocation);
		return Files.readAllBytes(Paths.get(fileLocation));
	}
	
	
	@GetMapping("/findFamilyMember")
	@PreAuthorize("hasAnyAuthority('warga:update')")
	public ResponseEntity<List<ListWargaResponse>> findFamilyMember() {
		String username = jwtAuthorizationFilter.getValidUsername();
		List<ListWargaResponse> response = wargaService.findFamilyMember(username);
		return new ResponseEntity<> (response, HttpStatus.OK);
	}
	
	@GetMapping("/findFamily")
	@PreAuthorize("hasAnyAuthority('warga:update')")
	public ResponseEntity<List<ListKeluargaResponse>> findFamily() {
		String username = jwtAuthorizationFilter.getValidUsername();
		List<ListKeluargaResponse> response = wargaService.findFamily(username);
		return new ResponseEntity<List<ListKeluargaResponse>>(response, HttpStatus.OK);
	}
	
	@PostMapping("/updateProfileKeluarga")
	@PreAuthorize("hasAnyAuthority('warga:update')")
	public ResponseEntity<ListKeluargaResponse> updateProfileKeluarga(
			@RequestParam("id") String id,
			@RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws NumberFormatException, IOException, NotAnImageFileException {
		String username = jwtAuthorizationFilter.getValidUsername();
		ListKeluargaResponse response = wargaService.saveProfileKeluarga(username, Long.parseLong(id), profileImage);
		return new ResponseEntity<ListKeluargaResponse>(response, HttpStatus.OK);
	}
	
	@PostMapping("/updateKKKeluarga")
	@PreAuthorize("hasAnyAuthority('warga:update')")
	public ResponseEntity<ListKeluargaResponse> updateKKKeluarga(
			@RequestParam("id") String id,
			@RequestParam(value = "kkImage", required = false) MultipartFile kkImage) throws NumberFormatException, IOException, NotAnImageFileException {
		String username = jwtAuthorizationFilter.getValidUsername();
		ListKeluargaResponse response = wargaService.saveKKKeluarga(username, Long.parseLong(id), kkImage);
		return new ResponseEntity<ListKeluargaResponse>(response, HttpStatus.OK);
	}
	
	@PostMapping("/updateRumahKeluarga")
	@PreAuthorize("hasAnyAuthority('warga:update')")
	public ResponseEntity<ListKeluargaResponse> updateRumahKeluarga(
			@RequestParam("id") String id,
			@RequestParam(value = "homeImage", required = false) MultipartFile homeImage) throws NumberFormatException, IOException, NotAnImageFileException {
		String username = jwtAuthorizationFilter.getValidUsername();
		ListKeluargaResponse response = wargaService.saveRumahKeluarga(username, Long.parseLong(id), homeImage);
		return new ResponseEntity<ListKeluargaResponse>(response, HttpStatus.OK);
	}	
	
	@GetMapping(path = "/keluarga/profile/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getProfileKeluargaImage(@PathVariable("id") String id) throws IOException
	{
		String fileLocation = FileConstant.KELUARGA_FOLDER  + id + FileConstant.FORWARD_SLASH + "profilekeluarga" + DOT + JPG_EXTENSION;
		LOGGER.info("Path File : " + fileLocation);
		byte[] filebyte = Files.readAllBytes(Paths.get(fileLocation));
		return filebyte;
	}
	
	@GetMapping(path = "/keluarga/kk/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getKKKeluargaImage(@PathVariable("id") String id) throws IOException
	{
		String fileLocation = FileConstant.KELUARGA_FOLDER  + id + FileConstant.FORWARD_SLASH + "kkkeluarga" + DOT + JPG_EXTENSION;
		LOGGER.info("Path File : " + fileLocation);
		byte[] filebyte = Files.readAllBytes(Paths.get(fileLocation));
		return filebyte;
	}
	
	@GetMapping(path = "/keluarga/home/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getHomeKeluargaImage(@PathVariable("id") String id) throws IOException
	{
		String fileLocation = FileConstant.KELUARGA_FOLDER  + id + FileConstant.FORWARD_SLASH + "rumahkeluarga" + DOT + JPG_EXTENSION;
		LOGGER.info("Path File : " + fileLocation);
		byte[] filebyte = Files.readAllBytes(Paths.get(fileLocation));
		return filebyte;
	}
	

	@GetMapping(path = "/keluarga/profile/{id}/{view}", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getProfileKeluargaImageThumbnail(
													@PathVariable("id") String id, 
													@PathVariable("view") String view) throws IOException
	{
		return getCustomKeluargaImage(id, view, "profilekeluarga");  //byteArray;//Files.readAllBytes(Paths.get(fileLocation));
	}
	
	@GetMapping(path = "/keluarga/kk/{id}/{view}", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getKKKeluargaImageThumbnail(
													@PathVariable("id") String id, 
													@PathVariable("view") String view) throws IOException
	{
		return getCustomKeluargaImage(id, view, "kkkeluarga");  //byteArray;//Files.readAllBytes(Paths.get(fileLocation));
	}
	
	@GetMapping(path = "/keluarga/home/{id}/{view}", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getHomeKeluargaImageThumbnail(
													@PathVariable("id") String id, 
													@PathVariable("view") String view) throws IOException
	{
		return getCustomKeluargaImage(id, view, "rumahkeluarga");  //byteArray;//Files.readAllBytes(Paths.get(fileLocation));
	}
	
	private byte[] getCustomKeluargaImage(String id, String view, String filename) 
			throws IOException {
		String fileLocation = FileConstant.KELUARGA_FOLDER  + id + FileConstant.FORWARD_SLASH + filename + DOT + JPG_EXTENSION;
		LOGGER.info("Path File : " + fileLocation);
		byte[] filebyte = Files.readAllBytes(Paths.get(fileLocation));
		
		int size = 40;
		if (view.equals("preview")) {
			size = 100;
		}
		if (view.equals("dashboard")) {
			size = 200;
		}
		if (view.equals("profilepic")) {
			size = 46;
		}
		if (view.equals("infokeluarga")) {
			size = 195;
		}
		
		InputStream input = new ByteArrayInputStream(filebyte);
		BufferedImage image = ImageIO.read(input);
		int width = image.getWidth();
		int height = image.getHeight();
		if (view.equals("preview") || view.equals("thumbnail") || view.equals("profilepic") ) {
			if (size > 0) {
				if (width * height > size * size) {
					image = Scalr.resize(image, Mode.FIT_EXACT, size, size);
				}
			}
			
		} else if (view.equals("dashboard")) {
			image = Scalr.resize(image, Mode.FIT_EXACT, 500, 326);
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", baos);
		byte[] byteArray = baos.toByteArray();
		
		return byteArray;//Files.readAllBytes(Paths.get(fileLocation));
		
	}
	
	private byte[] getCustomWargaImage(String id, String view, String filename) 
			throws IOException {
		String fileLocation = FileConstant.WARGA_FOLDER  + id + FileConstant.FORWARD_SLASH + filename + DOT + JPG_EXTENSION;
		LOGGER.info("Path File : " + fileLocation);
		byte[] filebyte = Files.readAllBytes(Paths.get(fileLocation));
		
		int size = 40;
		if (view.equals("preview")) {
			size = 100;
		}
		if (view.equals("dashboard")) {
			size = 200;
		}
		if (view.equals("profilepic")) {
			size = 120;
		}

		
		InputStream input = new ByteArrayInputStream(filebyte);
		BufferedImage image = ImageIO.read(input);
		int width = image.getWidth();
		int height = image.getHeight();
		if (view.equals("preview") || view.equals("thumbnail") || view.equals("profilepic") ) {
			if (size > 0) {
				if (width * height > size * size) {
					image = Scalr.resize(image, Mode.FIT_EXACT, size, size);
				}
			}
			
		} else if (view.equals("dashboard")) {
			image = Scalr.resize(image, Mode.FIT_EXACT, 500, 326);
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", baos);
		byte[] byteArray = baos.toByteArray();
		
		return byteArray;//Files.readAllBytes(Paths.get(fileLocation));
		
	}
	
	private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
		// TODO Auto-generated method stub
		return new ResponseEntity<> (new HttpResponse(httpStatus.value(),httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message), httpStatus);
	}
	
}
