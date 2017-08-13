package com.noteanalyzer.mvc.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.noteanalyzer.appraisal.exceptions.AddressNotAvailableException;
import com.noteanalyzer.mvc.model.AddressModel;
import com.noteanalyzer.mvc.model.NoteDetailModel;
import com.noteanalyzer.mvc.model.NoteInputFormModel;
import com.noteanalyzer.mvc.model.NoteDashboardModel;
import com.noteanalyzer.mvc.model.NoteTypeModel;
import com.noteanalyzer.mvc.model.PropertyTypeModel;
import com.noteanalyzer.mvc.model.UserModel;
import com.noteanalyzer.mvc.service.NoteService;
import com.noteanalyzer.mvc.service.UserService;
import com.noteanalyzer.utility.NoteUtility;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

@RestController
public class NoteRestController {

	@Autowired
	NoteService noteService;

	@Autowired
	UserService userService;

	/**
	 * @return the userService
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * @param userService
	 *            the userService to set
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * @return the noteService
	 */
	public NoteService getNoteService() {
		return noteService;
	}

	/**
	 * @param noteService
	 *            the noteService to set
	 */
	public void setNoteService(NoteService noteService) {
		this.noteService = noteService;
	}

	@RequestMapping(value = "/analyzeNote", method = RequestMethod.POST)
	public ResponseEntity<NoteInputFormModel> analyzeNote(@RequestBody NoteInputFormModel noteInputFormModel) {
		String zipCode = noteInputFormModel.getAddress().getZipCode();
		if (StringUtils.isEmpty(zipCode)) {
			return new ResponseEntity<NoteInputFormModel>(HttpStatus.NOT_FOUND);
		}
		System.out.println("Fetching Zip Code details with zipCode " + zipCode);
		Optional<AddressModel> address = noteService.getZipCodeDetails(zipCode);
		if (!address.isPresent()) {
			return new ResponseEntity<NoteInputFormModel>(HttpStatus.NOT_FOUND);
		}
		AddressModel addressModel = address.get();
		noteInputFormModel.setAddress(addressModel);
		noteInputFormModel.setZipCode(zipCode);
		for (String defaultCity : addressModel.getCityList()) {
			noteInputFormModel.setSelCity(defaultCity);
		}
		for (String defaultState : addressModel.getStateList()) {
			noteInputFormModel.setSelCity(defaultState);
		}

		Optional<List<NoteTypeModel>> noteTypeModelList = noteService.getNoteType();
		if (noteTypeModelList.isPresent()) {
			List<NoteTypeModel> noteTypeList = noteTypeModelList.get();
			noteInputFormModel.setNoteTypeList(noteTypeList);
			// noteInputFormModel.setSelNoteType(noteTypeList.get(0));
			// noteInputFormModel.setOriginalTerm(BigDecimal.valueOf(Double.valueOf(noteTypeList.get(0).getTermMonths())));
		}

		Optional<List<PropertyTypeModel>> propTypeModelList = noteService.getPropertyType();
		if (propTypeModelList.isPresent()) {
			noteInputFormModel.setPropTypeList(propTypeModelList.get());
			noteInputFormModel.setSelPropType(propTypeModelList.get().get(0));
		}

		return new ResponseEntity<NoteInputFormModel>(noteInputFormModel, HttpStatus.OK);

	}

