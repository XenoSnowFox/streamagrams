package com.xenosnowfox.streamagrams.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for working with Properties.
 */
@UtilityClass
public class PropertiesUtils {

	/**
	 * Loads the properties from a specified resource file.
	 *
	 * @param withResourceFileName
	 * 		Name of the properties' resource file to load
	 * @return project properties.
	 * @throws IOException
	 * if an error occurred when reading from the resource file
	 */
	public Properties fromResources(@NonNull final String withResourceFileName) throws IOException {
		try (InputStream input = PropertiesUtils.class.getClassLoader().getResourceAsStream(withResourceFileName)) {
			assert input != null : "Properties file `" + withResourceFileName + "` does not exist.";
			Properties properties = new Properties();
			properties.load(input);
			return properties;
		}
	}
}
