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
package cn.vlabs.clb.server.ui.frameservice.pdf;

import java.io.*;
import java.security.*;
import java.util.*;

import org.apache.commons.io.IOUtils;

import com.outsideinsdk.Export;
import com.outsideinsdk.ExportStatusCode;

/**
 * The PdfConvertAdapter is modified by <code>ExportTest</code> in pdfexporter.
 * The configuration file is assumed to be correctly formatted.
 * 
 * @author Kevin Glannon
 * @version 1.00
 * @see Export Export
 */
public class PdfConvertAdapter {
	private static final String INPUTPATHKEY = "inputpath";
	private static final String OUTPUTPATHKEY = "outputpath";
	private static final String OUTPUTIDKEY = "outputid";

	Properties convertConfig = new Properties();
	Properties probeConfig = new Properties();
	Export exporter = null;
	Export prober = null;

	public PdfConvertAdapter(String cfp,String pcfg)  {
		parseConfig(cfp,convertConfig);
		parseConfig(pcfg,probeConfig);
		convertConfig.remove(INPUTPATHKEY);
		convertConfig.remove(OUTPUTPATHKEY);
		probeConfig.remove(INPUTPATHKEY);
		probeConfig.remove(OUTPUTPATHKEY);
		exporter = new Export(convertConfig);
		prober = new Export(probeConfig);
	}

	public void parseConfig(String cfp,Properties props) {
		File cff = new File(cfp);
		if (!cff.exists() || !cff.isFile() || !cff.canRead()) {
			throw (new InvalidParameterException("Configuration file unreadable."));
		}
		BufferedReader cfr = null;
		try {
			cfr = new BufferedReader(new FileReader(cff));
			String line;
			while ((line = cfr.readLine()) != null) {
				processLine(line,props);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(cfr);
		}
	}

	private void processLine(String l,Properties props) {
		// Look for comments.
		int indPound = l.indexOf('#');
		// Remove comments and whitespace.
		String line = (indPound == -1) ? l.trim() : l.substring(0, indPound).trim();
		if (line.length() != 0) {
			StringTokenizer stl = new StringTokenizer(line);
			String key = stl.nextToken();
			String value = stl.nextToken();
			while (stl.hasMoreTokens()) {
				value += " " + stl.nextToken();
			}
			// Fill in the appropriate property.
			props.setProperty(key, value);
		}
	}

	/**
	 * Run the conversion using the given input path, output path.
	 * 
	 * @param srcPath
	 *            Input path.
	 * @param desPath
	 *            Output path.
	 * @param timeout
	 *            Export process timeout in milliseconds.
	 */
	public ExportStatusCode convert(String srcPath, String desPath, long timeout) {
		//只转换1-100页
		String oid = convertConfig.getProperty(OUTPUTIDKEY);
		ExportStatusCode result = exporter.convert(srcPath, desPath, oid, timeout);
		return result;
	}
	
	//探测是否有第101页
	public ExportStatusCode probeConvert(String srcPath,String desPath){
		String oid = probeConfig.getProperty(OUTPUTIDKEY);
		ExportStatusCode result = prober.convert(srcPath, desPath, oid);
		return result;
	}
	
	public static void main(String[] args){
		PdfConvertAdapter adapter = new PdfConvertAdapter("/home/clive/code/jee/clb/src/main/webapp/WEB-INF/conf/clb-convert.cfg",
				"/home/clive/code/jee/clb/src/main/webapp/WEB-INF/conf/clb-probe.cfg");
		ExportStatusCode sc = adapter.probeConvert("/home/clive/downloads/spi.docx", "/home/clive/downloads/spi.pdf");
		System.out.println(sc);
	}
}
