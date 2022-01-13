package com.exam.demo.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.exam.demo.controller.AppController;

@Service
public class CachingService {
	private final WSService wsService;

	CachingService(WSService wsService) {
		this.wsService = wsService;
	}

	// schedule a job to update caching (every 15 minutes)
	@Scheduled(fixedRate = 900000)
	public void updateCahching() {
		Map<String, String> playerFromCsv = CsvService.readCsvFile(AppController.ORIGIN_CSV_FILE);

		List<String> playersDetailsFromAPI = RestApiService.getPlayersDetailsFromAPI(playerFromCsv,
				AppController.API_URL);
		List<String> updatedPlayersIds = checkCacheUpdate(AppController.CACHE_FILE_PATH, playersDetailsFromAPI);

		if (!updatedPlayersIds.isEmpty()) {
			wsService.sendMessages(updatedPlayersIds);
		}
	}

	public List<String> checkCacheUpdate(String cacheFilePath, List<String> playersDetailsFromAPI) {
		String fileName = "";
		List<String> updatedPlayersIds = new ArrayList<String>();
		Object obj;
		JSONParser parser = new JSONParser();
		JSONObject jsonObject;

		try {
			for (String strings : playersDetailsFromAPI) {
				JSONObject jsonObjectFromAPI = (JSONObject) parser.parse(strings);
				Object value = (Object) jsonObjectFromAPI.get("id");
				String playersId = value.toString();
				fileName = cacheFilePath + playersId + ".json";

				obj = parser.parse(new FileReader(fileName));
				jsonObject = (JSONObject) obj;

				if (!jsonObject.equals(jsonObjectFromAPI)) {
					savePlayerToCache(fileName, strings.toString());
					System.out.println("Updated player #" + playersId + " " + new Date().toString());
					updatedPlayersIds.add(playersId);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return updatedPlayersIds;
	}

	public static void saveToCache(String filePath, List<String> playersDetailsFromAPI) throws ParseException {
		String fileName;
		JSONParser jsonParser = new JSONParser();
		for (String strings : playersDetailsFromAPI) {
			JSONObject jsonObject = (JSONObject) jsonParser.parse(strings);
			Object value = (Object) jsonObject.get("id");
			String playersId = value.toString();
			fileName = filePath + playersId + ".json";
			savePlayerToCache(fileName, strings.toString());
		}
	}

	public static void savePlayerToCache(String fileName, String string) {
		try (FileWriter file = new FileWriter(fileName)) {
			file.write(string);
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
