package com.noteanalyzer.mvc.service;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import com.noteanalyzer.appraisal.exceptions.AddressNotAvailableException;
import com.noteanalyzer.entity.notes.NoteConfiguration;
import com.noteanalyzer.entity.notes.NoteType;
import com.noteanalyzer.entity.notes.Parameters;
import com.noteanalyzer.entity.notes.Property;
import com.noteanalyzer.entity.notes.PropertyType;
import com.noteanalyzer.mvc.model.AddressModel;
import com.noteanalyzer.mvc.model.NoteDetailModel;
import com.noteanalyzer.mvc.model.NoteInputFormModel;
import com.noteanalyzer.mvc.model.NoteDashboardModel;
import com.noteanalyzer.mvc.model.NoteTypeModel;
import com.noteanalyzer.mvc.model.PropertyTypeModel;

import lombok.NonNull;



public interface NoteService {
	public void createNote(@NonNull NoteInputFormModel note) throws ParseException;
	
	public Optional<List<NoteTypeModel>> getNoteType();
	
	public Optional<List<PropertyTypeModel>> getPropertyType();
	
	public Optional<NoteDetailModel> getNoteDetail(@NonNull Long noteId);

	public Optional<List<NoteDashboardModel>> getAllNotes(@NonNull long userId);
	
	public List<NoteConfiguration> getConfigValue(@NonNull List<String> configCode);

	Parameters getParameterValue(String parameterName, String userEmailId);
	public Optional<AddressModel> getZipCodeDetails(String zipCode);

	Optional<AddressModel> getAllLocationDetails();

	Optional<List<Property>> getPropertyByAddress(NoteInputFormModel noteModel);

	List<NoteType> getNoteTypeByCode(String noteTypeCode);

	List<PropertyType> getPropertyTypeByCode(String propertyTypeCode);

	Optional<NoteDetailModel> updateNote(NoteDetailModel noteDetailModel);

	boolean deleteNote(NoteDetailModel noteDetailModel, String userName);
	
	
}
