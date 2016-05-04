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

import java.io.IOException;

/**
 * The <code>ExportThread</code> class represents the thread that launches the
 * export process.
 */

public class ExportThread extends Thread {
	private String[] execParams = null;
	private ExportStatusCode statusCode = ExportStatusCode.SCCERR_UNKNOWN;

	/**
	 * Create the <code>ExportThread</code> object. The supplied parameters will
	 * be passed to the native process.
	 * 
	 * @param params
	 *            String array containing command line arguments for the native
	 *            process.
	 */

	public ExportThread(String[] params) {
		super();
		execParams = params;
	}

	/**
	 * Run the native export process with the parameters supplied to the
	 * constructor. The status of the export can be retrieved by calling
	 * <code>getStatusCode</code>.
	 */

	public void run() {
		Process exportProc = null;
		try {
			exportProc = Runtime.getRuntime().exec(execParams);
			exportProc.waitFor();
		} catch (IOException ex) {
			statusCode = ExportStatusCode.SCCERR_JAVA_IO_ERROR;
			return;
		} catch (InterruptedException ex) {
			// We've been interrupted (probably a timeout). Kill the process.
			exportProc.destroy();
			statusCode = ExportStatusCode.SCCERR_JAVA_INTERRUPTED;
			return;
		}
		statusCode = new ExportStatusCode(exportProc.exitValue());
	}

	/**
	 * Return the <code>ExportStatusCode</code> for the result of the export.
	 * 
	 * @return The <code>ExportStatusCode</code> result of the export process.
	 */
	public ExportStatusCode getStatusCode() {
		return statusCode;
	}
}