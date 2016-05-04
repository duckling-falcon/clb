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

import java.util.ListResourceBundle;

/**
 * This is a data container class for use by the Java <code>ResourceBundle</code> system.  It provides a mapping
 * between integer conversion return codes and a text description for that code. For more information on the
 * <code>ResourceBundle</code> system, see
 * <a href=http://www.java.sun.com/j2se/1.3/docs/api/java/util/ResourceBundle.html>java.util.ResourceBundle</a>
 *
 * @author John Pazdernik
 * @version 1.00
 * @see <a href=http://www.java.sun.com/j2se/1.3/docs/api/java/util/ResourceBundle.html>java.util.ResourceBundle</a>
 * @see <a href=http://www.java.sun.com/j2se/1.3/docs/api/java/util/ListResourceBundle.html>java.util.ListResourceBundle</a>
 */
public class ExportResources extends ListResourceBundle {

	static final Object[][] contents = {
		{ Integer.toString( ExportStatusCode.SCCERR_OK.getCode() ), "no error" },
		{ Integer.toString( ExportStatusCode.SCCERR_UNKNOWN.getCode() ), "unknown error" },
		{ Integer.toString( ExportStatusCode.SCCERR_LEG_UNKNOWNFAILURE.getCode() ), "unknown error" },
		{ Integer.toString( ExportStatusCode.SCCERR_BADPARAM.getCode() ), "invalid parameter" },
		{ Integer.toString( ExportStatusCode.SCCERR_ABORT.getCode() ), "operation aborted" },
		{ Integer.toString( ExportStatusCode.SCCERR_CANCEL.getCode() ), "operation cancelled" },
		{ Integer.toString( ExportStatusCode.SCCERR_NOINIT.getCode() ), "module not initialized yet" },
		{ Integer.toString( ExportStatusCode.SCCERR_MACINITFAILED.getCode() ), "SCCVW_MACINIT failed" },
		{ Integer.toString( ExportStatusCode.SCCERR_MESSAGEHANDLED.getCode() ), "message handled" },
		{ Integer.toString( ExportStatusCode.SCCERR_NOITEM.getCode() ), "item does not exist" },
		{ Integer.toString( ExportStatusCode.SCCERR_NONEFOUND.getCode() ), "requested item not found" },
		{ Integer.toString( ExportStatusCode.SCCERR_MORE.getCode() ), "SCCERR_MORE" },
		{ Integer.toString( ExportStatusCode.SCCERR_NOTHANDLED.getCode() ), "callback function not handled or doesn't exist" },
		{ Integer.toString( ExportStatusCode.SCCERR_NODATATORENDER.getCode() ), "Rendering of this format is not supported" },
		{ Integer.toString( ExportStatusCode.SCCERR_SYSTEM.getCode() ), "operating system-generated error" },
		{ Integer.toString( ExportStatusCode.SCCERR_PROCCREATE.getCode() ), "process creation error" },
		{ Integer.toString( ExportStatusCode.SCCERR_PROCDESTROY.getCode() ), "process destruction error" },
		{ Integer.toString( ExportStatusCode.SCCERR_PROCTIMEOUT.getCode() ), "process run-time allotment exceeded" },
		{ Integer.toString( ExportStatusCode.SCCERR_ASSERTIONFAILED.getCode() ), "assertion failure" },
		{ Integer.toString( ExportStatusCode.SCCERR_DEBUGERROR.getCode() ), "debug error" },
		{ Integer.toString( ExportStatusCode.SCCERR_NOCHANGE.getCode() ), "no changes were made" },
		{ Integer.toString( ExportStatusCode.SCCERR_DISPLAYOPENFAILED.getCode() ), "XOpenDisplay failed" },
		{ Integer.toString( ExportStatusCode.SCCERR_WRONGDATAFORMAT.getCode() ), "unknown error" },
		{ Integer.toString( ExportStatusCode.SCCERR_UNKNOWNERRORCODE.getCode() ), "Unknown error: check for proper installation of OIT SDK modules" },
		{ Integer.toString( ExportStatusCode.SCCERR_ALLOCFAILED.getCode() ), "not enough memory for allocation" },
		{ Integer.toString( ExportStatusCode.SCCERR_OUTOFMEMORY.getCode() ), "out of memory" },
		{ Integer.toString( ExportStatusCode.SCCERR_INSUFFICIENTBUFFER.getCode() ), "buffer size too small" },
		{ Integer.toString( ExportStatusCode.SCCERR_MEMORYLEAK.getCode() ), "memory leak detected" },
		{ Integer.toString( ExportStatusCode.SCCERR_MEMTABLEFULL.getCode() ), "memory table full" },
		{ Integer.toString( ExportStatusCode.SCCERR_INVALIDHANDLE.getCode() ), "handle is invalid" },
		{ Integer.toString( ExportStatusCode.SCCERR_NULLHANDLE.getCode() ), "handle is NULL" },
		{ Integer.toString( ExportStatusCode.SCCERR_LOCKEDHANDLE.getCode() ), "handle is locked" },
		{ Integer.toString( ExportStatusCode.SCCERR_UNLOCKEDHANDLE.getCode() ), "handle is unlocked" },
		{ Integer.toString( ExportStatusCode.SCCERR_NOFILE.getCode() ), "no file is currently open" },
		{ Integer.toString( ExportStatusCode.SCCERR_FILEOPENFAILED.getCode() ), "file could not be opened" },
		{ Integer.toString( ExportStatusCode.SCCERR_SUPFILEOPENFAILED.getCode() ), "supplemental files could not be opened" },
		{ Integer.toString( ExportStatusCode.SCCERR_BADFILE.getCode() ), "file is corrupt" },
		{ Integer.toString( ExportStatusCode.SCCERR_EMPTYFILE.getCode() ), "file is empty" },
		{ Integer.toString( ExportStatusCode.SCCERR_PROTECTEDFILE.getCode() ), "file is password protected or encrypted" },
		{ Integer.toString( ExportStatusCode.SCCERR_FILECREATE.getCode() ), "file creation error" },
		{ Integer.toString( ExportStatusCode.SCCERR_FILECHANGED.getCode() ), "file has changed unexpectedly" },
		{ Integer.toString( ExportStatusCode.SCCERR_FILEWRITEFAILED.getCode() ), "file write error" },
		{ Integer.toString( ExportStatusCode.SCCERR_EOF.getCode() ), "unexpected EOF in file" },
		{ Integer.toString( ExportStatusCode.SCCERR_INVALIDFILEHANDLE.getCode() ), "file handle is invalid" },
		{ Integer.toString( ExportStatusCode.SCCERR_OPENINPUTFAILED.getCode() ), "open input failed" },
		{ Integer.toString( ExportStatusCode.SCCERR_FILECOPYFAILED.getCode() ), "file copy failed" },
		{ Integer.toString( ExportStatusCode.SCCERR_FILENOTAVAILABLE.getCode() ), "file not available" },
		{ Integer.toString( ExportStatusCode.SCCERR_RECORDDELETED.getCode() ), "requested record has been deleted" },
		{ Integer.toString( ExportStatusCode.SCCERR_INVALIDPATH.getCode() ), "invalid path" },
		{ Integer.toString( ExportStatusCode.SCCERR_INVALIDSPEC.getCode() ), "invalid spec" },
		{ Integer.toString( ExportStatusCode.SCCERR_INVALIDID.getCode() ), "invalid file ID" },
		{ Integer.toString( ExportStatusCode.SCCERR_UNSUPPORTEDFORMAT.getCode() ), "unsupported format" },
		{ Integer.toString( ExportStatusCode.SCCERR_NOFILTER.getCode() ), "no filter available for this file type" },
		{ Integer.toString( ExportStatusCode.SCCERR_FILTERLOADFAILED.getCode() ), "filter DLL for this file type could not be loaded" },
		{ Integer.toString( ExportStatusCode.SCCERR_FILTERALLOCFAILED.getCode() ), "filter DLL couldn't allocate enough memory" },
		{ Integer.toString( ExportStatusCode.SCCERR_FILTERTIMEOUT.getCode() ), "filter timed out" },
		{ Integer.toString( ExportStatusCode.SCCERR_FILTEREXCEPTACCESS.getCode() ), "exception in filter: access violation" },
		{ Integer.toString( ExportStatusCode.SCCERR_FILTEREXCEPTZERO.getCode() ), "exception in filter: divide by zero" },
		{ Integer.toString( ExportStatusCode.SCCERR_FILTEREXCEPTOTHER.getCode() ), "exception in filter" },
		{ Integer.toString( ExportStatusCode.SCCERR_FILTERSCRAMBLEERROR.getCode() ), "filter failed scramble test" },
		{ Integer.toString( ExportStatusCode.SCCERR_FILTERTILEMAXEXCEEDED.getCode() ), "bitmap tile size exceeded maximum" },
		{ Integer.toString( ExportStatusCode.SCCERR_NODISPLAYENGINE.getCode() ), "display engine not available for this file type" },
		{ Integer.toString( ExportStatusCode.SCCERR_DISPLAYINITFAILED.getCode() ), "display window initialization failed" },
		{ Integer.toString( ExportStatusCode.SCCERR_CHUNKERINITFAILED.getCode() ), "chunker (SCCCH.DLL) could not be initialized" },
		{ Integer.toString( ExportStatusCode.SCCERR_DATANOTAVAILABLE.getCode() ), "requested data not available" },
		{ Integer.toString( ExportStatusCode.SCCERR_VIEWERBAIL.getCode() ), "viewer bailed out" },
		{ Integer.toString( ExportStatusCode.SCCERR_CHARMAPFAILED.getCode() ), "character mapping routines failed" },
		{ Integer.toString( ExportStatusCode.SCCERR_LASTPAGE.getCode() ), "reached last page in the file" },
		{ Integer.toString( ExportStatusCode.SCCERR_NOPAGE.getCode() ), "requested page not available" },
		{ Integer.toString( ExportStatusCode.SCCERR_OTHERPRINTING.getCode() ), "another print process is already in progress" },
		{ Integer.toString( ExportStatusCode.SCCERR_FEATURENOTAVAIL.getCode() ), "feature not supported" },
		{ Integer.toString( ExportStatusCode.SCCERR_RAWTEXTDISABLED.getCode() ), "raw text disabled" },
		{ Integer.toString( ExportStatusCode.SCCERR_DATAREMOTEERROR.getCode() ), "RFA remote data error" },
		{ Integer.toString( ExportStatusCode.SCCERR_EXCEPTION.getCode() ), "exception occurred" },
		{ Integer.toString( ExportStatusCode.SCCERR_EXCEPT_ACCESS_VIOLATION.getCode() ), "access violation" },
		{ Integer.toString( ExportStatusCode.SCCERR_EXCEPT_BREAKPOINT.getCode() ), "breakpoint encountered" },
		{ Integer.toString( ExportStatusCode.SCCERR_EXCEPT_DATATYPE_MISALIGNMENT.getCode() ), "data misalignment" },
		{ Integer.toString( ExportStatusCode.SCCERR_EXCEPT_SINGLE_STEP.getCode() ), "trace trap single step indicated" },
		{ Integer.toString( ExportStatusCode.SCCERR_EXCEPT_ARRAY_BOUNDS_EXCEEDED.getCode() ), "out of bounds array element referenced" },
		{ Integer.toString( ExportStatusCode.SCCERR_EXCEPT_FLT_DENORMAL_OPERAND.getCode() ), "floating point value is denormal" },
		{ Integer.toString( ExportStatusCode.SCCERR_EXCEPT_FLT_DIVIDE_BY_ZERO.getCode() ), "floating point divide by zero" },
		{ Integer.toString( ExportStatusCode.SCCERR_EXCEPT_FLT_INEXACT_RESULT.getCode() ), "result cannot be represented as a decimal fraction" },
		{ Integer.toString( ExportStatusCode.SCCERR_EXCEPT_FLT_INVALID_OPERATION.getCode() ), "general floating point exception" },
		{ Integer.toString( ExportStatusCode.SCCERR_EXCEPT_FLT_OVERFLOW.getCode() ), "floating point exponent overflow" },
		{ Integer.toString( ExportStatusCode.SCCERR_EXCEPT_FLT_STACK_CHECK.getCode() ), "floating point stack underflow or overflow" },
		{ Integer.toString( ExportStatusCode.SCCERR_EXCEPT_FLT_UNDERFLOW.getCode() ), "floating point exponent underflow" },
		{ Integer.toString( ExportStatusCode.SCCERR_EXCEPT_INT_DIVIDE_BY_ZERO.getCode() ), "integer divide by zero" },
		{ Integer.toString( ExportStatusCode.SCCERR_EXCEPT_INT_OVERFLOW.getCode() ), "integer overflow" },
		{ Integer.toString( ExportStatusCode.SCCERR_EXCEPT_PRIV_INSTRUCTION.getCode() ), "privileged instruction" },
		{ Integer.toString( ExportStatusCode.SCCERR_EXCEPT_NONCONTINUABLE.getCode() ), "noncontinuable exception occurred" },
		{ Integer.toString( ExportStatusCode.SCCERR_EXCEPT_UNKNOWN.getCode() ), "unknown exception occurred" },
		{ Integer.toString( ExportStatusCode.SCCERR_TESTNOTAVAILABLE.getCode() ), "the requested test is not available" },
		{ Integer.toString( ExportStatusCode.SCCERR_TESTTIMEOUT.getCode() ), "the test timed out" },
		{ Integer.toString( ExportStatusCode.SCCERR_CREATEBINFAILED.getCode() ), "unable to create binary file" },
		{ Integer.toString( ExportStatusCode.SCCERR_OPENBINFAILED.getCode() ), "unable to open binary file" },
		{ Integer.toString( ExportStatusCode.SCCERR_WRITEBINFAILED.getCode() ), "unable to write to binary file" },
		{ Integer.toString( ExportStatusCode.SCCERR_READBINFAILED.getCode() ), "unable to read from binary file" },
		{ Integer.toString( ExportStatusCode.SCCERR_OPENDUMPFAILED.getCode() ), "unable to open dump file" },
		{ Integer.toString( ExportStatusCode.SCCERR_COMPAREFAILED.getCode() ), "chunker compare failed" },
		{ Integer.toString( ExportStatusCode.SCCERR_CHUNKERRUNMISMATCH.getCode() ), "chunkers had different run counts" },
		{ Integer.toString( ExportStatusCode.SCCERR_CHUNKERSIZEMISMATCH.getCode() ), "chunkers had different run sizes" },
		{ Integer.toString( ExportStatusCode.SCCERR_CHUNKERFAILED.getCode() ), "chunker failure" },
		{ Integer.toString( ExportStatusCode.SCCERR_CHUNKEROVERRUN.getCode() ), "chunker run count overflow" },
		{ Integer.toString( ExportStatusCode.SCCERR_COMPRESSIONFAILED.getCode() ), "general compression failure" },
		{ Integer.toString( ExportStatusCode.SCCERR_STREAMBAIL.getCode() ), "general stream bailout" },
		{ Integer.toString( ExportStatusCode.SCCERR_MISSINGELEMENT.getCode() ), "missing element" },
		{ Integer.toString( ExportStatusCode.SCCERR_TESTCOMPLETE.getCode() ), "test is complete" },
		{ Integer.toString( ExportStatusCode.SCCERR_TESTMODESWITCH.getCode() ), "SCCERR_TESTMODESWITCH" },
		{ Integer.toString( ExportStatusCode.SCCERR_BASELINEFILENOTFOUND.getCode() ), "a baseline file was not found for a compare file" },
		{ Integer.toString( ExportStatusCode.SCCERR_COMPAREFILENOTFOUND.getCode() ), "a compare file was not found for a baseline file" },
		{ Integer.toString( ExportStatusCode.SCCERR_TECHNOTAVAILABLE.getCode() ), "technology is not available" },
		{ Integer.toString( ExportStatusCode.SCCERR_TESTFILESEMPTY.getCode() ), "both the baseline and compare files are empty" },
		{ Integer.toString( ExportStatusCode.SCCERR_INVALIDRESPONSE.getCode() ), "received an unexpected response" },
		{ Integer.toString( ExportStatusCode.SCCERR_COMMTIMEOUT.getCode() ), "communications timeout" },
		{ Integer.toString( ExportStatusCode.SCCERR_COMMUNKNOWN.getCode() ), "general communications error" },
		{ Integer.toString( ExportStatusCode.SCCERR_CONNECTIONREFUSED.getCode() ), "connection refused" },
		{ Integer.toString( ExportStatusCode.SCCERR_COMMFAULT.getCode() ), "communications fault" },
		{ Integer.toString( ExportStatusCode.SCCERR_CONNECTIONDOWN.getCode() ), "host or network is down" },
		{ Integer.toString( ExportStatusCode.SCCERR_CONNECTIONUNREACHABLE.getCode() ), "host or network is unreachable" },
		{ Integer.toString( ExportStatusCode.SCCERR_DISCONNECTED.getCode() ), "unexpected disconnection" },
		{ Integer.toString( ExportStatusCode.SCCERR_SYNCHRONIZE.getCode() ), "synchronization error" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_FIRSTERROR.getCode() ), "general error" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_GENERAL.getCode() ), "general error" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_TREE.getCode() ), "template element tree error" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_INVALID_VAL.getCode() ), "invalid value" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_NONE.getCode() ), "no error" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_ENDMACRO.getCode() ), "end macro tag not found" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_ENDBARNEY.getCode() ), "end macro tag not found" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_START.getCode() ), " expected and not found" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_LT.getCode() ), " expected and not found" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_EQ.getCode() ), " expected and not found" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_END.getCode() ), " expected and not found" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_GT.getCode() ), " expected and not found" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_ACTION.getCode() ), "error parsing 'action =' statement" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_SECTION.getCode() ), "section not found" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_BARNEY.getCode() ), "section not found" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_ELEMENT.getCode() ), "error parsing 'element =' statement" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_LINK.getCode() ), "error parsing 'link =' statement" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_TAG.getCode() ), "error parsing 'tag =' statement" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_TEMPLATE.getCode() ), "error parsing 'template =' statement" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_TYPE.getCode() ), "error parsing 'type =' statement" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_TYPENAME.getCode() ), "error parsing 'name =' statement" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_DEFINE.getCode() ), "error parsing 'action = define' statement" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_DOCUMENT.getCode() ), "error parsing document" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_GENLINK.getCode() ), " link} statement" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_INSERT.getCode() ), "error parsing {## insert} statement" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_REPEAT.getCode() ), "error parsing {## repeat} statement" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_CHKTRAN.getCode() ), "unknown error" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_ALLOC.getCode() ), "error in allocating memory in MiscBuffer" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_INVALIDCMD.getCode() ), "invalid command" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_INVALIDERRNUM.getCode() ), "invalid error number" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_HANDLEELEM.getCode() ), "error in converting document element" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_ASISOUTTEXT.getCode() ), "error writing template text to output file" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_OUTTEXT.getCode() ), "error writing parser-created text to output file" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_NOTEMPLATE.getCode() ), "error opening template file" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_OUTFILEFAIL.getCode() ), "error opening output file" },
		{ Integer.toString( ExportStatusCode.SCCERR_NOENDREPEAT.getCode() ), "no {## /repeat} tag found" },
		{ Integer.toString( ExportStatusCode.SCCERR_NOENDIF.getCode() ), "no {## /if} tag found" },
		{ Integer.toString( ExportStatusCode.SCCERR_NOENDIGNORE.getCode() ), "no {## /ignore} tag found" },
		{ Integer.toString( ExportStatusCode.SCCERR_NONREPEAT.getCode() ), "non-repeatable element specified in {## repeat} statement" },
		{ Integer.toString( ExportStatusCode.SCCERR_REPEATENDONINDEX.getCode() ), "element specified in {## repeat} statement ended on the index" },
		{ Integer.toString( ExportStatusCode.SCCERR_IFENDONINDEX.getCode() ), "element specified in {## if} statement ended on the index" },
		{ Integer.toString( ExportStatusCode.SCCERR_INSERTENDONINDEX.getCode() ), "element specified in {## insert} statement ended on the index" },
		{ Integer.toString( ExportStatusCode.SCCERR_NOELEMENT.getCode() ), "no element was given" },
		{ Integer.toString( ExportStatusCode.SCCERR_INVALIDTAG.getCode() ), "unknown error" },
		{ Integer.toString( ExportStatusCode.SCCERR_NOSTARTTAG.getCode() ), "end macro tag with no corresponding start macro tag" },
		{ Integer.toString( ExportStatusCode.SCCERR_INVALIDTYPE.getCode() ), "type given not a valid document type" },
		{ Integer.toString( ExportStatusCode.SCCERR_NOVALUE.getCode() ), "no value given for sections.x.type" },
		{ Integer.toString( ExportStatusCode.SCCERR_NOCONDITION.getCode() ), "no condition given or missing '}'" },
		{ Integer.toString( ExportStatusCode.SCCERR_INVALIDLINKPARAM.getCode() ), "invalid parameter in {## link} statement" },
		{ Integer.toString( ExportStatusCode.SCCERR_NOLINKPARAM.getCode() ), "missing parameters in {## link} statement" },
		{ Integer.toString( ExportStatusCode.SCCERR_BADCOUNTORVALUE.getCode() ), "count or value element can only be followed by '}'" },
		{ Integer.toString( ExportStatusCode.SCCERR_INVALIDELSE.getCode() ), " else} not preceded by {## if}" },
		{ Integer.toString( ExportStatusCode.SCCERR_INVALIDCOND.getCode() ), "invalid condition in {## if} statement" },
		{ Integer.toString( ExportStatusCode.SCCERR_TEMPLATECREATE.getCode() ), "creation of the default template failed" },
		{ Integer.toString( ExportStatusCode.SCCERR_MAXELEMSPASSED.getCode() ), "maximum allowable number of elements exceeded" },
		{ Integer.toString( ExportStatusCode.SCCERR_NOTYPE.getCode() ), "no type was given for sections.x.type" },
		{ Integer.toString( ExportStatusCode.SCCERR_CUSTOMELEMENT.getCode() ), "the element is a custom element" },
		{ Integer.toString( ExportStatusCode.SCCERR_INVALIDELSEIF.getCode() ), " elseif} not preceded by {## if}" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_CSSPRAGMA.getCode() ), "pragma.cssfile required" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_ANCHOR.getCode() ), "invalid anchor value" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_LABEL.getCode() ), "label expected and not found" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_DECKSIZEEXCEEDED.getCode() ), "template-generated text exceeds the deck size" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_UNSUPPORTEDMACRO.getCode() ), "macro not supported in this output format" },
		{ Integer.toString( ExportStatusCode.SCCERR_INTERNALEXPORTFILTER.getCode() ), "internal export filter error" },
		{ Integer.toString( ExportStatusCode.SCCERR_TEMPLATEPARSE.getCode() ), "error parsing the template" },
		{ Integer.toString( ExportStatusCode.SCCERR_OUTPUTFILE.getCode() ), "error generating the output file" },
		{ Integer.toString( ExportStatusCode.SCCERR_TRUNCATIONLIMITREACHED.getCode() ), "non-fatal error:  we've reached the insertion truncation limit" },
		{ Integer.toString( ExportStatusCode.SCCERR_NONBREAKINGREPEAT.getCode() ), "{## repeat} statement found in unit header or footer" },
		{ Integer.toString( ExportStatusCode.SCCERR_NONBREAKINGTEMPLATE.getCode() ), "this template cannot be used with a nonzero deck size" },
		{ Integer.toString( ExportStatusCode.SCCERR_INVALIDATTRIBUTE.getCode() ), "invalid attribute" },
		{ Integer.toString( ExportStatusCode.SCCERR_INVALIDATTRIBVALUE.getCode() ), "invalid or missing attribute value" },
		{ Integer.toString( ExportStatusCode.SCCERR_MALFORMEDUNIT.getCode() ), "malformed unit - check for matching header and footer tags" },
		{ Integer.toString( ExportStatusCode.SCCERR_TEXTBUFFERTOOSMALL.getCode() ), "text buffer size is too small" },
		{ Integer.toString( ExportStatusCode.SCCERR_BUFFERTOOSMALLFORGRAPHICS.getCode() ), "buffer size is too small to export graphics" },
		{ Integer.toString( ExportStatusCode.SCCERR_BI_LASTERROR.getCode() ), "buffer size is too small to export graphics" },
		{ Integer.toString( ExportStatusCode.SCCERR_INTERNALEXMU.getCode() ), "internal export memory utility error" },
		{ Integer.toString( ExportStatusCode.SCCERR_INTERNALEXLM.getCode() ), "internal export list management error" },
		{ Integer.toString( ExportStatusCode.SCCERR_INTERNALPARA.getCode() ), "internal export para error" },
		{ Integer.toString( ExportStatusCode.SCCERR_INTERNALSTYLE.getCode() ), "internal export style error" },
		{ Integer.toString( ExportStatusCode.SCCERR_INTERNALTABLE.getCode() ), "internal export table error" },
		{ Integer.toString( ExportStatusCode.SCCERR_INTERNALTAGS.getCode() ), "internal export tags error" },
		{ Integer.toString( ExportStatusCode.SCCERR_INTERNALGRAPHICS.getCode() ), "internal export graphics error" },
		{ Integer.toString( ExportStatusCode.SCCERR_GRIDCELLLIMITREACHED.getCode() ), "non-fatal error:  we've reached the grid cell truncation limit" },
		{ Integer.toString( ExportStatusCode.SCCERR_STARTPAGEERROR.getCode() ), "the start page value is larger than the number of pages in the document" },
		{ Integer.toString( ExportStatusCode.SCCERR_ENDPAGEERROR.getCode() ), "the start page value is larger than the end page value" },
		{ Integer.toString( ExportStatusCode.SCCERR_JAVA_INTERRUPTED.getCode() ), "The process was interrupted from the Java side" },
		{ Integer.toString( ExportStatusCode.SCCERR_JAVA_IO_ERROR.getCode() ), "I/O error while communicating with native process" },
		{ Integer.toString( ExportStatusCode.SCCERR_JAVA_TIMEDOUT.getCode() ), "The export process timed out." },
		{ Integer.toString( ExportStatusCode.SCCERR_JAVA_SEGVIOLATION.getCode() ), "segmentation violation." },
		{ Integer.toString( ExportStatusCode.SCCERR_EMPTYCODE.getCode() ), "code 139 new status code undefined." }
		
	};

	/**
	 * See the class description in
	 * <a href=http://www.java.sun.com/j2se/1.3/docs/api/java/util/ListResourceBundle.html>java.util.ListResourceBundle</a>
	 */
	public ExportResources() {
		super();
	}

	/**
	 * See the class description in
	 * <a href=http://www.java.sun.com/j2se/1.3/docs/api/java/util/ListResourceBundle.html>java.util.ListResourceBundle</a>
	 *
	 * @return The object array holding the List resources.
	 */
	public Object[][] getContents() {
		return contents;
	}
}
