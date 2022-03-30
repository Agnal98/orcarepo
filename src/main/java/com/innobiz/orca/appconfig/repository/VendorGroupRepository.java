package com.innobiz.orca.appconfig.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.innobiz.orca.appconfig.entity.VendorGroup;

public interface VendorGroupRepository extends JpaRepository <VendorGroup, Integer> {
	
List<VendorGroup> findAll();
	
	Optional<VendorGroup> findByName(String name);
	
	Optional<VendorGroup> findByCode(String code);
	
	Optional<VendorGroup> findById(Integer id);
	
	Optional<VendorGroup> findByNameOrCode(String name, String code);
	

	

}

