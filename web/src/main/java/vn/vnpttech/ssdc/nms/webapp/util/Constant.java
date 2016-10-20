/*
 * Copyright 2015 Pivotal Software, Inc..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package vn.vnpttech.ssdc.nms.webapp.util;

/**
 *
 * @author longdq
 */
public class Constant {

    public static final String FILE_NAME = "update.xml";
    public static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";

    public static class ErrorCode {

        public static final int SUCCESS = 0;
        public static final int SYSTEM_FAIL = -1;
        public static final int CPE_CONNECTION_ERROR = 1;
        public static final int CPE_CONFIG_ERROR = 2;
        public static final int AUTHENTICATION_FAIL = 3;
        public static final int CONNECTION_ERROR = 4;
        public static final int CPE_ENQUEUED_REQUEST = 5;
        public static final int REQUEST_TIMEOUT = 6;
    }

    public static class FileType {

        public static final int PDF = 0;
        public static final int EXCEL = 1;
    }

    public static class Message {

        public static final String SUCCESS = "Success";
        public static final String FAILURE = "Failure";
        public static final String Error = "Error";

    }

}
