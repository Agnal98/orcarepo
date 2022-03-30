package com.innobiz.orca.appconfig.controller;

import static org.springframework.http.HttpStatus.*;

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
import com.innobiz.orca.appconfig.dto.UOMDTO;
import com.innobiz.orca.appconfig.service.UOMService;

@RestController
@RequestMapping(path = "/uom")
public class UOMController {

	private final UOMService uomService;

	MessageConfiguration messageConfiguration = new MessageConfiguration();

	@Autowired
	public UOMController(UOMService uomService) {
		this.uomService = uomService;
	}

	@PostMapping(path = "/create")
	public ResponseEntity<UOMDTO> create(@RequestBody @Valid UOMDTO uomDTO) {
		return new ResponseEntity<>(uomService.create(uomDTO), OK);
	}

	@PutMapping(path = "/update")
	public ResponseEntity<UOMDTO> update(@RequestBody @Valid UOMDTO uomDTO) {
		UOMDTO uomDto = uomService.update(uomDTO);
		return new ResponseEntity<>(uomDto, OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Map<String, String>> delete(@PathVariable(value = "id") Integer id) {
		uomService.delete(id);
		Map<String, String> deleteSuccess = new HashMap<>();
		deleteSuccess.put("id", id.toString());
		deleteSuccess.put("message", "Delete Success");
		return new ResponseEntity<>(deleteSuccess, OK);
		// ResponseEntity<>(this.messageConfiguration.infoMessage(MessageCode.UPS_205),OK);
	}

	@GetMapping(path = "/findAll")
	public ResponseEntity<List<UOMDTO>> findAll() {
		return new ResponseEntity<>(uomService.findAll(), OK);
	}

	@GetMapping(path = "/findById/{id}")
	public ResponseEntity<UOMDTO> findById(@PathVariable(value = "id") Integer id) {
		UOMDTO uomDto = uomService.findById(id);
		return new ResponseEntity<>(uomDto, OK);
	}

	@GetMapping("/findByName/{Name}")
	public ResponseEntity<UOMDTO> findByName(@PathVariable String uomName) {
		UOMDTO uomDTOList = uomService.findByName(uomName);
		return new ResponseEntity<>(uomDTOList, OK);
	}

	@GetMapping("/findByCode/{Code}")
	public ResponseEntity<UOMDTO> findByCode(@PathVariable String uomCode) {
		UOMDTO uomDTOList = uomService.findByCode(uomCode);
		return new ResponseEntity<>(uomDTOList, OK);
	}

	@GetMapping("/findByCategoryId/{id}")
	public ResponseEntity<UOMCategoryDTO> findByCategoryId(@PathVariable(value = "id") Integer id) {
		// System.out.println(uomCategoryService);
		UOMCategoryDTO uomCategoryDTO = uomService.findByCategoryId(id);
		return new ResponseEntity<>(uomCategoryDTO, OK);

	}

}
