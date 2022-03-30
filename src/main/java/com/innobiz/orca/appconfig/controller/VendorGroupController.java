package com.innobiz.orca.appconfig.controller;

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
import com.innobiz.orca.appconfig.dto.VendorGroupDTO;
import com.innobiz.orca.appconfig.service.VendorGroupService;
import static org.springframework.http.HttpStatus.*;


@RestController
@RequestMapping(path = "/vendorGroup")
public class VendorGroupController {
	private final VendorGroupService vendorGroupService;

	MessageConfiguration messageConfiguration = new MessageConfiguration();

	@Autowired
	public VendorGroupController(VendorGroupService vendorGroupService) {
		this.vendorGroupService = vendorGroupService;

	}

	@PostMapping(path = "/create")
	public ResponseEntity<VendorGroupDTO> create(@RequestBody @Valid VendorGroupDTO vendorGroupDTO) {
		return new ResponseEntity<>(vendorGroupService.create(vendorGroupDTO), OK);
	}


	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Map<String, String>> delete(@PathVariable(value = "id") Integer id) {
		vendorGroupService.delete(id);
		Map<String, String> deleteSuccess = new HashMap<>();
		deleteSuccess.put("id", id.toString());
		deleteSuccess.put("message", "Delete Success");
		return new ResponseEntity<>(deleteSuccess, OK);
	}
	@PutMapping(path = "/update")
	public ResponseEntity<VendorGroupDTO> update(@RequestBody @Valid VendorGroupDTO vendorGroupDTO) {
		VendorGroupDTO vendorGroupDto = vendorGroupService.update(vendorGroupDTO);
		return new ResponseEntity<>(vendorGroupDto, OK);
	}

	@GetMapping(path = "/findAll")
	public ResponseEntity<List<VendorGroupDTO>> findAll() {
		return new ResponseEntity<>(vendorGroupService.findAll(), OK);
	}

	@GetMapping(path = "/findById/{id}")
	public ResponseEntity<VendorGroupDTO> findById(@PathVariable(value = "id") Integer id) {
		VendorGroupDTO vendorGroupDTO = vendorGroupService.findById(id);
		return new ResponseEntity<>(vendorGroupDTO, OK);
	}

	@GetMapping("/findByName/{name}")
	public ResponseEntity<VendorGroupDTO> findByName(@PathVariable String name) {
		VendorGroupDTO vendorGroupDTO = vendorGroupService.findByName(name);
		return new ResponseEntity<>(vendorGroupDTO, OK);
	}

	@GetMapping("/findByCode/{code}")
	public ResponseEntity<VendorGroupDTO> findByCode(@PathVariable String code) {
		VendorGroupDTO venGroupDTO = vendorGroupService.findByCode(code);
		return new ResponseEntity<>(venGroupDTO, OK);
	}

}

	

	