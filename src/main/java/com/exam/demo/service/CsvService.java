package com.exam.demo.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Service
public class CsvService {

	public static List<String> getCsvHeader(String strings) {
		StringBuffer csvHeader = new StringBuffer("");
		List<String> header = getHeaderFromJson(strings);
		for (String str : header) {
			csvHeader.append(str + ",");
		}

		System.out.println("CSV Data:");
		System.out.println(header);

		return header;
	}

	public static List<String> getHeaderFromJson(String strings) {
		List<String> headers = new ArrayList<>();
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject;
		try {
			jsonObject = (JSONObject) jsonParser.parse(strings);

			Set<?> keys = jsonObject.keySet();
			for (Object key : keys) {
				headers.add(key.toString());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return headers;
	}

	public static Map<String, String> readCsvFile(String filename) {
		String line = "";
		String splitBy = ",";
		Map<String, String> playerFromCsv = new HashMap<>();

		try {
			try (// parsing a CSV file into BufferedReader class constructor
					BufferedReader br = new BufferedReader(new FileReader(filename))) {
				while ((line = br.readLine()) != null) {
					String[] player = line.split(splitBy);
					if (player[0].equals("id"))
						continue;
					playerFromCsv.put(player[0], player[1]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return playerFromCsv;
	}

	public static void writePlayersFullDetailsToCsv(String newFileName, List<String> playersDetailsFromAPI,
			Map<String, String> playerFromCsv) throws ParseException {
		File csvOutputFile = new File(newFileName);
		try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject;
			Object value;

			List<String> header = getCsvHeader(playersDetailsFromAPI.get(0));
			String headerStr = header.toString().replaceAll("[\\[\\]]", "") + ",nickname";
			pw.println(headerStr);

			for (String string : playersDetailsFromAPI) {
				String csvData = getDetailsFromJson(header, string);
				System.out.println(csvData);

				jsonObject = (JSONObject) jsonParser.parse(string);
				value = (Object) jsonObject.get("id");

				csvData = csvData + playerFromCsv.get(value.toString()) + ","; // adds nickname
				pw.println(csvData);
			}
			System.out.println();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static String getDetailsFromJson(List<String> header, String json) {
		String detailsFromJson = "";
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
			JSONObject jsonTeamObject;

			for (String key : header) {
				Object value = (Object) jsonObject.get(key.toString());
				if (value != null) {
					if (key.toString().equals("team")) {
						jsonTeamObject = (JSONObject) jsonParser.parse(value.toString());
						String teamName = jsonTeamObject.get("full_name").toString();
						detailsFromJson += teamName;
					} else {
						detailsFromJson += value.toString();
					}
				}
				detailsFromJson += ",";
			}
		} catch (org.json.simple.parser.ParseException pe) {
			System.out.println(pe);
		}
		return detailsFromJson;
	}

}
