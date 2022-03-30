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
import com.innobiz.orca.appconfig.dto.UOMCategoryDTO;
import com.innobiz.orca.appconfig.entity.UOMCategory;
import com.innobiz.orca.appconfig.exception.DataNotFoundException;
import com.innobiz.orca.appconfig.exception.DuplicateDataException;
import com.innobiz.orca.appconfig.repository.UOMCategoryRepository;
import com.innobiz.orca.appconfig.service.impl.UOMCategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {


public class UOMCategoryServiceImplTest {
	 @Mock
	    UOMCategoryRepository uomCategoryRepository;

	    @Mock
	    MessageConfiguration messageConfiguration;

	    ModelMapper modelMapper = new ModelMapper();

	    @InjectMocks
	    UOMCategoryServiceImpl uomCategoryService;

	    Map<String,String> mockedMap=mock(HashMap.class);

	    @BeforeEach
	    void setUp() {
	        uomCategoryService = new UOMCategoryServiceImpl(uomCategoryRepository,modelMapper, messageConfiguration);
	    }
	    @Test
	    void createUOMCategorySuccessFullyOnValidData() {
	        UOMCategoryDTO uomCategoryDto = new UOMCategoryDTO();
	        uomCategoryDto.setName("test1");
	        uomCategoryDto.setCode("123");

	        UOMCategory uomCategoryReceivedFromDb = new UOMCategory();
	        uomCategoryReceivedFromDb.setId(1);
	        uomCategoryReceivedFromDb.setName("test1");
	        uomCategoryReceivedFromDb.setCode("123");

	        when(uomCategoryRepository.save(any(UOMCategory.class))).thenReturn(uomCategoryReceivedFromDb);

	        UOMCategoryDTO uomCategoryDtoResponse = uomCategoryService.create(uomCategoryDto);
	        assertEquals(1,uomCategoryDtoResponse.getId());
	    }
	    @Test
	    void createUOMCategoryShouldFailIfProvidedIdInRequest() {
	        UOMCategoryDTO uomCategoryDto = new UOMCategoryDTO();
	        uomCategoryDto.setId(1);
	        uomCategoryDto.setName("test1");
	        uomCategoryDto.setCode("123");

	        when(messageConfiguration.getError()).thenReturn((HashMap<String, String>) mockedMap);
	        when(mockedMap.get("UPS-207")).thenReturn("Duplicate value found");

	        DuplicateDataException duplicateDataException = Assertions.assertThrows(DuplicateDataException.class, () ->
	                uomCategoryService.create(uomCategoryDto));

	        assertEquals("Duplicate value found",
	                duplicateDataException.getMessage());
	    }
	    @Test
	    void createUOMCategoryShouldFailIfTaskAlreadyExists() {
	        UOMCategoryDTO uomCategoryDto = new UOMCategoryDTO();
	        uomCategoryDto.setName("test1");
	        uomCategoryDto.setCode("123");

	        when(messageConfiguration.getError()).thenReturn((HashMap<String, String>) mockedMap);
	        when(mockedMap.get("UPS-207")).thenReturn("Duplicate value found");

	        when(uomCategoryRepository.findByNameOrCode(anyString(),anyString())).thenReturn(Optional.of(new UOMCategory()));
	        DuplicateDataException duplicateDataException = Assertions.assertThrows(DuplicateDataException.class, () ->
	                uomCategoryService.create(uomCategoryDto));

	        assertEquals("Duplicate value found",
	                duplicateDataException.getMessage());
	    }
	    @Test
	    void updateUOMCategorySuccessFullyOnValidData() {
	        UOMCategoryDTO uomCategoryDto = new UOMCategoryDTO();
	        uomCategoryDto.setId(1);
	        uomCategoryDto.setName("test-new-name");
	        uomCategoryDto.setCode("123-new-code");

	        UOMCategory uomCategoryFromDb = new UOMCategory();
	        uomCategoryFromDb.setId(1);
	        uomCategoryFromDb.setName("test1");
	        uomCategoryFromDb.setCode("123");
	        when(uomCategoryRepository.findById(1)).thenReturn(Optional.of(uomCategoryFromDb));
	        UOMCategoryDTO updateUOMCategoryResponse = uomCategoryService.update(uomCategoryDto);
	        assertEquals(1,updateUOMCategoryResponse.getId());
	        assertEquals("test-new-name",updateUOMCategoryResponse.getName());
	    }
	    @Test
	    void updateUOMCategoryShouldFailIfTaskNotFound() {
	        UOMCategoryDTO uomCategoryDto = new UOMCategoryDTO();
	        uomCategoryDto.setId(100);
	        uomCategoryDto.setName("test-new-name");
	        uomCategoryDto.setCode("123-new-code");

	        when(messageConfiguration.getError()).thenReturn((HashMap<String, String>) mockedMap);
	        when(mockedMap.get("UPS-602")).thenReturn("File not found");

	        when(uomCategoryRepository.findById(100)).thenReturn(Optional.ofNullable(null));

	        DataNotFoundException dataNotFoundException = Assertions.assertThrows(DataNotFoundException.class, () ->
	                uomCategoryService.update(uomCategoryDto));

	        assertEquals(100, dataNotFoundException.getUomCategoryId());
	        assertEquals("File not found",
	                dataNotFoundException.getMessage());
	    }
	    @Test
	    void findAllShouldSendAllTasks() {
	        UOMCategory uomCategory1 = new UOMCategory();
	        uomCategory1.setId(1);
	        uomCategory1.setName("test1");
	        uomCategory1.setCode("123");

	        UOMCategory uomCategory2 = new 	UOMCategory();
	        uomCategory2.setId(2);
	        uomCategory2.setName("test2");
	        uomCategory2.setCode("345");
	        List<UOMCategory> uomCategory = Arrays.asList(uomCategory1,uomCategory2);

	        when(uomCategoryRepository.findAll()).thenReturn(uomCategory);

	        List<UOMCategoryDTO> uomCategoryDTOList = uomCategoryService.findAll();

	        assertEquals(2, uomCategoryDTOList.size());
	    }
	    @Test
	    void getUOMCategoryByIdShouldReturnUniqueTask() {
	        UOMCategory uomCategory = new UOMCategory();
	        uomCategory.setId(1);
	        uomCategory.setName("test1");
	        uomCategory.setCode("123");

	        UOMCategoryDTO expectedUOMCategoryDto = new UOMCategoryDTO();
	        expectedUOMCategoryDto.setId(1);
	        expectedUOMCategoryDto.setName("test1");
	        expectedUOMCategoryDto.setCode("123");

	        when(uomCategoryRepository.findById(1)).thenReturn(Optional.of(uomCategory));

	        UOMCategoryDTO actualUOMCategoryDto = uomCategoryService.findById(1);

	        assertEquals(expectedUOMCategoryDto, actualUOMCategoryDto);
	    }
	    @Test
	    void getUOMCategoryByIdShouldThrowExceptionIfTaskNotFound() {

	        when(messageConfiguration.getError()).thenReturn((HashMap<String, String>) mockedMap);
	        when(mockedMap.get("OI-602")).thenReturn("File not found");

	        when(uomCategoryRepository.findById(100)).thenReturn(Optional.ofNullable(null));


	        DataNotFoundException dataNotFoundException = Assertions.assertThrows(DataNotFoundException.class, () ->
	                uomCategoryService.findById(100));

	        assertEquals(100, dataNotFoundException.getUomCategoryId());
	        assertEquals("File not found",
	                dataNotFoundException.getMessage());
	    }
	    @Test
	    void getUOMCategoryByNameShouldSendTask() {
	        UOMCategory uomCategory1 = new UOMCategory();
	        uomCategory1.setId(1);
	        uomCategory1.setName("test1");
	        uomCategory1.setCode("4325");

	        when(uomCategoryRepository.findByName("test1")).thenReturn(Optional.of(uomCategory1));
	        UOMCategoryDTO uomCategoryDTO = uomCategoryService.findByName("test1");
	        assertNotNull(uomCategoryDTO);
	        assertEquals("test1",uomCategoryDTO.getName());
	    }
	    @Test
	    void getUOMCategoryByNameShouldThrowErrorIfTaskNotFound() {

	        when(messageConfiguration.getError()).thenReturn((HashMap<String, String>) mockedMap);
	        when(mockedMap.get("UPS-602")).thenReturn("File not found");

	        when(uomCategoryRepository.findByName("test1")).thenReturn(Optional.empty());
	        DataNotFoundException dataNotFoundException = Assertions.assertThrows(DataNotFoundException.class, () ->
	                uomCategoryService.findByName("test1"));

	        assertEquals("File not found", dataNotFoundException.getMessage());
	    }
	    @Test
	    void deleteUOMCategoryShouldDeleteTask() {
	        UOMCategory uomCategory = new UOMCategory();
	        uomCategory .setId(1);
	        uomCategory .setName("test1");
	        uomCategory .setCode("123");

	        when(uomCategoryRepository.findById(1)).thenReturn(Optional.of(uomCategory));
	        doNothing().when(uomCategoryRepository).delete(uomCategory);
	        uomCategoryService.delete(1);

	        verify(uomCategoryRepository,times(1)).delete(uomCategory);
	        verify(uomCategoryRepository,times(1)).findById(1);

	    }

	    @Test
	    void deleteUOMCategoryShouldFailIfTaskNotFound() {

	        when(messageConfiguration.getError()).thenReturn((HashMap<String, String>) mockedMap);
	        when(mockedMap.get("UPS-602")).thenReturn("File not found");

	        when(uomCategoryRepository.findById(100)).thenReturn(Optional.ofNullable(null));
	        DataNotFoundException dataNotFoundException = Assertions.assertThrows(DataNotFoundException.class, () ->
	                uomCategoryService.delete(100));

	        assertEquals(100, dataNotFoundException.getUomCategoryId());
	        assertEquals("File not found", dataNotFoundException.getMessage());

	    }
	}
}





