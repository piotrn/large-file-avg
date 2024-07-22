package com.example.largefileavg.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AverageDTO {
	private String year;
	private double averageTemperature;
}
