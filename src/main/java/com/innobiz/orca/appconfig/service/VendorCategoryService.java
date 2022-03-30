package com.innobiz.orca.appconfig.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.innobiz.orca.appconfig.dto.VendorCategoryDTO;


@Service
public interface VendorCategoryService {
	
	public  VendorCategoryDTO create(VendorCategoryDTO  vendorCategoryDTO); 
	
	public VendorCategoryDTO update(VendorCategoryDTO vendorCategoryDTO);
	
	public void  delete(Integer id);
	
	public List<VendorCategoryDTO> findAll();
	
	VendorCategoryDTO findById(Integer id);

	VendorCategoryDTO findByName(String name);
	
	VendorCategoryDTO findByCode(String code);
}
	

	
		