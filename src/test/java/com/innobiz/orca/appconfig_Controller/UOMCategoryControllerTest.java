package com.innobiz.orca.appconfig_Controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.innobiz.orca.appconfig.controller.UOMCategoryController;
import com.innobiz.orca.appconfig.dto.UOMCategoryDTO;
import com.innobiz.orca.appconfig.service.UOMCategoryService;

@ExtendWith(MockitoExtension.class)

public class UOMCategoryControllerTest {
	UOMCategoryController uomCategoryController;
	@Mock
	private UOMCategoryService uomCategoryService;

	@BeforeEach
	void setUp() {
		uomCategoryController = new UOMCategoryController(uomCategoryService);
	}

	@Test
	void createUOMCategotyControllerTest() {

		UOMCategoryDTO uomCategoryDto = new UOMCategoryDTO();
		uomCategoryDto.setCode("456");
		uomCategoryDto.setName("test2");

		
		ResponseEntity<UOMCategoryDTO> uomCategory1Dto = uomCategoryController.create(uomCategoryDto);
		assertNotNull( uomCategory1Dto.getBody().getId());
	}
	@Test
	void testfindAll() {

		
		ResponseEntity<List<UOMCategoryDTO>> uomCategoryDto = uomCategoryController.findAll();

		assertTrue(uomCategoryDto.getBody().size()>0);
	}

	

	@Test
	void updateUOMCategoryControllerTest() {
		UOMCategoryDTO uomCategoryDto = new UOMCategoryDTO();
		uomCategoryDto.setId(1);
		uomCategoryDto.setCode("456");
		uomCategoryDto.setName("test2");

		
		ResponseEntity<UOMCategoryDTO> uomCategory1List = uomCategoryController.update(uomCategoryDto);
		assertEquals("456", uomCategory1List.getBody().getCode());

	}
	

	@Test
	void findByIdTest() {
		
		ResponseEntity<UOMCategoryDTO> uomCategoryDto = uomCategoryController.findById(1);

		assertEquals(1, uomCategoryDto.getBody().getId());

	}

	@Test
	void findByNameTest() {
		
		ResponseEntity<UOMCategoryDTO> uomCategoryDTOList = uomCategoryController.findByName("test2");

		//assertEquals("test2", uomCategoryDTOList.getBody().getName());

	}
	 @Test
	    void deleteUOMCategoryTest() {

	        ResponseEntity<Map<String,String>> message=uomCategoryController.delete(1);
	        assertEquals(200,message.getStatusCodeValue());

	    }

}
