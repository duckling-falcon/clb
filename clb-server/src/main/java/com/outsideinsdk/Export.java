/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package com.outsideinsdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * The <code>Export</code> class represents the interface for performing file
 * conversions. These conversions are done by making calls to the
 * {@link #convert(String, String, String) convert} method, which can be called
 * repeatedly for any instance of this class. It also allows for the setting of
 * conversion properties that are persistant across <code>convert</code> calls.
 * 
 * @author John Pazdernik
 * @version 1.00
 * @see <a
 *      href=http://www.java.sun.com/j2se/1.3/docs/api/java/util/Properties.html
 *      >java.util.Properties</a>
 */
public class Export {

	/**
	 * Constant strings containing the keys for the required command line
	 * parameters.
	 */
	private static final String INPUTPATH_U = "inputpath_u";
	private static final String OUTPUTPATH_U = "outputpath_u";
	private static final String OUTPUTFORMAT = "outputid";

	private static final Logger LOG = Logger.getLogger(Export.class);
	/**
	 * The encoding map for converting bytes into base64 characters.
	 */
	private static final byte BASE64_MAP[] = { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4a, 0x4b, 0x4c,
			0x4d, 0x4e, 0x4f, 0x50, 0x51, 0x52, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59, 0x5a, 0x61, 0x62, 0x63, 0x64,
			0x65, 0x66, 0x67, 0x68, 0x69, 0x6a, 0x6b, 0x6c, 0x6d, 0x6e, 0x6f, 0x70, 0x71, 0x72, 0x73, 0x74, 0x75, 0x76,
			0x77, 0x78, 0x79, 0x7a, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x2b, 0x2f, };

	/**
	 * String representing the default name of the process to execute in
	 * <code>convert</code>. This name can be overridden by specifying a full
	 * pathname in the EXEPATH property.
	 */
	private static final String COMMAND_NAME = "exporter.exe";
	private static final String COMMAND_NAME_UNIX = "exporter";

	/**
	 * Default timeout value for the export process.
	 */
	private static final long defaultTimeout = 0; // No timeout

	/**
	 * The properties that are used when executing a conversion.
	 */
	private Properties properties = null;

	private String[] execParams = new String[4];
	private String workDir = null;
	private boolean unix = true;

	/**
	 * Creates a new <code>Export</code> object with no properties.
	 */
	public Export() {
		// Determine the name of the directory which contains the Class resource
		// for this object.
		// This directory will be used as the default expected location for the
		// commandName exe.
		// Note that this resource is expected to be a jar file at this time.
		URL url = Export.class.getResource("Export.class");
		char[] buf = url.toString().toCharArray();

		String osName = System.getProperty("os.name");

		if (osName.startsWith("Windows") == true)
			unix = false;
		else
			unix = true;

		for (int i = 0; i < buf.length; i++) {
			if (buf[i] == '!') {
				for (; i >= 0; i--) {
					if (buf[i] == '/')
						break;
				}

				i++;
				for (int j = i; j >= 0; j--) {
					if (buf[j] == ':') {

						if (unix == true)
							j++;
						else
							j--;

						workDir = new String(buf, j, i - j);
						break;
					}
				}
				break;
			}
		}
	}

	/**
	 * Creates a new <code>Export</code> object with the supplied properties.
	 * These properties are copied to protect them from modifications external
	 * to this object.
	 * 
	 * @param properties
	 *            The properties to use when executing conversions.
	 */
	public Export(Properties properties) {
		this();
		setRuntimeProperties(properties);
	}

	/**
	 * Gets the current properties.
	 * 
	 * @return A copy of the properties currently associated with this
	 *         conversion.
	 */
	public Properties getRuntimeProperties() {
		if (properties == null)
			return null;

		return (Properties) properties.clone();
	}

	/**
	 * Sets the properties to be used for all subsequent conversions executed by
	 * this <code>Export</code> object. These properties are copied to protect
	 * them from modifications external to this object.
	 * 
	 * @param properties
	 *            The properties to use when executing conversions.
	 */
	public void setRuntimeProperties(Properties properties) {
		if (properties != null)
			this.properties = (Properties) properties.clone();
		else
			this.properties = null;

		parseProperties();
	}

	/**
	 * Convert a file by executing a native process with no timeout. Use the
	 * supplied parameters and the previously set properties to construct a
	 * command line String array. Execute this command line using a Runtime.exec
	 * method, wait for the process to complete, capture the return value, wrap
	 * it in an {@link ExportStatusCode ExportStatusCode} and return.
	 * 
	 * @param inputPath
	 *            The full pathname of the file to convert.
	 * @param outputPath
	 *            The full pathname of the destination file for the conversion.
	 * @param outputFormat
	 *            The type of format to convert the inputPath file into. See
	 *            {@link ExportFormat ExportFormat} for the available format
	 *            types.
	 * @return Return value of the conversion wrapped as an
	 *         <code>ExportStatusCode</code>.
	 */

	public ExportStatusCode convert(String inputPath, String outputPath, String outputFormat) {
		return convert(inputPath, outputPath, outputFormat, defaultTimeout);
	}

	/**
	 * Convert a file by executing a native process. Use the supplied parameters
	 * and the previously set properties to construct a command line String
	 * array. Execute this command line using a Runtime.exec method, wait for
	 * the process to complete, capture the return value, wrap it in an
	 * {@link ExportStatusCode ExportStatusCode} and return.
	 * 
	 * @param inputPath
	 *            The full pathname of the file to convert.
	 * @param outputPath
	 *            The full pathname of the destination file for the conversion.
	 * @param outputFormat
	 *            The type of format to convert the inputPath file into. See
	 *            {@link ExportFormat ExportFormat} for the available format
	 *            types.
	 * @param timeout
	 *            Timeout interval in milliseconds; 0 means no timeout.
	 * @return Return value of the conversion wrapped as an
	 *         <code>ExportStatusCode</code>.
	 */

	public ExportStatusCode convert(String inputPath, String outputPath, String outputFormat, long timeout) {
		if (properties != null && properties.getProperty(ExportProperties.EXEPATH) != null) {
			execParams[0] = properties.getProperty(ExportProperties.EXEPATH);
			if (unix == true) {
				if (execParams[0].endsWith(".exe"))
					execParams[0] = execParams[0].substring(0, (execParams[0].length() - 4));
			} else {
				if (execParams[0].endsWith(".exe") == false)
					execParams[0] += ".exe";
			}
		} else {
			if (unix == true)
				execParams[0] = workDir + COMMAND_NAME_UNIX;
			else
				execParams[0] = workDir + COMMAND_NAME;
		}

		/*
		 * The 1.5 JVM has a bug which causes a problem with quoted UNC
		 * pathnames. I have tested unquoted names on both 1.4 and 1.5 VMs, and
		 * cannot find a definitive need for the quotes in either case, so I am
		 * simply removing them
		 * /usr/local/pdfexport/bin/exporter inputpath="/home/clive/downloads/spi.docx" outputpath="/home/clive/downloads/spi.pdf" outputid=FI_PDF exportendpage=101 exepath=/usr/local/pdfexport/bin/exporter whattoexport=range exportstartpage=101 outputid=FI_PDF fontdirectory=/usr/local/pdfexport/fonts/
		 */

		/*
		 * if (unix == false) execParams[0] = "\"" + execParams[0] + "\"";
		 */

		if (unix == false) {
			execParams[1] = "\"" + INPUTPATH_U + "=" + base64Encode(inputPath) + "\"";
			execParams[2] = "\"" + OUTPUTPATH_U + "=" + base64Encode(outputPath) + "\"";
			execParams[3] = "\"" + OUTPUTFORMAT + "=" + outputFormat + "\"";
		} else {
			execParams[1] = INPUTPATH_U + "=" + "\"" + base64Encode(inputPath) + "\"";
			execParams[2] = OUTPUTPATH_U + "=" + "\"" + base64Encode(outputPath) + "\"";
			execParams[3] = OUTPUTFORMAT + "=" + outputFormat;
		}

		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		ExportStatusCode statusCode = null;
		ExportShutdownHook shutdownHook = null;

		if (timeout <= 0) {
			// No timeout. Start the export process from this thread.
			try {
				process = runtime.exec(execParams);
				// Install a shutdown hook to perform cleanup if we're interrupted.
				shutdownHook = new ExportShutdownHook(process);
				runtime.addShutdownHook(shutdownHook);
				process.waitFor();
				String errorMessage = readErrorMessage(process.getErrorStream());
				boolean isStartError = isStartPageError(errorMessage);
				if(isStartError){
					return ExportStatusCode.SCCERR_STARTPAGEERROR;
				}
				if(process.exitValue()!= 0 && !isStartError){
					LOG.error("Convert error "+ errorMessage);
				}
				runtime.removeShutdownHook(shutdownHook);
				shutdownHook.finished();
				statusCode = new ExportStatusCode(process.exitValue());
			} catch (IOException ex) {
				if (shutdownHook != null) {
					runtime.removeShutdownHook(shutdownHook);
					shutdownHook.finished();
				}
				statusCode = ExportStatusCode.SCCERR_JAVA_IO_ERROR;
			} catch (InterruptedException ex) {
				runtime.removeShutdownHook(shutdownHook);
				shutdownHook.finished();
				process.destroy();
				statusCode = ExportStatusCode.SCCERR_JAVA_INTERRUPTED;
			}
		} else {
			// Non-zero timeout.
			long[] waitIntervals = new long[] { 50, 100, 250, 250, 500, 500, 500, 500, 1000 };
			int waitIndex = 0;
			long totalWait = 0;

			try {
				process = runtime.exec(execParams);

				// Install the shutdown hook.
				shutdownHook = new ExportShutdownHook(process);
				runtime.addShutdownHook(shutdownHook);

				boolean done = false;
				while (!done) {
					Thread.sleep(waitIntervals[waitIndex]);
					totalWait += waitIntervals[waitIndex];

					try {
						int exitValue = process.exitValue();
						if(process.exitValue()!= 0){
							String errorMessage = readErrorMessage(process.getErrorStream());
							LOG.error("Convert error "+ errorMessage);
						}
						runtime.removeShutdownHook(shutdownHook);
						shutdownHook.finished();
						statusCode = new ExportStatusCode(exitValue);
						done = true;
					} catch (IllegalThreadStateException ex) {
						// The process is still running.
						if (totalWait > timeout) {
							runtime.removeShutdownHook(shutdownHook);
							shutdownHook.finished();
							process.destroy();
							statusCode = ExportStatusCode.SCCERR_JAVA_TIMEDOUT;
							done = true;
						} else if (waitIndex + 1 < waitIntervals.length) {
							++waitIndex;
						}
					}
				}
			} catch (IOException ex) {
				if (shutdownHook != null) {
					runtime.removeShutdownHook(shutdownHook);
					shutdownHook.finished();
				}
				statusCode = ExportStatusCode.SCCERR_JAVA_IO_ERROR;
			} catch (InterruptedException ex) {
				runtime.removeShutdownHook(shutdownHook);
				shutdownHook.finished();
				process.destroy();
				statusCode = ExportStatusCode.SCCERR_JAVA_INTERRUPTED;
			}
		}

		return statusCode;
	}

	private String readErrorMessage(InputStream errorStream) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(errorStream));
		StringBuffer sb = new StringBuffer();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line).append("\n");
		}
		
		return sb.toString().trim();
	}

	private boolean isStartPageError(String errorMessage) {
		if("EXRunExport() returned error code 23 (requested page not available)".equals(errorMessage)){
			return true;
		} 
		if("EXRunExport() returned error code 1792 (The start page value is larger than the number of pages in the document)".equals(errorMessage)){
			return true;
		}
		return false;
	}

	/**
	 * Take the individual properties out of the Properties object, and if they
	 * are not the EXEPATH property, or their values are not equal to DEFAULT,
	 * place them into the String array execParams.
	 */
	private void parseProperties() {
		if (properties == null) {
			execParams = new String[4];
			return;
		}

		int count = 0;
		@SuppressWarnings("rawtypes")
		Enumeration enums = properties.propertyNames();

		// First, determine the number of properties that will be used as params.
		while (enums.hasMoreElements()) {
			String key = (String) enums.nextElement();
			String value = properties.getProperty(key);
			if (value != ExportProperties.DEFAULT && key != ExportProperties.EXEPATH)
				count++;
		}

		// allocate space for the number of parameters that was just determined,
		// plus 4 slots for the
		// commandName and the 3 required parameters that will be passed to
		// convert().
		execParams = new String[count + 4];
		count = 4;
		enums = properties.propertyNames();

		// Now place the properties into their params slots, encoding those that
		// require it.
		while (enums.hasMoreElements()) {
			String key = (String) enums.nextElement();
			String value = properties.getProperty(key);
			if (value != ExportProperties.DEFAULT && key != ExportProperties.EXEPATH) {
				if (key == ExportProperties.TEMPLATE)
					execParams[count] = ExportProperties.TEMPLATE_U + "=" + base64Encode(value);
				else if (key == ExportProperties.TEMPDIR)
					execParams[count] = ExportProperties.TEMPDIR_U + "=" + base64Encode(value);
				else
					execParams[count] = key + "=" + value;
				if (unix == false)
					execParams[count] = "\"" + execParams[count] + "\"";
				count++;
			}
		}
	}

	/**
	 * Encode a string using Base64 encoding.
	 * 
	 * @param src
	 *            The string to encode.
	 * @return A new string, which is the base64 encoded version of src.
	 */
	private String base64Encode(String src) {
		char[] srcChars = src.toCharArray();
		short[] srcBytes = new short[srcChars.length * 2];
		int index = 0;

		// First, break the 2 byte chars into single bytes.
		for (int i = 0; i < srcChars.length; i++) {
			srcBytes[index++] = (short) (srcChars[i] >> 8);
			srcBytes[index++] = (short) (srcChars[i] & 0xff);
		}

		// Determine the size of the output string.
		int newLength = srcBytes.length * 4 / 3;
		if (newLength % 4 != 0)
			newLength = (1 + (newLength / 4)) * 4;

		char[] destChars = new char[newLength];
		index = 0;
		int i = 0;

		// Encode the bytes 3 at a time, by shifting them to unique positions
		// within an int and calling encode().
		for (; i + 2 < srcBytes.length; i += 3, index += 4)
			encode(srcBytes[i] << 16 | srcBytes[i + 1] << 8 | srcBytes[i + 2], destChars, index);

		// Handle any bytes left over, that didn't hit on a 3 byte boundary.
		switch (srcBytes.length % 3) {
		case 1: {
			encode(srcBytes[i] << 16, destChars, index);
			index += 2;
			destChars[index++] = 0x3d; // '=' character
			destChars[index++] = 0x3d; // '=' character
			break;
		}
		case 2: {
			encode(srcBytes[i] << 16 | srcBytes[i + 1] << 8, destChars, index);
			index += 3;
			destChars[index++] = 0x3d; // '=' character
			break;
		}
		}

		return new String(destChars);
	}

	/**
	 * Unpacks 3 bytes into 4 locations in the destination array, and also
	 * encodes them using Base64.
	 * 
	 * @param val
	 *            The integer containing the 3 packed bytes to be encoded.
	 * @param dest
	 *            The char array where the encoded bytes are to be placed.
	 * @param index
	 *            The location in the destination array to place the encoded
	 *            bytes.
	 */
	private void encode(int val, char[] dest, int index) {
		dest[index] = (char) BASE64_MAP[(val & 0xfc0000) >> 18];
		dest[index + 1] = (char) BASE64_MAP[(val & 0x3f000) >> 12];
		dest[index + 2] = (char) BASE64_MAP[(val & 0xfc0) >> 6];
		dest[index + 3] = (char) BASE64_MAP[(val & 0x3f)];
	}
}

/**
 * Private class used to shut down the export process if the JVM is interrupted.
 */
class ExportShutdownHook extends Thread {
	private Process process = null;

	public ExportShutdownHook(Process proc) {
		super();
		process = proc;
	}

	public void run() {
		process.destroy();
	}

	public void finished() {
		try {

			InputStream in = process.getInputStream();
			OutputStream out = process.getOutputStream();
			InputStream err = process.getErrorStream();

			in.close();
			out.close();
			err.close();

			this.process = null;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}