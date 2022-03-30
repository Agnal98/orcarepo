package com.innobiz.orca.appconfig.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.innobiz.orca.appconfig.entity.ProductGroup;
import com.innobiz.orca.appconfig.entity.VendorCategory;

public interface VendorCategoryRepository extends JpaRepository <VendorCategory, Integer> {
	
	List<VendorCategory> findAll();
	
	Optional<VendorCategory> findByName(String name);
	
	Optional<VendorCategory> findByCode(String code);
	
	Optional<VendorCategory> findById(Integer id);
	
	Optional<VendorCategory> findByNameOrCode(String name, String code);
	

	

}
