package com.innobiz.orca.appconfig_serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.innobiz.orca.appconfig.config.MessageConfiguration;
import com.innobiz.orca.appconfig.dto.UOMDTO;
import com.innobiz.orca.appconfig.entity.UOM;
import com.innobiz.orca.appconfig.exception.DataNotFoundException;
import com.innobiz.orca.appconfig.exception.DuplicateDataException;
import com.innobiz.orca.appconfig.repository.UOMRepository;
import com.innobiz.orca.appconfig.service.impl.UOMServiceImpl;

@ExtendWith(MockitoExtension.class)
class TaskerviceImplTest {

//public class UOMServiceImplTest {
	@Mock
	UOMRepository uomRepository;

	@Mock
	MessageConfiguration messageConfiguration;

	ModelMapper modelMapper = new ModelMapper();

	@InjectMocks
	UOMServiceImpl uomService;

	Map<String, String> mockedMap = mock(HashMap.class);

	@BeforeEach
	void setUp() {
		// uomService = new UOMServiceImpl(uomRepository,modelMapper,
		// messageConfiguration);
	}

	@Test
	void createUOMSuccessFullyOnValidData() {
		UOMDTO uomDto = new UOMDTO();
		uomDto.setUomName("test1");
		uomDto.setUomCode("123");
		uomDto.setRoundPrescition(0);

		UOM uomReceivedFromDb = new UOM();
		uomReceivedFromDb.setId(1);
		uomReceivedFromDb.setUomName("test1");
		uomReceivedFromDb.setUomCode("123");
		uomReceivedFromDb.setRoundPrescition(0);

		when(uomRepository.save(any(UOM.class))).thenReturn(uomReceivedFromDb);

		UOMDTO uomDtoResponse = uomService.create(uomDto);
		assertEquals(1, uomDtoResponse.getId());
	}

	@Test
	void createUOMShouldFailIfProvidedIdInRequest() {
		UOMDTO uomDto = new UOMDTO();
		uomDto.setId(1);
		uomDto.setUomName("test1");
		uomDto.setUomCode("123");
		uomDto.setRoundPrescition(1);

		when(messageConfiguration.getError()).thenReturn((HashMap<String, String>) mockedMap);
		when(mockedMap.get("UPS-207")).thenReturn("Duplicate value found");

		DuplicateDataException duplicateDataException = Assertions.assertThrows(DuplicateDataException.class,
				() -> uomService.create(uomDto));

		assertEquals("Duplicate value found", duplicateDataException.getMessage());
	}

	@Test
	void createUOMShouldFailIfTaskAlreadyExists() {
		UOMDTO uomDto = new UOMDTO();
		uomDto.setUomName("test1");
		uomDto.setUomCode("123");
		uomDto.setRoundPrescition(1);

		when(messageConfiguration.getError()).thenReturn((HashMap<String, String>) mockedMap);
		when(mockedMap.get("UPS-207")).thenReturn("Duplicate value found");

		when(uomRepository.findByUomNameOrUomCode(anyString(), anyString())).thenReturn(Optional.of(new UOM()));
		DuplicateDataException duplicateDataException = Assertions.assertThrows(DuplicateDataException.class,
				() -> uomService.create(uomDto));

		assertEquals("Duplicate value found", duplicateDataException.getMessage());
	}

	@Test
	void updateUOMSuccessFullyOnValidData() {
		UOMDTO uomDto = new UOMDTO();
		uomDto.setId(1);
		uomDto.setUomName("test-new-name");
		uomDto.setUomCode("123-new-code");
		uomDto.setRoundPrescition(2);

		UOM uomFromDb = new UOM();
		uomFromDb.setId(1);
		uomFromDb.setUomName("test1");
		uomFromDb.setUomCode("123");
		uomDto.setRoundPrescition(2);
		when(uomRepository.findById(1)).thenReturn(Optional.of(uomFromDb));
		UOMDTO updateUOMResponse = uomService.update(uomDto);
		assertEquals(1, updateUOMResponse.getId());
		assertEquals("test-new-name", updateUOMResponse.getUomName());
	}

	@Test
	void updateUOMShouldFailIfTaskNotFound() {
		UOMDTO uomDto = new UOMDTO();
		uomDto.setId(100);
		uomDto.setUomName("test-new-name");
		uomDto.setUomCode("123-new-code");
		uomDto.setRoundPrescition(2);

		when(messageConfiguration.getError()).thenReturn((HashMap<String, String>) mockedMap);
		when(mockedMap.get("UPS-602")).thenReturn("File not found");

		when(uomRepository.findById(100)).thenReturn(Optional.ofNullable(null));

		DataNotFoundException dataNotFoundException = Assertions.assertThrows(DataNotFoundException.class,
				() -> uomService.update(uomDto));

		assertEquals(100, dataNotFoundException.getUomCategoryId());
		assertEquals("File not found", dataNotFoundException.getMessage());
	}

