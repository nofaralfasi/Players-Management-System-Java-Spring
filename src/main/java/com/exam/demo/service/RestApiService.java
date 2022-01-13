package com.exam.demo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class RestApiService {

	public static List<String> getPlayersDetailsFromAPI(Map<String, String> playerFromCsv, String urlAddress) {
		String inputLine;
		List<String> dataLines = new ArrayList<>();
		StringBuffer content;
		BufferedReader in;
		Set<String> playersIds = playerFromCsv.keySet();

		try {
			for (String id : playersIds) {
				String url_address = urlAddress + id;
				URL url = new URL(url_address);
				content = new StringBuffer();

				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");

				in = new BufferedReader(new InputStreamReader(con.getInputStream()));

				while ((inputLine = in.readLine()) != null) {
					content.append(inputLine);
				}

				in.close();
				dataLines.add(content.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dataLines;
	}

}
