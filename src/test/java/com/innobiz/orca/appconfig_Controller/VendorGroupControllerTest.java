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

import com.innobiz.orca.appconfig.controller.VendorGroupController;
import com.innobiz.orca.appconfig.dto.VendorGroupDTO;
import com.innobiz.orca.appconfig.repository.VendorGroupRepository;
import com.innobiz.orca.appconfig.service.VendorGroupService;

@ExtendWith(MockitoExtension.class)
class VendorGroupControllerTest {

	VendorGroupController vendorController;

	@Mock
	private VendorGroupService vendorGroupService;
	@Mock
	private VendorGroupRepository vendorGroupRepository;

	@BeforeEach
	void setUp() {
		vendorController = new VendorGroupController(vendorGroupService);
	}

	@Test
	void createVendorGroupTest() {

		VendorGroupDTO vendorDto = new VendorGroupDTO();
		vendorDto.setCode("498");
		vendorDto.setName("test4");

		ResponseEntity<VendorGroupDTO> vendorDTO1 = vendorController.create(vendorDto);
		assertNotNull(vendorDTO1.getBody().getId());
	}

	@Test
	void testfindAllUOM() {
		ResponseEntity<List<VendorGroupDTO>> vendorDtoList = vendorController.findAll();
		assertTrue(vendorDtoList.getBody().size() > 0);

	}

	@Test
	void findByIdTest() {
		ResponseEntity<VendorGroupDTO> vendorDto = vendorController.findById(1);
		assertEquals(1, vendorDto.getBody().getId());

	}

	@Test
	void findByNameTest() {

		ResponseEntity<VendorGroupDTO> vendorDto1 = vendorController.findByName("test4");

		assertNotNull("test4", vendorDto1.getBody().getName());

	}

	@Test
	void findByCodeTest() {

		ResponseEntity<VendorGroupDTO> customerDto2 = vendorController.findByCode("498");

		assertEquals("498", customerDto2.getBody().getCode());
	}

	@Test
	void updateTest() {
		VendorGroupDTO vendorGroupDTO = new VendorGroupDTO();
		// uomDto.setId(1);
		vendorGroupDTO.setCode("478");
		vendorGroupDTO.setName("test5");

		ResponseEntity<VendorGroupDTO> vendorDto1 = vendorController.update(vendorGroupDTO);
		assertEquals(1, vendorDto1.getBody().getId());
	}

	@Test
	void deleteTest() {

		ResponseEntity<Map<String, String>> message = vendorController.delete(1);
		assertEquals(200, message.getStatusCodeValue());

	}

}
