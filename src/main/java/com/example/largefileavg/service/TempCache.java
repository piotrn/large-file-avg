package com.example.largefileavg.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.example.largefileavg.model.AverageDTO;

@Component
public class TempCache {
	
	@Value("${filepath}")
	private String filepath;
	
	Map<String, List<AverageDTO>> data;
	
	private long lastUpdate;
	
	public boolean isUpToDate() {
		final Resource resource = new ClassPathResource(filepath);
		
		try {
			return resource.getFile().lastModified() < lastUpdate;
		} catch (IOException e) {
			return false;
		}
	}
	
	public void setData(Map<String, List<AverageDTO>> data) {
		this.data = data;
		this.lastUpdate = System.currentTimeMillis();
	}
	
	public List<AverageDTO> getDataForCity(String city) {
		return data.get(city.toLowerCase());
	}
	
}
