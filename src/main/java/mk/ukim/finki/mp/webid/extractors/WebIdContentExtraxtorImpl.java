package mk.ukim.finki.mp.webid.extractors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WebIdContentExtraxtorImpl implements WebIdContentExtractor {

	@Override
	public String extract(String webId) {
		System.out.println("EXTRACTING");

		URL url;
		String content = "";
		StringBuilder sb = new StringBuilder();

		try {
			// get URL content
			url = new URL(webId);
			URLConnection conn = url.openConnection();

			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));

			String inputLine;

			while ((inputLine = br.readLine()) != null) {
				sb.append(inputLine + "\n");
			}

			br.close();

			System.out.println("Done");

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		content = sb.toString();
		return content;
	}

}
