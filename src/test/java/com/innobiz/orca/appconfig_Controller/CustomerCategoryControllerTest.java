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

import com.innobiz.orca.appconfig.controller.CustomerCategoryController;
import com.innobiz.orca.appconfig.dto.CustomerCategoryDTO;
import com.innobiz.orca.appconfig.repository.CustomerCategoryRepository;

import com.innobiz.orca.appconfig.service.CustomerCategoryService;

@ExtendWith(MockitoExtension.class)
class CustomerCategoryControllerTest {

	CustomerCategoryController customerController;

	@Mock
	private CustomerCategoryService customerCategoryService;
	@Mock
	private CustomerCategoryRepository customerCategoryRepository;

	@BeforeEach
	void setUp() {
		customerController = new CustomerCategoryController(customerCategoryService);
	}

	@Test
	void createCustomerCategoryTest() {

		CustomerCategoryDTO customerDto = new CustomerCategoryDTO();
		customerDto.setCode("498");
		customerDto.setName("test4");

		ResponseEntity<CustomerCategoryDTO> customerDTO1 = customerController.create(customerDto);
		assertNotNull(customerDTO1.getBody().getId());
	}

	@Test
	void testfindAllUOM() {
		ResponseEntity<List<CustomerCategoryDTO>> customerDtoList = customerController.findAll();
		assertTrue(customerDtoList.getBody().size() > 0);

	}

	@Test
	void findByIdTest() {
		ResponseEntity<CustomerCategoryDTO> customerDto = customerController.findById(1);
		assertEquals(1, customerDto.getBody().getId());

	}

	@Test
	void findByNameTest() {

		ResponseEntity<CustomerCategoryDTO> customerDto1 = customerController.findByName("test4");

		assertNotNull("test4", customerDto1.getBody().getName());

	}

	@Test
	void findByCodeTest() {

		ResponseEntity<CustomerCategoryDTO> customerDto2 = customerController.findByCode("498");

		assertEquals("498", customerDto2.getBody().getCode());
	}

	@Test
	void updateTest() {
		CustomerCategoryDTO customerCategoryDTO = new CustomerCategoryDTO();
		// uomDto.setId(1);
		customerCategoryDTO.setCode("478");
		customerCategoryDTO.setName("test5");

		ResponseEntity<CustomerCategoryDTO> customerDto1 = customerController.update(customerCategoryDTO);
		assertEquals(1, customerDto1.getBody().getId());
	}

	@Test
	void deleteTest() {

		ResponseEntity<Map<String, String>> message = customerController.delete(1);
		assertEquals(200, message.getStatusCodeValue());

	}

}
