package com.exam.demo.controller;

import java.util.List;
import java.util.Map;

import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;

import com.exam.demo.service.CachingService;
import com.exam.demo.service.CsvService;
import com.exam.demo.service.RestApiService;

@Controller
public class AppController {
	public final static String ORIGIN_CSV_FILE = "D:\\\\Downloads\\\\players.csv";
	public final static String NEW_CSV_FILE = "D:\\\\Downloads\\\\new_players.csv";
	public final static String CACHE_FILE_PATH = "D:\\\\Downloads\\\\";
	public final static String API_URL = "https://www.balldontlie.io/api/v1/players/";

	public CsvService csvService = new CsvService();

	public static void run() throws ParseException {
		Map<String, String> playerFromCsv = CsvService.readCsvFile(ORIGIN_CSV_FILE);

		List<String> playersDetailsFromAPI = RestApiService.getPlayersDetailsFromAPI(playerFromCsv, API_URL);
		CachingService.saveToCache(CACHE_FILE_PATH, playersDetailsFromAPI);
		CsvService.writePlayersFullDetailsToCsv(NEW_CSV_FILE, playersDetailsFromAPI, playerFromCsv);
	}

}
