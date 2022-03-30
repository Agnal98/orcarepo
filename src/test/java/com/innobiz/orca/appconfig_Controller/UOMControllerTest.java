package com.innobiz.orca.appconfig_Controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import com.innobiz.orca.appconfig.controller.UOMController;
import com.innobiz.orca.appconfig.dto.UOMDTO;
import com.innobiz.orca.appconfig.repository.UOMCategoryRepository;
import com.innobiz.orca.appconfig.repository.UOMRepository;
import com.innobiz.orca.appconfig.service.UOMService;

@ExtendWith(MockitoExtension.class)
class UOMControllerTest {

	UOMController uomController;

	@Mock
	private UOMService uomService;
	@Mock
	private UOMCategoryRepository uomCategoryRepository;
	@Mock
	private UOMRepository uomRepository;

	@BeforeEach
	void setUp() {
		uomController = new UOMController(uomService);
	}

	@Test
	void createUOMTest() {

		UOMDTO uomDto = new UOMDTO();
		uomDto.setUomCode("456");
		uomDto.setUomName("test2");
		uomDto.setRoundPrescition(1);

		ResponseEntity<UOMDTO> uomDTO1 = uomController.create(uomDto);
		assertNotNull(uomDTO1.getBody().getId());
	}

	@Test
	void testfindAllUOM() {
		ResponseEntity<List<UOMDTO>> uomDtoList = uomController.findAll();
		assertTrue(uomDtoList.getBody().size() > 0);

	}

	@Test
	void findByIdTest() {
		ResponseEntity<UOMDTO> uomDto = uomController.findById(1);
		assertEquals(1, uomDto.getBody().getId());

	}

	@Test
	void findByNameTest() {

		ResponseEntity<UOMDTO> uomDto1 = uomController.findByName("test2");

		assertNotNull("test2", uomDto1.getBody().getUomName());

	}

	@Test
	void findByCodeTest() {

		ResponseEntity<UOMDTO> uomDto2 = uomController.findByCode("456");

		assertEquals("456", uomDto2.getBody().getUomCode());
	}

	@Test
	void updateTest() {
		UOMDTO uomDto = new UOMDTO();
		// uomDto.setId(1);
		uomDto.setUomCode("457");
		uomDto.setUomName("test3");
		uomDto.setRoundPrescition(1);

		ResponseEntity<UOMDTO> uomDto1 = uomController.update(uomDto);
		assertEquals(1, uomDto1.getBody().getId());
	}

	@Test
	void deleteUOMTest() {

		ResponseEntity<Map<String, String>> message = uomController.delete(1);
		assertEquals(200, message.getStatusCodeValue());

	}

}