	@RequestMapping(value = "/api/createNote", method = RequestMethod.POST)
	public ResponseEntity<String> createNote(@RequestBody NoteInputFormModel noteInputFormModel) {
		String userName = NoteUtility.getLoggedInUserName();
		Optional<UserModel> loggedInuser = userService.getByUsername(userName);
		if (loggedInuser.isPresent()) {
			noteInputFormModel.setUserId(loggedInuser.get().getUserId());
		} else {
			System.out.println("Error with loggedin user name and ID");
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

		System.out.println("Inside POST with ALL value " + noteInputFormModel);
		try {
			noteService.createNote(noteInputFormModel);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		} catch (AddressNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/templateDownload", method = RequestMethod.GET)
	public void downloadFile(HttpServletResponse response) throws IOException {

		File file = null;

		// if(type.equalsIgnoreCase("internal")){
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		file = new File(classloader.getResource("noteTemplate.csv").getFile());
		// }else{
		// file = new File(EXTERNAL_FILE_PATH);
		// }

		/*
		 * if(!file.exists()){ String errorMessage =
		 * "Sorry. The file you are looking for does not exist";
		 * System.out.println(errorMessage); OutputStream outputStream =
		 * response.getOutputStream();
		 * outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
		 * outputStream.close(); return; }
		 */
		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		if (mimeType == null) {
			System.out.println("mimetype is not detectable, will take default");
			mimeType = "application/octet-stream";
		}

		System.out.println("mimetype : " + mimeType);

		response.setContentType(mimeType);

		/*
		 * "Content-Disposition : inline" will show viewable types [like
		 * images/text/pdf/anything viewable by browser] right on browser while
		 * others(zip e.g) will be directly downloaded [may provide save as
		 * popup, based on your browser setting.]
		 */
		// response.setHeader("Content-Disposition", String.format("inline;
		// filename=\"" + file.getName() +"\""));

		/*
		 * "Content-Disposition : attachment" will be directly download, may
		 * provide save as popup, based on your browser setting
		 */
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));

		response.setContentLength((int) file.length());

		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

		// Copy bytes from source to destination(outputstream in this example),
		// closes both streams.
		FileCopyUtils.copy(inputStream, response.getOutputStream());
	}

	@RequestMapping(value = "/api/noteUpload", method = RequestMethod.POST)
	public ResponseEntity<List<NoteInputFormModel>> multiFileUpload(MultipartHttpServletRequest request,
			RedirectAttributes redirectAttributes) throws IOException {

		Iterator<String> iterator = request.getFileNames();
		MultipartFile multipart = null;
		List<NoteInputFormModel> responseList = new ArrayList<>();
		while (iterator.hasNext()) {
			multipart = request.getFile(iterator.next());
			// do something with the file.....
		}
		System.out.println("Index controler for filr upload called>>" + multipart);
		if (multipart == null) {
			System.out.println("Empty uploaded file");
			return new ResponseEntity<List<NoteInputFormModel>>(responseList, HttpStatus.BAD_REQUEST);
		} else {
			System.out.println("Fetching files" + multipart);

			ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
			strat.setType(NoteInputFormModel.class);
			String[] columns = new String[] { "typeOfNote", "address", "property", "dateOfNote", "upb", "rate",
					"pdiPayment", "tdiPayment", "remainingTerm" }; // the fields
																	// to bind
																	// do in
																	// your
																	// JavaBean
			strat.setColumnMapping(columns);
			File file = NoteUtility.convert(multipart);
			CsvToBean csv = new CsvToBean();
			FileReader fr = new FileReader(file);
			CSVReader csvReader = new CSVReader(new FileReader(file));

			List list = csv.parse(strat, csvReader);
			for (Object object : list) {
				NoteInputFormModel note = (NoteInputFormModel) object;
				System.out.println(note);
				responseList.add(note);
			}
			return new ResponseEntity<List<NoteInputFormModel>>(responseList, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/api/fetchAllNotes", method = RequestMethod.GET)
	public ResponseEntity<List<NoteDashboardModel>> listAllNotes() {
		String loggedInUserName = NoteUtility.getLoggedInUserName();
		Optional<UserModel> loggedInuser = userService.getByUsername(loggedInUserName);
		if (loggedInuser.isPresent()) {
			System.out.println("Inside Arvind listAllNotes loggedInUserName" + loggedInUserName);
			long userId = loggedInuser.get().getUserId();
			Optional<List<NoteDashboardModel>> notesList = noteService.getAllNotes(userId);
			if (!notesList.isPresent()) {
				return new ResponseEntity<List<NoteDashboardModel>>(HttpStatus.NO_CONTENT);// You
			}
			return new ResponseEntity<List<NoteDashboardModel>>(notesList.get(), HttpStatus.OK);
		} else {
			System.out.println("Error with loggedin user name and ID " + loggedInUserName);
			return new ResponseEntity<List<NoteDashboardModel>>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/api/updateNote", method = RequestMethod.POST)
	public ResponseEntity<NoteDetailModel> updateNote(@RequestBody NoteDetailModel noteDetailModel) {

		System.out.println("Inside POST with editNote noteDetailModel value " + noteDetailModel);
		if (noteDetailModel == null) {
			return new ResponseEntity<NoteDetailModel>(HttpStatus.BAD_REQUEST);
		}
		Optional<NoteDetailModel> model = noteService.updateNote(noteDetailModel);
		return new ResponseEntity<NoteDetailModel>(model.get(), HttpStatus.OK);
	}

	@RequestMapping(value = "/api/deleteNote", method = RequestMethod.POST)
	public ResponseEntity<String> deleteNote(@RequestBody NoteDetailModel noteDetailModel) {
		if (noteDetailModel == null) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		System.out.println("Inside DELETE with deleteNote noteDetailModel value " + noteDetailModel);
		boolean isDeleted = noteService.deleteNote(noteDetailModel, NoteUtility.getLoggedInUserName());
		if (isDeleted) {
			return new ResponseEntity<String>(HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/api/getNoteDetail/{noteId}", method = RequestMethod.GET)
	public ResponseEntity<NoteDetailModel> getNoteDetail(@PathVariable String noteId) {
		if (StringUtils.isEmpty(noteId)) {
			return new ResponseEntity<NoteDetailModel>(HttpStatus.BAD_REQUEST);
		}
		Optional<NoteDetailModel> noteDetailModel = noteService.getNoteDetail(Long.valueOf(noteId));
		if (noteDetailModel.isPresent()) {
			System.out.println("Inside Get Note Details with  noteDetailModel value " + noteId);
			return new ResponseEntity<NoteDetailModel>(noteDetailModel.get(), HttpStatus.OK);
		}
		return new ResponseEntity<NoteDetailModel>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/api/getNoteType", method = RequestMethod.GET)
	public ResponseEntity<List<NoteTypeModel>> getNoteType() {
		Optional<List<NoteTypeModel>> noteTypeModelList = noteService.getNoteType();
		if (noteTypeModelList.isPresent()) {
			return new ResponseEntity<List<NoteTypeModel>>(noteTypeModelList.get(), HttpStatus.OK);
		}
		return new ResponseEntity<List<NoteTypeModel>>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/api/getPropertyType", method = RequestMethod.GET)
	public ResponseEntity<List<PropertyTypeModel>> getPropertyType() {

		Optional<List<PropertyTypeModel>> propTypeModelList = noteService.getPropertyType();
		if (propTypeModelList.isPresent()) {
			return new ResponseEntity<List<PropertyTypeModel>>(propTypeModelList.get(), HttpStatus.OK);
		}
		return new ResponseEntity<List<PropertyTypeModel>>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/getStateCityList/{zipCode}", method = RequestMethod.GET)
	public ResponseEntity<AddressModel> getStateCityList(@PathVariable String zipCode) {
		Optional<AddressModel> address = noteService.getZipCodeDetails(zipCode);
		if (address.isPresent()) {
			return new ResponseEntity<AddressModel>(address.get(), HttpStatus.OK);
		}
		return new ResponseEntity<AddressModel>(HttpStatus.NOT_FOUND);

	}

}