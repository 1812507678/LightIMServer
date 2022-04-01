package com.ideaout.im.util.tencent_cloud;

import java.util.List;

public class ImageCheckResponseData {
    /*{
        "result_list": [
            {
                "code": 0,
                "message": "success",
                "url": "https://upload-images.jianshu.io/upload_images/1416781-7e8ad3012b28d991.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/692/format/webp",
                "data": {
                    "result": 0,
                    "forbid_status": 0,
                    "confidence": 3.884,
                    "hot_score": 12.364,
                    "normal_score": 87.629,
                    "porn_score": 0.007
                }
            },
            {
                "code": -1308,
                "message": "ERROR_DOWNLOAD_IMAGE_FAILED",
                "url": "https://files.jb51.net/file_images/article/201810/201810030852322.png"
            }
        ]
    }*/

    public List<ResponseItemData> result_list;

    class ResponseItemData {
        public int code;
        public String message;
        public String url;
        public Data data;

        class Data {
            public int result;
            public double forbid_status;
            public double hot_score;
            public double normal_score;
            public double porn_score;
        }
    }
}
