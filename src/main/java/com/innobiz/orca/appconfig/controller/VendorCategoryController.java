package com.innobiz.orca.appconfig.controller;

import static org.springframework.http.HttpStatus.OK;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innobiz.orca.appconfig.config.MessageConfiguration;
import com.innobiz.orca.appconfig.dto.UOMCategoryDTO;
import com.innobiz.orca.appconfig.service.UOMCategoryService;

//@CrossOrigin("*")
@RestController // This means that this class is a Controller
@RequestMapping(path = "/vendorCategory") // This means URL's start with /demo (after Application path)
public class VendorCategoryController {

	private final VendorCategoryService vendorCategoryService;

	MessageConfiguration messageConfiguration = new MessageConfiguration();

	@Autowired
	public VendorCategoryController(VendorCategoryService vendorCategoryService) {
		this. vendorCategoryService =  vendorCategoryService;
	}

	@PostMapping(path = "/create") // Map ONLY POST Requests
	public ResponseEntity<VendorCategoryDTO> create(@RequestBody @Valid  VendorCategoryDTO  vendorCategoryDTO) {
		return new ResponseEntity<>( vendorCategoryService.create( vendorCategoryDTO), OK);
	}


	@PutMapping("/update") // update name
	public ResponseEntity<VendorCategoryDTO> update(@RequestBody @Valid VendorCategoryDTO vendorCategoryDTO) {
		 VendorCategoryDTO  vendorCategoryDto =  vendorCategoryService.update(vendorCategoryDTO);
		return new ResponseEntity<>( vendorCategoryDto, OK);

	}

	@GetMapping(path = "/findAll")
	public ResponseEntity<List<VendorCategoryDTO>> findAll() {
		return new ResponseEntity<>( vendorCategoryService.findAll(), OK);

	}

	@GetMapping("/findById/{id}")
	public ResponseEntity<VendorCategoryDTO> findById(@PathVariable(value = "id") Integer id) {
		 VendorCategoryDTO  vendorCategoryDTO =  vendorCategoryService.findById(id);
		return new ResponseEntity<>( vendorCategoryDTO, OK);
	}

	@GetMapping(path = "/findByName/{name}")
	public ResponseEntity<VendorCategoryDTO> findByName(@PathVariable String name) {
		VendorCategoryDTO vendorCategoryDTOList = vendorCategoryService.findByName(name);
		return new ResponseEntity<>(vendorCategoryDTOList, OK);
	}

	@GetMapping(path = "/findByCode/{code}")
	public ResponseEntity< VendorCategoryDTO> findByCode(@PathVariable String code) {
		 VendorCategoryDTO  vendorCategoryDTOList =  vendorCategoryService.findByCode(code);
		return new ResponseEntity<>( vendorCategoryDTOList, OK);

	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Map<String, String>> delete(@PathVariable(value = "id") Integer id) {
		 vendorCategoryService.delete(id);
		Map<String, String> deleteSuccess = new HashMap<>();
		deleteSuccess.put("id", id.toString());
		deleteSuccess.put("message", "Delete Success");
		return new ResponseEntity<>(deleteSuccess, OK);
	}

}