	@Test
	void findAllShouldSendAllTasks() {
		UOM uom1 = new UOM();
		uom1.setId(1);
		uom1.setUomName("test1");
		uom1.setUomCode("123");
		uom1.setRoundPrescition(2);

		UOM uom2 = new UOM();
		uom2.setId(2);
		uom2.setUomName("test2");
		uom2.setUomCode("345");
		uom2.setRoundPrescition(2);
		List<UOM> uom = Arrays.asList(uom1, uom2);

		when(uomRepository.findAll()).thenReturn(uom);

		List<UOMDTO> uomDTOList = uomService.findAll();

		assertEquals(2, uomDTOList.size());
	}

	@Test
	void getUOMByIdShouldReturnUniqueTask() {
		UOM uom = new UOM();
		uom.setId(1);
		uom.setUomName("test1");
		uom.setUomCode("123");
		uom.setRoundPrescition(2);

		UOMDTO expectedUOMDto = new UOMDTO();
		expectedUOMDto.setId(1);
		expectedUOMDto.setUomName("test1");
		expectedUOMDto.setUomCode("123");
		expectedUOMDto.setRoundPrescition(2);

		when(uomRepository.findById(1)).thenReturn(Optional.of(uom));

		UOMDTO actualUOMDto = uomService.findById(1);

		assertEquals(expectedUOMDto, actualUOMDto);
	}

	@Test
	void getUOMByIdShouldThrowExceptionIfTaskNotFound() {

		when(messageConfiguration.getError()).thenReturn((HashMap<String, String>) mockedMap);
		when(mockedMap.get("OI-602")).thenReturn("Data not found");

		when(uomRepository.findById(100)).thenReturn(Optional.ofNullable(null));

		DataNotFoundException dataNotFoundException = Assertions.assertThrows(DataNotFoundException.class,
				() -> uomService.findById(100));

		assertEquals(100, dataNotFoundException.getUomCategoryId());
		assertEquals("Data not found", dataNotFoundException.getMessage());
	}

	@Test
	void getUOMByNameShouldSendTask() {
		UOM uom1 = new UOM();
		uom1.setId(1);
		uom1.setUomName("test1");
		uom1.setUomCode("4325");
		uom1.setRoundPrescition(3);

		when(uomRepository.findByUomName("test1")).thenReturn(Optional.of(uom1));
		UOMDTO uomDTO = uomService.findByName("test1");
		assertNotNull(uomDTO);
		assertEquals("test1", uomDTO.getUomName());
	}

	@Test
	void getUOMByNameShouldThrowErrorIfTaskNotFound() {

		when(messageConfiguration.getError()).thenReturn((HashMap<String, String>) mockedMap);
		when(mockedMap.get("UPS-602")).thenReturn("File not found");

		when(uomRepository.findByUomName("test1")).thenReturn(Optional.empty());
		DataNotFoundException dataNotFoundException = Assertions.assertThrows(DataNotFoundException.class,
				() -> uomService.findByName("test1"));

		assertEquals("File not found", dataNotFoundException.getMessage());
	}

	@Test
	void deleteUOMShouldDeleteTask() {
		UOM uom = new UOM();
		uom.setId(1);
		uom.setUomName("test1");
		uom.setUomCode("123");
		uom.setRoundPrescition(2);

		when(uomRepository.findById(1)).thenReturn(Optional.of(uom));
		doNothing().when(uomRepository).delete(uom);
		uomService.delete(1);

		verify(uomRepository, times(1)).delete(uom);
		verify(uomRepository, times(1)).findById(1);

	}

	@Test
	void deleteUOMShouldFailIfTaskNotFound() {

		when(messageConfiguration.getError()).thenReturn((HashMap<String, String>) mockedMap);
		when(mockedMap.get("UPS-602")).thenReturn("File not found");

		when(uomRepository.findById(100)).thenReturn(Optional.ofNullable(null));
		DataNotFoundException dataNotFoundException = Assertions.assertThrows(DataNotFoundException.class,
				() -> uomService.delete(100));

		assertEquals(100, dataNotFoundException.getUomCategoryId());
		assertEquals("File not found", dataNotFoundException.getMessage());

	}
}
