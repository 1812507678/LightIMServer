package com.ideaout.im.util;

import com.ideaout.im.config.Config;
import com.ideaout.im.config.RabbitConfig;
import com.ideaout.im.http.ResponseDataBase;
import com.ideaout.im.util.tencent_cloud.ImageCheckProxy;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileUploadUtil {
    private static Logger logger = Logger.getLogger(FileUploadUtil.class);

    public static Map<String, List<String>> uploadFile(MultipartFile[] fileArry, ResponseDataBase responseDataBase,boolean isImageLegalCheck) {
        boolean isFileContainSensitiveInfo = false;
        Map<String, List<String>> resultMap = new HashMap<>();

        List<String> imgList = new ArrayList<>();
        List<String> imgList_smaller = new ArrayList<>();
        List<File> localFileList = new ArrayList<>();
        if (fileArry != null && fileArry.length > 0) {
            for (MultipartFile aFileArry : fileArry) {
                String fileName = aFileArry.getOriginalFilename();//获取文件名加后缀
                if (!TextUtil.isEmpty(fileName)) {
                    String fileF = fileName.substring(fileName.lastIndexOf("."));//文件后缀
                    fileName = new Date().getTime() + "_" + new Random().nextInt(1000) + fileF;//新的文件名

                    String fileAdd = new SimpleDateFormat("yyyyMMdd").format(new Date());
                    String filePath = Config.getNginxFileDirectory() + fileAdd; //文件存储位置
                    File file = new File(filePath);
                    //先判断文件是否存在,如果文件夹不存在则创建
                    if (!file.exists() && !file.isDirectory()) {
                        boolean mkdir = file.mkdir();
                        logger.error("创建文件夹：" + mkdir);
                    }
                    File targetFile = new File(file, fileName);
                    try {
                        aFileArry.transferTo(targetFile);
                        //注，待APP上线后returnUrl只返回fileAdd + "/" + fileName即可，客户端自己拼接URL
                        //String returnUrl = Config.serverUrl + Config.nginx_file_directory + fileAdd + "/" + fileName; //返回存储路径
                        String returnUrl = fileAdd + "/" + fileName; //返回存储路径
                        imgList.add(returnUrl);
                        localFileList.add(file);
                        logger.error("文件保存成功：" + filePath + "/" + fileName + " ,返回URL为：" + returnUrl);

                        if (isImageLegalCheck && ImageCheckProxy.getInstance().isImageContainQRCode(targetFile)){
                            //判断图片中是否包含二维码
                            isFileContainSensitiveInfo = true;
                            break;
                        }
                        else {
                            //压缩文件
                            String fileNameSmaller = "smaller_" + fileName;  //小图片文件名
                            File targetFileSmaller = new File(file, fileNameSmaller);
                            ImageZipUtil.zipImageFile(targetFile, targetFileSmaller, Config.compress_file_width, Config.compress_file_width, 1);
                            //压缩后返回的小图片URL
                            //String returnSmallerUrl = Config.serverUrl + Config.nginx_file_directory + fileAdd + "/" + fileNameSmaller; //返回存储路径
                            String returnSmallerUrl = fileAdd + "/" + fileNameSmaller; //返回存储路径
                            imgList_smaller.add(returnSmallerUrl);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error(e.getMessage());
                    }
                }
            }

            //上面没有检测出问题，则需要去腾讯云api检测
//            if (isImageLegalCheck && !isFileContainSensitiveInfo){
//                boolean isImageLegal = ImageCheckProxy.getInstance().isImageLegal(imgList);
//                if (!isImageLegal) {
//                    //图片不合法，需要删除本地图片，然后返回提示
//                    for (File file : localFileList) {
//                        if (file.exists()) {
//                            file.delete();
//                        }
//                    }
//                    imgList.clear();
//                    imgList_smaller.clear();
//                    isFileContainSensitiveInfo = true;
//                }
//            }
        }

        resultMap.put("imgList", imgList);
        resultMap.put("imgList_smaller", imgList_smaller);


        if (isFileContainSensitiveInfo){
            responseDataBase.errorDec = HttpUtil.ErrorDec.ImageContainSensitiveMsg.desc;
            responseDataBase.code = HttpUtil.ErrorDec.ImageContainSensitiveMsg.value;
        }
        else {
            if (isHasUploadFileFailure(fileArry,imgList.size())){
                responseDataBase.errorDec = "文件上传失败，请检查网络后重试";
                responseDataBase.code = HttpUtil.ErrorDec.RequestError.value;
            }
        }
        return resultMap;
    }

    public static Map<String, List<String>> uploadFile(MultipartFile[] fileArry, ResponseDataBase responseDataBase) {
       return uploadFile(fileArry,responseDataBase,false);
    }

    //判断文件是有上传失败  上传失败返回true
    public static boolean isHasUploadFileFailure(MultipartFile[] file, int listUrlLength) {
        return file != null && file.length > 0 && listUrlLength == 0;
        //return file == null || file.length <= 0 || !TextUtil.isEmpty(returnImageListUrlString);
    }
}
