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
import com.innobiz.orca.appconfig.dto.VendorGroupDTO;
import com.innobiz.orca.appconfig.entity.VendorGroup;
import com.innobiz.orca.appconfig.exception.DataNotFoundException;
import com.innobiz.orca.appconfig.exception.DuplicateDataException;
import com.innobiz.orca.appconfig.repository.VendorGroupRepository;
import com.innobiz.orca.appconfig.service.VendorGroupService;

@Service
@Transactional
public class VendorGroupServiceImpl implements VendorGroupService {

	private final VendorGroupRepository vendorGroupRepository;
	private final ModelMapper modelmapper;
	MessageConfiguration messageConfiguration;

	@Autowired
	public VendorGroupServiceImpl(VendorGroupRepository vendorGroupRepository, ModelMapper modelMapper,
			MessageConfiguration messageConfiguration) {
		this.vendorGroupRepository = vendorGroupRepository;
		this.modelmapper = modelMapper;
		this.messageConfiguration = messageConfiguration;
	}

	public VendorGroupDTO create(VendorGroupDTO vendorGroupDTO) {

		if (vendorGroupDTO.getId() != null || isDuplicateVendorGroup(vendorGroupDTO)) {
			throw new DuplicateDataException(messageConfiguration.getError().get("OI-100"));
		}

		return convertEntityToDto(vendorGroupRepository.save(convertDtoToEntity(vendorGroupDTO)));
	}

	

	public void delete(Integer id) {
		Optional<VendorGroup> vendorGroupOptional = vendorGroupRepository.findById(id);
		if (vendorGroupOptional.isPresent()) {
			vendorGroupOptional.get().setIsDeleted(true);
		} else {
			throw new DataNotFoundException(id, messageConfiguration.getError().get("OI-101"));
		}
	}

	public VendorGroupDTO update(VendorGroupDTO vendorGroupDTO) {

		if (vendorGroupDTO.getId() != null) {
			if (!isDuplicateVendorGroupForUpdate(vendorGroupDTO)) {
				Optional<VendorGroup> vendorGroupOptional = vendorGroupRepository.findById(vendorGroupDTO.getId());
				if (vendorGroupOptional.isPresent()) {
					VendorGroup vendorGroup = mergeVendorGroup(vendorGroupDTO,vendorGroupOptional.get());
					vendorGroupRepository.save(vendorGroup);
					return convertEntityToDto(vendorGroup);
				}
				throw new DataNotFoundException(vendorGroupDTO.getId(), messageConfiguration.getError().get("OI-101"));
			}
			}
			throw new DuplicateDataException(messageConfiguration.getError().get("OI-100"));
		}
		
	
	public List<VendorGroupDTO> findAll() {
		return vendorGroupRepository.findAll().stream().map(this::convertEntityToDto).toList();
	}

	public VendorGroupDTO findByName(String name) {

		Optional<VendorGroup> vendorGroupOptional = vendorGroupRepository.findByName(name);
		if (vendorGroupOptional.isPresent()) {
			return convertEntityToDto(vendorGroupOptional.get());
		}
		throw new DataNotFoundException(name, messageConfiguration.getError().get("OI-101"));
	}

	public VendorGroupDTO findByCode(String code) {

		Optional<VendorGroup> vendorGroupOptional = vendorGroupRepository.findByCode(code);
		if (vendorGroupOptional.isPresent()) {
			return convertEntityToDto(vendorGroupOptional.get());
		}
		throw new DataNotFoundException(code, messageConfiguration.getError().get("OI-101"));
	}

	public VendorGroupDTO findById(Integer id) {

		Optional<VendorGroup> vendorGroupOptional = vendorGroupRepository.findById(id);
		if (vendorGroupOptional.isPresent()) {
			return convertEntityToDto(vendorGroupOptional.get());
		}
		throw new DataNotFoundException(id, messageConfiguration.getError().get("OI-101"));
	}

	private VendorGroup convertDtoToEntity(VendorGroupDTO vendorGroupDTO) throws MappingException {
		VendorGroup vendorGroup = modelmapper.map(vendorGroupDTO, VendorGroup.class);
//		vendorGroup.setVendorGroupParent(vendorGroupRepository.findById(vendorGroupDTO.getVendorGroupParent().getId()).get());
		
		return vendorGroup;
	}
	private VendorGroup mergeVendorGroup(VendorGroupDTO vendorGroupDto, VendorGroup vendorGroup) throws EntityNotFoundException, MappingException {
		modelmapper.map(vendorGroupDto,vendorGroup);
		return vendorGroup;
	}

	private VendorGroupDTO convertEntityToDto(VendorGroup vendorGroup) throws MappingException {
		return modelmapper.map(vendorGroup, VendorGroupDTO.class);
	}

	private boolean isDuplicateVendorGroup(VendorGroupDTO vendorGroupDTO) {
		return this.vendorGroupRepository
				.findByNameOrCode(vendorGroupDTO.getName().trim(), vendorGroupDTO.getCode().trim()).isPresent();
	}

	private boolean isDuplicateVendorGroupForUpdate(VendorGroupDTO vendorGroupDTO) {
		Optional<VendorGroup> vendorGroupOptional = vendorGroupRepository
				.findByNameOrCode(vendorGroupDTO.getName().trim(), vendorGroupDTO.getCode().trim());
		if (vendorGroupOptional.isPresent()) {
			if (vendorGroupOptional.get().getId() == vendorGroupDTO.getId()) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

}
