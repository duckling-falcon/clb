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
package cn.vlabs.clb.api.document;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ChunkResponse {

    public static final int FOUND_THE_SAME_CONTENT = 100;
    public static final int SUCCESS = 200;
    public static final int DUPLICATE_CHUNK = 300;
    public static final int TOO_LARGE_CHUNK = 350;
    public static final int EXCEED_MAX_CHUNK_SIZE = 360;
    public static final int NOT_FOUND_CHUNK_META = 400;
    public static final int CHUNK_INDEX_INVALID = 403;
    public static final int NEED_MORE_CHUNK = 500;

    public static final String PREPARE_PHASE = "prepare";
    public static final String EXECUTE_PHASE = "execute";
    public static final String FINISH_PHASE = "finish";

    // private String message;
    private Set<Integer> emptyChunkSet;
    private int chunkIndex;
    private int docid;
    private int statusCode;
    private String phase;
    private Map<String, Object> opts = new HashMap<String, Object>();
    private int chunkSize;

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public Map<String, Object> getOpts() {
        return opts;
    }

    public void setOpts(Map<String, Object> opts) {
        this.opts = opts;
    }

    public Set<Integer> getEmptyChunkSet() {
        return emptyChunkSet;
    }

    public void setEmptyChunkSet(Set<Integer> emptyChunkSet) {
        this.emptyChunkSet = emptyChunkSet;
    }

    public int getChunkIndex() {
        return chunkIndex;
    }

    public void setChunkIndex(int chunkIndex) {
        this.chunkIndex = chunkIndex;
    }

    public int getDocid() {
        return docid;
    }

    public void setDocid(int docid) {
        this.docid = docid;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public boolean isSccuessStatus() {
        return statusCode == SUCCESS;
    }

    public boolean isDuplicateChunk() {
        return statusCode == DUPLICATE_CHUNK;
    }
    
    public boolean isExistSameFileContent(){
        return statusCode == FOUND_THE_SAME_CONTENT;
    }

    public String getStatusMessage() {
        switch (phase) {
        case PREPARE_PHASE:
            switch (statusCode) {
            case SUCCESS:
                return String.format("Prepare chunk upload success.");
            case EXCEED_MAX_CHUNK_SIZE:
                return String.format("You cannot assign a chunkSize=%d larger than 16MB.", this.getChunkSize());
            case FOUND_THE_SAME_CONTENT:
                return String.format("The database contains the same file content.");
            }
        case EXECUTE_PHASE:
            switch (statusCode) {
            case SUCCESS:
                return String.format("Write chunk %d success for docid=%d.", chunkIndex, docid);
            case DUPLICATE_CHUNK:
                return String.format("Duplicate chunk %d in docid %d, current chunk data will be ignored.", chunkIndex,
                        docid);
            case TOO_LARGE_CHUNK:
                return String.format(
                        "Cannot upload a chunk data, because actual chunkSize=%d is greater than chunkSize=%d. ",
                        opts.get("currentChunkSize"), this.chunkSize);
            case NOT_FOUND_CHUNK_META:
                return String.format("Not found chunk status for docid=%d, please invoke prepareChunkUpload firstly.",
                        docid);
            case CHUNK_INDEX_INVALID:
                return String.format("Document[%d] can only recieve chunk index from 0 to %d, but client want to write chunk[%d]",
                        docid, opts.get("numOfChunk"), chunkIndex);
            }
        case FINISH_PHASE:
            switch (statusCode) {
            case SUCCESS:
                return String.format("Finish chunk upload success for docid=%d.", docid);
            case NEED_MORE_CHUNK:
                return String.format("Cannot finish chunk upload for docid=%d, becase these chunks %s are missing.",
                        docid, this.getEmptyChunkSet().toString());
            }
        }
        return null;
    }
}
