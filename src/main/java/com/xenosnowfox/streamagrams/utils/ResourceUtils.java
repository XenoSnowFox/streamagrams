package com.xenosnowfox.streamagrams.utils;

import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Utility class for working with resource files.
 */
@UtilityClass
public class ResourceUtils {

	/**
	 * Attempts to read in and return the resource of the given name as a string.
	 *
	 * @param withResourceFileName
	 * 		filename of the resource to read in
	 * @return String contents of the file
	 */
	public String getResourceAsString(final String withResourceFileName) {
		try (InputStream inputStream = ResourceUtils.class.getResourceAsStream(withResourceFileName)) {
			// Ensure the file exists.
			if (inputStream == null) {
				String errMessage = "Unable to read resource `" + withResourceFileName + "`.";

				if (!withResourceFileName.startsWith("/")
						&& ResourceUtils.class.getResource("/" + withResourceFileName) != null) {
					errMessage += " Were you meaning to include a slash at the beginning of the path (`" + "/"
							+ withResourceFileName + "`)?";
				}

				throw new NullPointerException(errMessage);
			}

			// compile into a string
			StringBuilder resultStringBuilder = new StringBuilder();
			try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
				String line;
				while ((line = br.readLine()) != null) {
					resultStringBuilder.append(line)
							.append("\n");
				}
			}
			return resultStringBuilder.toString();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
