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
 * <CODE>ExportProperties</CODE> provides all of the properties and their values that are valid for use in an
 * {@link Export Export} object.
 *
 * The naming convention for the <code>ExportProperties</code> constants is as follows:<br><br>
 *
 * PROPERTY - any constant using an un-separated name represents a valid <code>Property</code> name.<br>
 * PROPERTY_VALUE - any constant using a separated name constisting of a PROPERTY name and some VALUE represents
 *                  a valid VALUE for its respective PROPERTY.<br><br>
 *
 * Special Cases:<br><br>
 *
 * DEFAULT - can be used to represent the default VALUE for any PROPERTY.<br>
 * PROPERTY_U - any constant with a VALUE name of U indicates the use of an encoded VALUE.  These constants are for
 *              internal use, and should not be set on a <code>Properties</code> passed to
 *              {@link Export#setRuntimeProperties(java.util.Properties) Export.setRuntimeProperties}.<br>
 *
 * @author John R. Pazdernik
 * @version 1.00
 */
public interface ExportProperties {

    /** Default property value */
	public static final String DEFAULT = "default";
	/** Path for the exporter executable */
	public static final String EXEPATH = "exepath";

    /** Output file flavor.
     *  Applies to: HTML, Wireless Export SDKs
     */
	public static final String FLAVOR = "flavor";
	/** Netscape Navigator 3.0 output flavor.
	 *  Requires HTML Export SDK
	 */
	public static final String FLAVOR_NS30 = "netscape30";
	/** Netscape Navigator 4.0 (CSS) output flavor.
	 *  Requires HTML Export SDK
	 */
	public static final String FLAVOR_NS40 ="netscape40";
	/** Netscape Navigator 4.6 output flavor.
	 *  Requires HTML Export SDK
	 */
	public static final String FLAVOR_NS46 ="netscape46";
	/** Microsoft Internet Explorer 3.0 output flavor.
	 *  Requires HTML Export SDK
	 */
	public static final String FLAVOR_MS30 ="msie30";
	/** Microsoft Internet Explorer 4.0 (CSS) output flavor.
	 *  Requires HTML Export SDK
	 */
	public static final String FLAVOR_MS40 ="msie40";
	/** Microsoft Internet Explorer 5.0 output flavor.
	 *  Requires HTML Export SDK
	 */
	public static final String FLAVOR_MS50 ="msie50";
	/** Standard HTML 2.0 output flavor.
	 *  Requires HTML Export SDK
	 */
	public static final String FLAVOR_HTML20 ="html20";
	/** Standard HTML 3.0 output flavor.
	 *  Requires HTML Export SDK
	 */
	public static final String FLAVOR_HTML30 ="html30";
	/** Standard HTML 4.0 output flavor.
	 *  Requires HTML Export SDK
	 */
	public static final String FLAVOR_HTML40 ="html40";
	/** Generic HTML support output flavor.
	 *  Requires HTML Export SDK
	 */
	public static final String FLAVOR_GENERICHTML = "generic";
	/** WAP WML 1.1 (no table support).
	 *  Requires Wireless Export SDK
	 */
	public static final String FLAVOR_WML11 = "wml11";
	/** WAP WML 1.1 (with table support).
	 *  Requires Wireless Export SDK
	 */
	public static final String FLAVOR_WML11_TBL = "wml11tbl";
	/** WAP WML 2.0 (with table support).
	 *  Requires Wireless Export SDK
	 */
	public static final String FLAVOR_WML20 = "wml20";
	/** Text (no table support).
	 *  Requires Wireless Export SDK
	 */
	public static final String FLAVOR_TEXT = "text";
	/** XHTML Basic 1.0 (with table support).
	 *  Requires Wireless Export SDK
	 */
	public static final String FLAVOR_XHTMLB1 = "xhtmlb1";
	/** XHTML Basic 1.0 (no table support).
	 *  Requires Wireless Export SDK
	 */
	public static final String FLAVOR_XHTMLB1_NOTBL = "xhtmlb1notbl";
	/** CHTML 2.0 (used by DoCoMo's iMode - no table support).
	 *  Requires Wireless Export SDK
	 */
	public static final String FLAVOR_CHTML2 = "chtml2";
	/** HDML 3.0 (no table support).
	 *  Requires Wireless Export SDK
	 */
	public static final String FLAVOR_HDML = "hdml";
	/** Palm Web Clipping (with table support).
	 *  Requires Wireless Export SDK
	 */
	public static final String FLAVOR_WCA11 = "wca11";
	/** Palm Web Clipping (no table support).
	 *  Requires Wireless Export SDK
	 */
	public static final String FLAVOR_WCA11_NOTBL = "wca11notbl";
	/** AvantGo 3.3 HTML for Palm (with table support).
	 *  Requires Wireless Export SDK
	 */
	public static final String FLAVOR_AG33PALM = "ag33palm";
	/** AvantGo 3.3 HTML for Palm (no table support).
	 *  Requires Wireless Export SDK
	 */
	public static final String FLAVOR_AG33PALM_NOTBL = "ag33palmnotbl";
	/** AvantGo 3.3 HTML for WinCE (with table support).
	 *  Requires Wireless Export SDK
	 */
	public static final String FLAVOR_AG33CE = "ag33ce";
	/** AvantGo 3.3 HTML for WinCE (no table support).
	 *  Requires Wireless Export SDK
	 */
	public static final String FLAVOR_AG33CE_NOTBL = "ag33cenotbl";
	/** Generic HTML for Wireless devices (with table support).
	 *  Requires Wireless Export SDK
	 */
	public static final String FLAVOR_WGENERICHTML = "wgenhtml";

    /** Determines the format to be used for exported graphics.
     *  Applies to: HTML and Wireless Export SDKs.
     */
	public static final String GRAPHICTYPE = "graphictype";
	/** GIF graphics */
	public static final String GRAPHICTYPE_GIF = "gif";
	/** JPEG graphics */
	public static final String GRAPHICTYPE_JPG = "jpg";
	/** WBMP graphics */
	public static final String GRAPHICTYPE_WBMP = "wbmp";
	/** BMP graphics */
	public static final String GRAPHICTYPE_BMP = "bmp";
	/** PNG graphics */
	public static final String GRAPHICTYPE_PNG = "png";
	/** No graphic conversion */
	public static final String GRAPHICTYPE_NONE = "none";

    /** Indicates the template file to be used.
     *  If this option is not set, the Export module will use an internal
     *  default template.
     *  Applies to: HTML and Wireless Export SDKs.
     */
	public static final String TEMPLATE = "template";
	public static final String TEMPLATE_U = "template_u";

    /** Unicode character byte order.
     *  Applies to: HTML and Wireless Export SDKs.
     */
	public static final String CHARBYTEORDER = "charbyteorder";
	/** Use the same byte order as the template file. */
	public static final String CHARBYTEORDER_TEMPLATE = "template";
	/** BigEndian (Motorola) byte order */
	public static final String CHARBYTEORDER_BIGENDIAN = "bigendian";
	/** LittleEndian (Intel) byte order */
	public static final String CHARBYTEORDER_LITTLEENDIAN = "littleendian";

    /** Remove excess white space.
     *  Applies to: HTML and Wireless Export SDKs.
     */
	public static final String COLLAPSEWHITESPACE = "collapsewhitespace";

	/** Generate bullets and numbers.
     *  Applies to: HTML and Wireless Export SDKs.
     */
	public static final String GENBULLETSANDNUMS = "genbulletsandnums";

	/** Label word processing table cells.
     *  Applies to: Wireless Export SDK
     */
	public static final String LABELWPCELLS = "labelwpcells";

	/** Label spreadsheet and database cells.
     *  Applies to: Wireless Export SDK
     */
	public static final String LABELSSDBCELLS = "labelssdbcells";

	/** Use separate buffers for graphics.
     *  Applies to: Wireless Export SDK
     */
	public static final String SEPARATEGRAPHICSBUFFER = "separategraphicsbuffer";

	/** Size in bytes of the graphics buffer.
     *  Applies to: Wireless Export SDK
     */
	public static final String GRAPHICBUFFERSIZE = "graphicbuffersize";

	/** Minimum size (in pixels) for exported graphics.
	 *  Smaller graphics will not be exported.
     *  Applies to: Wireless Export SDK.
	 */
	public static final String GRAPHICSKIPSIZE = "graphicskipsize";

	/** Maximum URL length; 0 indicates default length.
     *  Applies to: Wireless Export SDK.
     */
	public static final String MAXURLLENGTH	= "maxurllength";

	/** Determines whether additional formatting features will be added.
     *  Applies to: HTML and Wireless Export SDKs.
     */
	public static final String NOSOURCEFORMATTING	= "nosourceformatting";

	/** Maximum size for the text buffer.
	 *  If this is 0, the entire document will be in one file.
     *  Applies to: Wireless Export SDK.
     */
	public static final String TEXTBUFFERSIZE = "textbuffersize";

	/** Generate interlaced GIF files.
     *  Applies to: HTML, Image and Wireless Export SDKs.
     */
	public static final String GIFINTERLACE = "gifinterlace";

	/** Maximum height of exported graphic files.
	 *  Set to 0 for no limit.
     *  Applies to: HTML, Image and Wireless Export SDKs.
     */
	public static final String GRAPHICHEIGHTLIMIT = "graphicheightlimit";

	/** Maximum width of exported graphic files.
	 *  Set to 0 for no limit.
     *  Applies to: HTML, Image and Wireless Export SDKs.
     */
	public static final String GRAPHICWIDTHLIMIT = "graphicwidthlimit";

	/** Specifiy height of exported graphic files in pixels.
     *  Applies to: Image Export SDKs.
     */
	public static final String GRAPHICHEIGHT = "graphicheight";

	/** Specifiy width of exported graphic files in pixels.
     *  Applies to: Image Export SDK.
     */
	public static final String GRAPHICWIDTH = "graphicwidth";

	/** Maximum size (in pixels) of exported graphics.
	 *  Set to 0 for no limit.
	 *  If set, this overrides the graphic height and width limit options.
     *  Applies to: HTML, Image and Wireless Export SDKs.
     */
	public static final String GRAPHICSIZELIMIT = "graphicsizelimit";

	/** Graphic output resolution (0-2400 DPI).
	 *  If set to 0, the original resolution is used.
	 *  Default is 96 DPI.
     *  Applies to: HTML, Image and Wireless Export SDKs.
     */
	public static final String GRAPHICOUTPUTDPI = "graphicoutputdpi";

	/** JPEG quality (1-100)
	 *  Set to 1 for lowest quality/highest compression.
	 *  Set to 100 for highest quality/lowest compression.
     *  Applies to: HTML, Image and Wireless Export SDKs.
     */
	public static final String JPEGQUALITY = "jpegquality";

	/** TIFF colorspace setting.
	 *  Determines the number of bits to use per pixel.
     *  Applies to: Image Export SDK.
     */
	public static final String TIFFCOLORSPACE = "tiffcolorspace";

	/**  24 Bit RGB (true color). */
	public static final String TIFFCOLORSPACE_24BIT = "24 Bit RGB";
	/**  8 Bit palette. */
	public static final String TIFFCOLORSPACE_8BITPALETTE = "8 Bit Palette";
	/**  Black and White (1 bit per pixel). */
	public static final String TIFFCOLORSPACE_BW = "Black and White";

    /** TIFF compression setting.
	 *  Set the compression scheme of the TIFF image.
	 *  Applies to: Image Export SDK.
	 */

	public static final String TIFFCOMPRESSION = "tiffcompression";
	/** No compression. */
	public static final String TIFFCOMPRESSION_NONE = "None";
	/** Packbits. */
	public static final String TIFFCOMPRESSION_PACKBITS = "Packbits";
	/** LZW compression. */
	public static final String TIFFCOMPRESSION_LZW = "LZW Compression";
	/** CCITT 1D (used for Black and White images only). */
	public static final String TIFFCOMPRESSION_CCITT_1D = "CCITT - 1D";
	/** CCITT Group 3 Fax (used for Black and White images only). */
	public static final String TIFFCOMPRESSION_CCITTGRP3 = "CCITT - Group 3 Fax";
	/** CCITT Group 4 Fax (used for Black and White images only). */
	public static final String TIFFCOMPRESSION_CCITTGRP4 = "CCITT - Group 4 Fax";

    /** TIFF file byte order.
	 *  Applies to: Image Export SDK.
	 */
	public static final String TIFFBYTEORDER = "tiffbyteorder";
	/** Little Endian (Intel). */
	public static final String TIFFBYTEORDER_LITTLEENDIAN = "Little Endian";
	/*** Big Endian (Motorola). */
	public static final String TIFFBYTEORDER_BIGENDIAN = "Big Endian";

    /** TIFF multipage file.
	 *  Sets output to multiple TIFF files or single multipage TIFF.
	 *  Applies to: Image Export SDK.
	 */
	public static final String TIFFMULTIPAGE = "tiffmultipage";

    /** TIFF Fillorder.
	 *  Sets the bits of each byte to flipped or nonflipped.
	 *  Applies to: Image Export SDK.
	 */
	public static final String TIFFFILLORDER = "tifffillorder";
	/** Fillorder1 the bits of each byte are not flipped. */
	public static final String TIFFFILLORDER_FILLORDER1 = "fillorder1";
	/** Fillorder2 the bits of each byte are flipped, i.e least significant bit becomes most significant bit. */
	public static final String TIFFFILLORDER_FILLORDER2 = "fillorder2";

	/** Print range.
	 *  Specifies what if all or range of pages are to be exported.
	 *  Applies to: Image Export SDK.
	 */
	public static final String WHATTOEXPORT = "whattoexport";
	/** Export all pages. */
	public static final String WHATTOEXPORT_ALL = "all";
	/** Export range of pages. */
	public static final String WHATTOEXPORT_RANGE = "range";

	/** Print start page.
	 *  Set to the start of the pages to be printed.
	 *  Applies to: Image Export SDK.
	 */
	public static final String EXPORTSTARTPAGE = "exportstartpage";

	/** Print end page.
	 *  Set to the last page to be printed.
	 *  Applies to: Image Export SDK.
	 */
	public static final String EXPORTENDPAGE = "exportendpage";

	/** Print spreadsheet direction.
	 *  Determines what direction to print.
	 *  Applies to: Image Export SDK.
	 */
	public static final String SSDIRECTION = "ssdirection";
	/** Print across then down. */
	public static final String SSDIRECTION_ACROSSDOWN = "Across and Down";
	/** Print down then across. */
	public static final String SSDIRECTION_DOWNACROSS = "Down and Across";

	/** Print spreadsheet gridlines.
	 *  Determines whether spreadsheet gridlines should be printed.
	 *  Applies to: Image Export SDK.
	 */
	public static final String SSSHOWGRIDLINES = "ssshowgridlines";

	/** Print spreadsheet headings.
	 *  Determines whether spreadsheet headings should be printed.
	 *  Applies to: Image Export SDK.
	 */
	public static final String SSSHOWHEADINGS = "ssshowheadings";

	/** Print scale percent.
	 *  Determines the percent image should be scaled 0-100.
	 *  Applies to: Image Export SDK.
	 */
	public static final String SSSCALEPERCENT = "ssscalepercent";

	/** Print number of pages horizontally.
	 *  Set scaleX pages 0 - 100.
	 *  Applies to: Image Export SDK.
	 */
	public static final String SSSCALEXWIDE = "ssscalexwide";

	/** Print number of pages vertically.
	 *  Set scaleX for vertical pages 0 - 100.
	 *  Applies to: Image Export SDK.
	 */
	public static final String SSSCALEXHIGH = "ssscalexhigh";

	/** Print database gridlines.
	 *  Determines whether database gridlines should be printed.
	 *  Applies to: Image Export SDK.
	 */
	public static final String DBSHOWGRIDLINES = "dbshowgridlines";

	/** Print database headings.
	 *  Determines whether database headings should be printed.
	 *  Applies to: Image Export SDK.
	 */
	public static final String DBSHOWHEADINGS = "dbshowheadings";

	/** Print scaling setting spreadsheet.
	 *  Determines the type of scaling to be done on image.
	 *  Applies to: Image Export SDK.
	 */
	public static final String SSFITTOPAGE = "ssfittopage";
	/** No scaling. */
	public static final String SSFITTOPAGE_NOSCALE = "No Scaling";
	/** Fit to page. */
	public static final String SSFITTOPAGE_FITPAGE = "Fit to Page";
	/** Fit to width. */
	public static final String SSFITTOPAGE_FITWIDTH = "Fit to Width";
	/** Fit to height. */
	public static final String SSFITTOPAGE_FITHEIGHT = "Fit to Height";
	/** Scale to percent. */
	public static final String SSFITTOPAGE_SCALEPERCENT = "Scale to Percent";
	/** Scale to pages specified. */
	public static final String SSFITTOPAGE_SCALEPAGES = "Scale to Pages Specified";

	/** Print scaling setting database.
	 *  Determines the type of scaling to be done on image.
	 *  Applies to: Image Export SDK.
	 */
	public static final String DBFITTOPAGE = "dbfittopage";
	/** No scaling. */
	public static final String DBFITTOPAGE_NOSCALE = "No Scaling";
	/** Fit to page. */
	public static final String DBFITTOPAGE_FITPAGE = "Fit to Page";
	/** Fit to width. */
	public static final String DBFITTOPAGE_FITWIDTH = "Fit to Width";
	/** Fit to height. */
	public static final String DBFITTOPAGE_FITHEIGHT = "Fit to Height";

	/** Print margin values.
	 *  Set the page margin values in twips (1440 twips = 1 inch).
	 *  Applies to: Image Export SDK.
	 */
	/** Default Top Margin in twips. */
	public static final String DEFAULTMARGINTOP = "defaultmargintop";
	/** Default Bottom Margin in twips. */
	public static final String DEFAULTMARGINBOTTOM = "defaultmarginbottom";
	/** Default Left Margin in twips. */
	public static final String DEFAULTMARGINLEFT = "defaultmarginleft";
	/** Default Right Margin in twips. */
	public static final String DEFAULTMARGINRIGHT = "defaultmarginright";

	/** Document Page Settings.
	 *  Determines if document page setting should be used.
	 *  Applies to: Image Export SDK.
	 */
	public static final String USEDOCPAGESETTINGS = "usedocpagesettings";



    /** Output character set
     *  Applies to: HTML and Wireless Export SDKs.
     */
	public static final String CHARSET = "charset";
	/** Latin-1 */
	public static final String CHARSET_ISO8859_1 = "iso8859-1";
	/** Latin-2 */
	public static final String CHARSET_ISO8859_2 = "iso8859-2";
	/** Latin-3 */
	public static final String CHARSET_ISO8859_3 = "iso8859-3";
	/** Latin-4 */
	public static final String CHARSET_ISO8859_4 = "iso8859-4";
	/** Cyrillic */
	public static final String CHARSET_ISO8859_5 = "iso8859-5";
	/** Arabic */
	public static final String CHARSET_ISO8859_6 = "iso8859-6";
	/** Greek */
	public static final String CHARSET_ISO8859_7 = "iso8859-7";
	/** Hebrew */
	public static final String CHARSET_ISO8859_8 = "iso8859-8";
	/** Turkish */
	public static final String CHARSET_ISO8859_9 = "iso8859-9";
	/** Mac Roman */
	public static final String CHARSET_MACROMAN = "macroman";
	/** Mac CE */
	public static final String CHARSET_MACLATIN2 = "maclatin2";
	/** Mac Greek */
	public static final String CHARSET_MACGREEK = "macgreek";
	/** Mac Cyrillic */
	public static final String CHARSET_MACCYRILLIC = "maccyrillic";
	/** Mac Turkish */
	public static final String CHARSET_MACROMANTURKISH = "macromanturkish";
	/** Simplified Chinese */
	public static final String CHARSET_GB2312 = "gb2312";
	/** Traditional Chinese */
	public static final String CHARSET_BIG5 = "big5";
	/** Japanese */
	public static final String CHARSET_SHIFTJIS = "shoftjis";
	/** Japanese EUC */
	public static final String CHARSET_EUCJP = "eucjp";
	/** JIS (Japanese) */
	public static final String CHARSET_ISO2022_JP = "iso2022-jp";
	/** Russian */
	public static final String CHARSET_KOI8R = "koi8r";
	/** Windows Eastern Europe */
	public static final String CHARSET_WINDOWS1250 = "windows1250";
	/** Windows Cyrillic */
	public static final String CHARSET_WINDOWS1251 = "windows1251";
	/** Windows Latin-1 */
	public static final String CHARSET_WINDOWS1252 = "windows1252";
	/** Windows Greek */
	public static final String CHARSET_WINDOWS1253 = "windows1253";
	/** Windows Turkish */
	public static final String CHARSET_WINDOWS1254 = "windows1254";
	/** Windows Hebrew */
	public static final String CHARSET_WINDOWS1255 = "windows1255";
	/** Windows Arabic */
	public static final String CHARSET_WINDOWS1256 = "windows1256";
	/** Windows Baltic */
	public static final String CHARSET_WINDOWS1257 = "windows1257";
	/** Thai */
	public static final String CHARSET_THAI874 = "thai874";
	/** Korean Hangul */
	public static final String CHARSET_KOREANHANGUL = "koreanhangul";
	/** UTF-8 Unicode */
	public static final String CHARSET_UTF8 = "utf8";
	/** Unicode */
	public static final String CHARSET_UNICODE = "unicode";

    /** Fallback file format used when a file can not be identified.
     *  Applies to: HTML, Image and Wireless Export SDKs.
     */
	public static final String FALLBACKFORMAT = "fallbackformat";
	/** ANSI 7-bit text */
	public static final String FALLBACKFORMAT_ANSI = "fi_ansi";
	/** ANSI 8-bit text */
	public static final String FALLBACKFORMAT_ANSI8 = "fi_ansi8";
	/** ASCII 7-bit text */
	public static final String FALLBACKFORMAT_ASCII = "fi_ascii";
	/** ASCII 8-bit text */
	public static final String FALLBACKFORMAT_ASCII8 = "fi_ascii8";
	/** ANSI 1250 Central European text */
	public static final String FALLBACKFORMAT_CENTRALEU_1250 = "fi_centraleu_1250";
	/** ANSI 1251 Cyrillic text */
	public static final String FALLBACKFORMAT_CYRILLIC1251 = "fi_cyrillic1251";
	/** KOI8-R Cyrillic text */
	public static final String FALLBACKFORMAT_CYRILLICKOI8 = "fi_cyrillickoi8";
	/** Central European Latin-2 text */
	public static final String FALLBACKFORMAT_LATIN2 = "fi_latin2";
	/** Macintosh 7-bit text */
	public static final String FALLBACKFORMAT_MAC = "fi_mac";
	/** Macintosh 8-bit text */
	public static final String FALLBACKFORMAT_MAC8 = "fi_mac8";
	/** Japanese EUC text */
	public static final String FALLBACKFORMAT_JAPANESE_EUC = "fi_japanese_euc";
	/** Japanese JIS text */
	public static final String FALLBACKFORMAT_JAPANESE_JIS = "fi_japanese_jis";
	/** Unicode text */
	public static final String FALLBACKFORMAT_UNICODE = "fi_unicode";
	/** Japanese ShiftJIS text */
	public static final String FALLBACKFORMAT_SHIFTJIS = "fi_shiftjis";
	/** Chinese GB text */
	public static final String FALLBACKFORMAT_CHINESEGB = "fi_chinesegb";
	/** Chinese Big 5 text */
	public static final String FALLBACKFORMAT_CHINESEBIG5 = "fi_chinesebig5";
	/** Korean Hangeul text */
	public static final String FALLBACKFORMAT_HANGEUL = "fi_hangeul";
        /** Hebrew old code text */
        public static final String FALLBACKFORMAT_HEBREW_OLDCODE = "fi_hebrew_oldcode";
        /** Hebrew window text */
        public static final String FALLBACKFORMAT_HEBREW_WINDOWS = "fi_hebrew_windows";
        /** Hebrew Arabic window text */
        public static final String FALLBACKFORMAT_ARABIC_WINDOWS = "fi_arabic_windows";
	/** No fallback file format.
	 *  An "unsupported format" error will be returned.
	 */
	public static final String FALLBACKFORMAT_NONE = "fi_none";

    /** Graphic sizing algorithm.
     *  Applies to: HTML, Image and Wireless Export SDKs.
     */
	public static final String GRAPHICSIZEMETHOD = "graphicsizemethod";
	/** Use faster, lower quality scaling. */
	public static final String GRAPHICSIZEMETHOD_QUICK = "quick";
	/** Use slower, higher quality scaling. */
	public static final String GRAPHICSIZEMETHOD_SMOOTH = "smooth";
	/** Use smooth sizing on grayscale graphics and quick sizing on color graphics. */
	public static final String GRAPHICSIZEMETHOD_GRAYSCALE = "grayscale";

    /** Directory for temporary files.
     *  Applies to: HTML, Image, Wireless and XML Export SDKs.
     */
	public static final String TEMPDIR = "tempdir";
	public static final String TEMPDIR_U = "tempdir_u";

	/** Specifies the character to used when an input character cannot be mapped
	 *  to the output character set.
     *  Applies to: HTML, Image and Wireless Export SDKs.
	 */
	public static final String UNMAPPABLECHAR = "unmappablechar";

	public static final String HREFPREFIX = "hrefprefix";
	public static final String FIRSTPREVHREF = "firstprevhref";

    /** Strict DTD compliance.
     *  Applies to: HTML Export SDK.
     */
	public static final String STRICTDTD = "strictdtd";

	/** Fallback font for CSS-output HTML flavors.
     *  Applies to: HTML Export SDK.
     */
	public static final String FALLBACKFONT = "fallbackfont";

	/** If set, invalid characters in style names are omitted rather than being converted
	 *  to 4-digit hex values. Setting this option may result in non-unique style names.
     *  Applies to: HTML Export SDK.
     */
	public static final String SIMPLESTYLENAMES = "simplestylenames";

	public static final String HANDLENEWFILEINFO = "handlenewfileinfo";
	public static final String OEM = "oem";
	public static final String OEMOUTPUT = "oemoutput";
	public static final String LINKLOCATION = "linklocation";
	public static final String CUSTOMELEMENT = "customelement";
	public static final String CUSTOMELEMENTVALUE = "customelementvalue";
	public static final String PROCESSELEMENT = "processelement";

	/** Set to generate well-formed, but not necessarily strictly DTD-compliant, output.
     *  Applies to: HTML Export SDK.
     */
	public static final String WELLFORMED = "wellformed";

	/** Set to use JavaScript for tab support.
     *  Applies to: HTML Export SDK.
     */
	public static final String JAVASCRIPTTABS = "javascripttabs";

	/** Maximum number of source document characters per output document.
	 *  Set to 0 for no limit.
     *  Applies to: HTML and Wireless Export SDKs.
     */
	public static final String PAGESIZE = "pagesize";

	public static final String CMCALLBACKVERSION = "cmcallbackversion";
	public static final String CMCALLBACKCHARSET = "cmcallbackcharset";
	public static final String ALTLINKPREV = "altlinkprev";
	public static final String ALTLINKNEXT = "altlinknext";
	public static final String CBALLDISABLED = "cballdisabled";
	public static final String CBCREATENEWFILE = "cbcreatenewfile";
	public static final String CBNEWFILEINFO = "cbnewfileinfo";
	public static final String CBPROCESSLINK = "cbprocesslink";
	public static final String CBCUSTOMELEMENT = "cbcustomelement";
	public static final String CBCBGRAPHICEXPORTFAILURE = "cbgraphicexportfailure";
	public static final String CBOEMOUTPUT = "cboemoutput";
	public static final String CBALTLINK = "cbaltlink";
	public static final String CBALLENABLED = "cballenabled";

	/** If set, prevents graphics from overlapping due to (rare) bugs found in many
	 *  browsers. Turning this on places &lt;div&gt; tags around all &lt;img&gt; tags
	 *  written by Export, forcing each graphic to be on its own line in the browser
	 *  window.
     *  Applies to: HTML Export SDK.
     */
	public static final String PREVENTGRAPHICOVERLAP = "preventgraphicoverlap";

    /** File identification flags
     *  Applies to: HTML, Image, Wireless and XML Export SDKs.
     */
	public static final String FIFLAGS = "fiflags";
	/** Do NOT attempt to identify ASCII text files. */
	public static final String FIFLAGS_NORMAL = "fiflags_normal";
	/** Run an extended test on un-identified input files to determine if they
	 *  are 7-bit ASCII text files.
	 */
	public static final String FIFLAGS_EXTENDEDTEST = "fiflags_extendedtest";

    /** Set to suppress the size attribute when writing &lt;font&gt; tags.
     *  Also suppresses &lt;big&gt; and &lt;small&gt; markup tags.
     *  Applies to: HTML and Wireless Export SDKs.
     */
	public static final String SUPPRESSFONTSIZE = "suppressfontsize";

	/** Set to suppress the color attribute when writing &lt;font&gt; tags.
     *  Applies to: HTML and Wireless Export SDKs.
     */
	public static final String SUPPRESSFONTCOLOR = "suppressfontcolor";

	/** Set to suppress the face attribute when writing &lt;font&gt; tags.
     *  Applies to: HTML and Wireless Export SDKs.
     */
	public static final String SUPPRESSFONTFACE = "suppressfontface";

	public static final String LINKACTION = "linkaction";
	public static final String LINKACTION_CONVERT = "convert";
	public static final String LINKACTION_CREATE = "create";
	public static final String LINKACTION_SKIP = "skip";

    /** Number of rows in the grid.
     *  Set to 0 to use the number of rows in the spreadsheet.
     *  Applies to: HTML and Wireless Export SDK.
     */
	public static final String GRIDROWS = "gridrows";

	/** Number of columns in the grid.
	 *  Set to 0 to use the number of columns in the spreadsheet.
     *  Applies to: HTML and Wireless Export SDK.
	 */
	public static final String GRIDCOLS = "gridcols";

	/** Grid advance direction when using "previous" and "next".
     *  Applies to: HTML and Wireless Export SDK.
     */
	public static final String GRIDADVANCE = "gridadvance";
	/** Horizontal grid traversal */
	public static final String GRIDADVANCE_ACROSS = "0";
	/** Vertical grid traversal */
	public static final String GRIDADVANCE_DOWN = "1";

	/** Set if grid output should continue when the edge of the spreadsheet
	 *  is reached.
     *  Applies to: HTML and Wireless Export SDK.
	 */
	public static final String GRIDWRAP = "gridwrap";

    /** XML definition method.
     *  Applies to: XML Export SDK.
     */
	public static final String XMLDEFMETHOD = "xmldefmethod";
	/** Use a Document Type Definition. */
	public static final String XMLDEFMETHOD_DTD = "xmldefmethod_dtd";
	/** Use an Extensible Schema Definition. */
	public static final String XMLDEFMETHOD_XSD = "xmldefmethod_xsd";

	/** URL of the XML definition (DTD or XSD).
     *  Applies to: XML Export SDK.
     */
	public static final String XMLDEFREFERENCE = "xmldefreference";

	/** Set to include paragraph style name reference to &lt;p&gt; tags.
     *  Applies to: XML Export SDK.
     */
	public static final String PSTYLENAMESFLAG = "pstylenamesflag";

	/** Set to include embeddings.
     *  Applies to: XML Export SDK.
     */
	public static final String EMBEDDINGSFLAG = "embeddingsflag";

	/** Set to exclude the XML declaration.
     *  Applies to: XML Export SDK
     */
	public static final String NOXMLDECLARATIONFLAG = "noxmldeclarationflag";

	/** Include character offset information.  Offset can be used to map to
		* a viewer position and an link to a position in PageML XML output
	 * */
	public static final String OFFSETTRACKED = "offsettracked";

	/** Include bold character attribute information
		*/
	public static final String BOLD = "bold";

	/** Include italic character attribute information
		*/
	public static final String ITALIC = "italic";

	/** Include underline character attribute information
		*/
	public static final String UNDERLINE = "underline";

	/** Include double underline character attribute information
		*/
	public static final String DOULBEUNDERLINE = "doubleunderline";

	/** Include outline character attribute information
		*/
	public static final String OUTLINE = "outline";

	/** Include strikeout character attribute information
		*/
	public static final String STRIKEOUT = "strikeout";

	/** Include smallcaps character attribute information
		*/
	public static final String SMALLCAPS = "smallcaps";

	/** Include allcaps character attribute information
		*/
	public static final String ALLCAPS = "allcaps";

	/** Include hidden character attribute information
		*/
	public static final String HIDDEN = "hidden";

	/** Include line spacing character attribute information
		*/
	public static final String LINESPACING = "linespacing";

	/** Include line height character attribute information
		*/
	public static final String LINEHEIGHT = "lineheight";

	/** Include left indent character attribute information
		*/
	public static final String LEFTINDENT = "leftindent";

	/** Include right indent character attribute information
		*/
	public static final String RIGHTINDENT = "rightindent";

	/** Include first indent character attribute information
		*/
	public static final String FIRSTINDENT = "firstindent";

	/** Specifies the name of the printer whose metrics should be used to calculate
		* page information when outputing PageML XML.  If unspecifed the default printer
		* will be used. The screen metrics of the system will be used if a printer is not
		* specified and a default printer does not exist.  As the metrics between printers
		* and the screen such as which fonts are installed will vary, The PAGEML XML output
		* can also change/vary between different systems and configurations.
		*/
	public static final String PRINTERNAME = "printername";

	/** Default print font face 
     *  Applies to: PageML XML and Image Export SDK.
	 */
        public static final String DEFAULTPRINTFONTFACE = "defaultprintfontface";

	/** Default print font height
     *  Applies to: PageML XML and Image Export SDK.
	 */
        public static final String DEFAULTPRINTFONTHEIGHT = "defaultprintfontheight";

	/** Default print font attribute
     *  Applies to: PageML XML and Image Export SDK.
	 */
        public static final String DEFAULTPRINTFONTATTRIBUTE = "defaultprintfontattribute";

	/** Default print font type
     *  Applies to: PageML XML and Image Export SDK.
	 */
        public static final String DEFAULTPRINTFONTTYPE = "defaultprintfonttype";

	/** Print font alias ID 
     *  Applies to: PageML XML and Image Export SDK.
	 */
        public static final String PRINTFONTALIASID = "printfontaliasid";

	/** Print font alias flag
     *  Applies to: PageML XML and Image Export SDK.
	 */
        public static final String PRINTFONTALIASFLAG = "printfontaliasflag";

	/** Print font alias original
     *  Applies to: PageML XML and Image Export SDK.
	 */
        public static final String PRINTFONTALIASORIGINAL = "printfontaliasoriginal";

	/** Print font alias
     *  Applies to: PageML XML and Image Export SDK.
	 */
        public static final String PRINTFONTALIAS = "printfontalias";

	/** Jpeg graphics compression
     *  Applies to: PageML XML and Image Export SDK.
	 */
        public static final String JPEGCOMPRESSION = "jpegcompression";

	/** LZW compression
     *  Applies to: PageML XML and Image Export SDK.
	 */
        public static final String LZWCOMPRESSION = "lzwcompression";
		    
        /** Show hidden spread sheet columns/rows/sheets */
        public static final String SHOWHIDDENSSDTA = "showhiddenssdata";
        
        /** Show change tracking of the word processing document */
        public static final String SHOWCHANGETRACKING = "showchangetracking";
        
        /** Show spread sheet border */
        public static final String SHOWSPREADSHEETBORDER = "showspreadsheetborder"; 


}
