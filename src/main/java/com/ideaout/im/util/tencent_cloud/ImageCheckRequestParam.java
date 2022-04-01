package com.ideaout.im.util.tencent_cloud;

import com.ideaout.im.config.Config;
import com.ideaout.im.config.RabbitConfig;

import java.util.List;

public class ImageCheckRequestParam {
    /*{
        "appid": 1253928527,
        "url_list": [
            "https://upload-images.jianshu.io/upload_images/1416781-7e8ad3012b28d991.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/692/format/webp",
            "https://files.jb51.net/file_images/article/201810/201810030852322.png"
        ]
    }*/

    private long appid = Config.TENCENT_IMAGECHECKSDK_APPID;
    private List<String> url_list;

    ImageCheckRequestParam(List<String> url_list) {
        this.url_list = url_list;
    }

}
