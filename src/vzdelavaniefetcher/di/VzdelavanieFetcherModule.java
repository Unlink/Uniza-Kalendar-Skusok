/*
 *  Copyright (c) 2015 Michal Ďuračík
 */
package vzdelavaniefetcher.di;

import com.unlink.common.HttpClient.HttpClient;
import dagger.Module;
import dagger.Provides;
import java.io.File;
import javax.inject.Named;
import vzdelavaniefetcher.GUI.MainFrame;
import vzdelavaniefetcher.tools.SimpleSerializedMappedSet;

@Module(injects = MainFrame.class)
public class VzdelavanieFetcherModule {
	
	@Provides HttpClient provideHttpClient() {
		return new HttpClient();
	}
	
	@Provides @Named("predmetManager.storage") SimpleSerializedMappedSet<String, Integer> provideProjectManagerStorage() {
		return new SimpleSerializedMappedSet<>(new File("terminy.dat"));
	}

}
