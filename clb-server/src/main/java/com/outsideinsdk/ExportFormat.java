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

/**
 * <code>ExportFormat</code> provides all the possible values for the output format of a conversion executed by
 * {@link Export#convert(String, String, String) Export.convert}.
 * 
 * @author John R. Pazdernik
 * @version 1.00
 */
public interface ExportFormat {
	public static final String HTML = "fi_html";
	public static final String WML = "fi_wml";
	public static final String HDML = "fi_hdml";
	public static final String XHTMLBASIC = "fi_xhtmlb";
	public static final String CHTML = "fi_chtml";
	public static final String TEXT = "fi_text";
	public static final String WIRELESSHTML = "fi_wirelesshtml";
	public static final String SEARCHML = "fi_searchml";
	public static final String PAGEML = "fi_pageml";
	public static final String XML_FLEXIONDOC4 = "fi_xml_flexiondoc4";
	public static final String  TIFF = "fi_tiff";
	public static final String  PNG = "fi_png";
	public static final String  BMP = "fi_bmp";
	public static final String  GIF = "fi_gif";
	public static final String  JPEG = "fi_jpeg";
}
