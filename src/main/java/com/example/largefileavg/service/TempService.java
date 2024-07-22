package com.example.largefileavg.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.comparator.Comparators;

import com.example.largefileavg.exception.InvalidFormatException;
import com.example.largefileavg.model.AverageDTO;

@Service
public class TempService {

	@Value("${filepath}")
	private String filepath;
	
	private TempCache tempCache;
	
	private Object readCacheLock = new Object();
	
	private Object addCityLock = new Object();
	
	private Object addYearLock = new Object();
	
	public TempService(TempCache tempCache) {
		this.tempCache = tempCache;
	}
	
	public List<AverageDTO> getAverageTemp(String city) throws IOException {
		synchronized(readCacheLock) {
			if (!tempCache.isUpToDate()) {
				calculateAvg();
			} 
		}
		return tempCache.getDataForCity(city);
	}

	private static class AvgData {
		private double sum;
		private long count;
		
		private synchronized void addValue(double val) {
			sum += val;
			count++;
		}
	}
	
	private void calculateAvg() throws IOException {
		var resource = new ClassPathResource(filepath);
		var br = Files.newBufferedReader(Paths.get(resource.getURI()));
		final Map<String, Map<String, AvgData>> data = new ConcurrentHashMap<>();
		br.lines().parallel().forEach(line -> {
			final String[] values = line.split(";");
			if (values.length != 3) {
				throw new InvalidFormatException("Invalid number of columns in CSV");
			}
			var city = values[0].toLowerCase();
			if (values[1].length() != 23) {
				throw new InvalidFormatException("Invalid timestamp length");
			}
			var year = values[1].substring(0, 4);
			synchronized(addCityLock) {
				data.computeIfAbsent(city, c -> new ConcurrentHashMap<>());
			}
			synchronized(addYearLock) {
				data.get(city).computeIfAbsent(year, v -> new AvgData());
			}
			var avgData = data.get(city).get(year);
			avgData.addValue(Double.parseDouble(values[2]));
		});

		final Map<String, List<AverageDTO>> result = new HashMap<>();
		data.forEach((city, map) -> {
			map.forEach((year, avgData) -> {
				if (avgData.count > 0) {
					result.computeIfAbsent(city, c -> new ArrayList<>()).add(new AverageDTO(year, avgData.sum / avgData.count));
				}
			});
		});
		tempCache.setData(result);
	}
}
