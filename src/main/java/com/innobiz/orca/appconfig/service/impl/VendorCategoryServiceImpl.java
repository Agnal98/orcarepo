package com.innobiz.orca.appconfig.service.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orca.appconfig.config.MessageConfiguration;
import com.innobiz.orca.appconfig.dto.ProductGroupDTO;
import com.innobiz.orca.appconfig.dto.VendorCategoryDTO;
import com.innobiz.orca.appconfig.entity.ProductGroup;
import com.innobiz.orca.appconfig.entity.VendorCategory;
import com.innobiz.orca.appconfig.exception.DataNotFoundException;
import com.innobiz.orca.appconfig.exception.DuplicateDataException;
import com.innobiz.orca.appconfig.repository.VendorCategoryRepository;
import com.innobiz.orca.appconfig.service.VendorCategoryService;
import com.mural.task.dto.TaskDTO;
import com.mural.task.entity.Task;


@Service
@Transactional
public class VendorCategoryServiceImpl implements VendorCategoryService {

private final VendorCategoryRepository vendorCategoryRepository;
private final ModelMapper modelmapper;
MessageConfiguration messageConfiguration;

@Autowired
public VendorCategoryServiceImpl(VendorCategoryRepository vendorCategoryRepository, ModelMapper modelMapper,
MessageConfiguration messageConfiguration) {
this.vendorCategoryRepository = vendorCategoryRepository;
this.modelmapper = modelMapper;
this.messageConfiguration = messageConfiguration;
}

public VendorCategoryDTO create(VendorCategoryDTO vendorCategoryDTO) {

if (vendorCategoryDTO.getId() != null || isDuplicateVendorGroup(vendorCategoryDTO)) {
throw new DuplicateDataException(messageConfiguration.getError().get("OI-100"));
}

return convertEntityToDto(vendorCategoryRepository.save(convertDtoToEntity(vendorCategoryDTO)));
}


public void delete(Integer id) {
Optional<VendorCategory> vendorCategoryOptional = vendorCategoryRepository.findById(id);
if (vendorCategoryOptional.isPresent()) {
vendorCategoryOptional.get().setIsDeleted(true);
} else {
throw new DataNotFoundException(id, messageConfiguration.getError().get("OI-101"));
}
}

public VendorCategoryDTO update(VendorCategoryDTO vendorCategoryDTO) {

	if (vendorCategoryDTO.getId() != null) {
		if (!isDuplicateVendorCategoryForUpdate(vendorCategoryDTO)) {
			Optional<VendorCategory> vendorCategoryOptional = vendorCategoryRepository.findById(vendorCategoryDTO.getId());
			if (vendorCategoryOptional.isPresent()) {
				VendorCategory vendorCategory = mergeVendorCategory(vendorCategoryDTO,vendorCategoryOptional.get()); 
//						vendorCategoryOptional.get());
				vendorCategoryRepository.save(vendorCategory);
				return convertEntityToDto(vendorCategory);
			}
			throw new DataNotFoundException(vendorCategoryDTO.getId(), messageConfiguration.getError().get("OI-101"));
		}

		}
		throw new DuplicateDataException(messageConfiguration.getError().get("OI-100"));
	}
	
public List<VendorCategoryDTO>findAll() {
	return vendorCategoryRepository.findAll().stream().map(this::convertEntityToDto).toList();
}


public VendorCategoryDTO findByName(String name) {

Optional<VendorCategory> vendorCategoryOptional = vendorCategoryRepository.findByName(name);
if (vendorCategoryOptional.isPresent()) {
return convertEntityToDto(vendorCategoryOptional.get());
}
throw new DataNotFoundException(name, messageConfiguration.getError().get("OI-101"));
}

public VendorCategoryDTO findByCode(String code) {

Optional<VendorCategory> vendorCategoryOptional = vendorCategoryRepository.findByCode(code);
if (vendorCategoryOptional.isPresent()) {
return convertEntityToDto(vendorCategoryOptional.get());
}
throw new DataNotFoundException(code, messageConfiguration.getError().get("OI-101"));
}

public VendorCategoryDTO findById(Integer id) {

Optional<VendorCategory> vendorCategoryOptional = vendorCategoryRepository.findById(id);
if (vendorCategoryOptional.isPresent()) {
return convertEntityToDto(vendorCategoryOptional.get());
}
throw new DataNotFoundException(id, messageConfiguration.getError().get("OI-101"));
}

private VendorCategory convertDtoToEntity(VendorCategoryDTO vendorCategoryDTO) throws MappingException {
VendorCategory vendorCategory = modelmapper.map(vendorCategoryDTO, VendorCategory.class);
//vendorCategory.setVendorCategoryParent(vendorCategoryRepository.findById(vendorCategoryDTO.getVendorCategoryParent().getId()).get());

return vendorCategory;
}
private VendorCategory mergeVendorCategory(VendorCategoryDTO vendorCategoryDTO, VendorCategory vendorCategory) throws EntityNotFoundException, MappingException {
	modelmapper.map(vendorCategory, vendorCategory);
	return vendorCategory;
}
private VendorCategoryDTO convertEntityToDto(VendorCategory vendorCategory) throws MappingException {
return modelmapper.map(vendorCategory, VendorCategoryDTO.class);
}

private boolean isDuplicateVendorGroup(VendorCategoryDTO vendorCategoryDTO) {
return this.vendorCategoryRepository
.findByNameOrCode(vendorCategoryDTO.getName().trim(), vendorCategoryDTO.getCode().trim()).isPresent();
}

private boolean isDuplicateVendorCategoryForUpdate(VendorCategoryDTO vendorCategoryDTO) {
	Optional<VendorCategory> vendorCategoryOptional = vendorCategoryRepository
			.findByNameOrCode(vendorCategoryDTO.getName().trim(),vendorCategoryDTO.getCode().trim());
	if (vendorCategoryOptional.isPresent()) {
		if (vendorCategoryOptional.get().getId() == vendorCategoryDTO.getId()) {
			return false;
		} else {
			return true;
		}
	}
	return false;
}

}
