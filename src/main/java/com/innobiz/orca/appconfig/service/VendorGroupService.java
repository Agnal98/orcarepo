package com.innobiz.orca.appconfig.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.innobiz.orca.appconfig.dto.VendorGroupDTO;

@Service
	public interface VendorGroupService {
		
		public VendorGroupDTO create(VendorGroupDTO vendorGroupDTO); 
		
		public VendorGroupDTO update(VendorGroupDTO vendorGroupDTO);
		
		public void  delete(Integer id);
		
		public List<VendorGroupDTO> findAll();
		
		VendorGroupDTO findById(Integer id);
		
		VendorGroupDTO findByName(String name);
		
		VendorGroupDTO findByCode(String code);
		

		
			
		}	

