/*
 *  Copyright (c) 2015 Michal Ďuračík
 */
package vzdelavaniefetcher;

import com.unlink.common.HttpClient.HttpClient;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import vzdelavaniefetcher.tools.ResourceManager;

/**
 *
 * @author Unlink
 */
public class VzdelavanieLogin {

	private HttpClient aClient;

	public VzdelavanieLogin(HttpClient paClient) {
		this.aClient = paClient;
	}

	private String makeRedirect() throws IOException {
		final File file = File.createTempFile("uniza-redirector", ".html");
		file.createNewFile();
		file.deleteOnExit();
		try (FileWriter fw = new FileWriter(file)) {
			String sablona = ResourceManager.readResourceToString("redirect.html");
			sablona = sablona.replace("{{NAME}}", Fetcher.dajInstanciu().getMeno());
			sablona = sablona.replace("{{PASSWORD}}", Fetcher.dajInstanciu().getHeslo());
			fw.write(sablona);
		}
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				file.delete();
			}
		}, 1000000*10);
		return file.toURI().toString();
	}

	public URI createUri() throws IOException, URISyntaxException {
		return new URI(makeRedirect());
	}

}
