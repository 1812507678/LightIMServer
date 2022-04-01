package com.ideaout.im.util.tencent_cloud;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.ideaout.im.config.Config;
import com.ideaout.im.config.RabbitConfig;
import com.ideaout.im.util.GsonUtil;
import com.ideaout.im.util.HttpRequestProxy;
import com.ideaout.im.util.TextUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ImageCheckProxy {
    private static ImageCheckProxy instance;

    public static ImageCheckProxy getInstance() {
        if (instance == null) {
            instance = new ImageCheckProxy();
        }
        return instance;
    }

    private boolean getTencentImageCheckResult(List<String> imageUrlList) {
        boolean isLegal = true;
        try {
            //imageUrlList.add("https://upload-images.jianshu.io/upload_images/1416781-7e8ad3012b28d991.png");
            //imageUrlList.add("https://img1.mm131.me/pic/5022/1.jpg");
            //imageUrlList.add("http://193.112.113.190/upload/imgs/20190702/1562078069844_536.jpg");
            //imageUrlList.add("http://193.112.113.190/upload/imgs/20190702/1562078069929_889.jpg");

            String appSign = ImageCheckSDKSignUtil.appSign(Config.TENCENT_IMAGECHECKSDK_APPID, Config.TENCENT_IMAGECHECKSDK_SECRETID, Config.TENCENT_IMAGECHECKSDK_SECRETKEY, "", 1000 * 60);
            ImageCheckRequestParam requestParam = new ImageCheckRequestParam(imageUrlList);
            String[] param = {GsonUtil.toJson(requestParam)};

            Map<String, String> headParamMap = new HashMap<>();
            headParamMap.put("content-type", "application/json");
            headParamMap.put("authorization", appSign);

            System.out.println("图片过滤校验请求数据=====：" + GsonUtil.toJson(requestParam));
            String result = HttpRequestProxy.post("https://recognition.image.myqcloud.com/detection/porn_detect", param, headParamMap);
            System.out.println("图片过滤校验返回数据=====：" + result);
            ImageCheckResponseData responseData = GsonUtil.fromJson(result, ImageCheckResponseData.class);

            if (responseData != null && responseData.result_list != null) {
                for (ImageCheckResponseData.ResponseItemData responseItemData : responseData.result_list) {
                    if (responseItemData.code == 0) {
                        if (responseItemData.data.result != 0) {  //不等于0，表示不正常
                            isLegal = false;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return isLegal;
    }

    public boolean isImageLegal(List<String> imageUrlList) {
        return getTencentImageCheckResult(imageUrlList);
    }

    /**
     * 识别二维码
     */
    public boolean isImageContainQRCode(File file) {
        return !TextUtil.isEmpty(getImageContainQRCodeContent(file));
    }

    public String getImageContainQRCodeContent(File file) {
        MultiFormatReader formatReader = new MultiFormatReader();
        //读取指定的二维码文件
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(file);
            BinaryBitmap binaryBitmap= new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
            //定义二维码参数
            Map  hints= new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            com.google.zxing.Result result = formatReader.decode(binaryBitmap, hints);
            //输出相关的二维码信息
            //System.out.println("解析结果："+result.toString());
            //System.out.println("二维码格式类型："+result.getBarcodeFormat());
            System.out.println("二维码文本内容："+result.getText());
            bufferedImage.flush();

            return result.getText();
        }
        catch (IOException | NotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 基于图片路径
    public static String deEncodeByPath(String path) {
        String content = null;
        BufferedImage image;
        try {
            image = ImageIO.read(new File(path));
            image = imgColorContrast(image, 100);
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);//解码
            content = result.getText();
            System.out.println("二维码文本内容 new："+content);
        }
        catch (NotFoundException e) {
            //这里判断如果识别不了带LOGO的图片，重新添加上一个属性
            try {
                image = ImageIO.read(new File(path));
                image = imgColorContrast(image, 100);
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                Binarizer binarizer = new HybridBinarizer(source);
                BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
                Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
                //设置编码格式
                hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
                //设置优化精度
                hints.put(DecodeHintType.TRY_HARDER, Boolean.FALSE);
                //设置复杂模式开启（使用这种方式就可以识别微信的二维码了）
                hints.put(DecodeHintType.PURE_BARCODE,Boolean.TYPE);
                Result result = new MultiFormatReader().decode(binaryBitmap, hints);//解码
                content = result.getText();
                System.out.println("二维码文本内容 new："+content);
            } catch (NotFoundException en) {
                en.printStackTrace();
                return null;
            } catch (IOException e1) {
                e1.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return content;
    }

    // 调整图片对比度（提高偏暗环境下二维码识别成功率）
    public static BufferedImage imgColorContrast(BufferedImage imgsrc, int contrast) {
        try {
            int contrast_average = 128;
            //创建一个不带透明度的图片
            BufferedImage back=new BufferedImage(imgsrc.getWidth(), imgsrc.getHeight(),BufferedImage.TYPE_INT_RGB);
            int width = imgsrc.getWidth();
            int height = imgsrc.getHeight();
            int pix;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int pixel = imgsrc.getRGB(j, i);
                    Color color = new Color(pixel);

                    if (color.getRed() < contrast_average)
                    {
                        pix = color.getRed()- Math.abs(contrast);
                        if (pix < 0) pix = 0;
                    }
                    else
                    {
                        pix = color.getRed() + Math.abs(contrast);
                        if (pix > 255) pix = 255;
                    }
                    int red= pix;
                    if (color.getGreen() < contrast_average)
                    {
                        pix = color.getGreen()- Math.abs(contrast);
                        if (pix < 0) pix = 0;
                    }
                    else
                    {
                        pix = color.getGreen() + Math.abs(contrast);
                        if (pix > 255) pix = 255;
                    }
                    int green= pix;
                    if (color.getBlue() < contrast_average)
                    {
                        pix = color.getBlue()- Math.abs(contrast);
                        if (pix < 0) pix = 0;
                    }
                    else
                    {
                        pix = color.getBlue() + Math.abs(contrast);
                        if (pix > 255) pix = 255;
                    }
                    int blue= pix;

                    color = new Color(red,green,blue);
                    int x=color.getRGB();
                    back.setRGB(j,i,x);
                }
            }
            return back;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
