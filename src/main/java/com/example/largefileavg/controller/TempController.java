package com.example.largefileavg.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.largefileavg.model.AverageDTO;
import com.example.largefileavg.service.TempService;

@RestController
public class TempController {

	private TempService service;
	
	public TempController(TempService service) {
		super();
		this.service = service;
	}

	@GetMapping
	public List<AverageDTO> getAverageTemp(@RequestParam String city) throws IOException {
		return service.getAverageTemp(city);
	}
}
