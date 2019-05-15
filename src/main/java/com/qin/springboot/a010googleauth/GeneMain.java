package com.qin.springboot.a010googleauth;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GeneMain {

    public static void main(String[] args) {
       m1();

        //不同user定制不同的secretKey
        //m2();

        //二维码 ,参考m1()

        //客户端微信小程序 ： https://github.com/LCTT/WeApp-Password

    }

    /**
     *返回一个URL生成并显示二维码。用户扫描这个二维码
     *谷歌身份验证应用程序的智能手机注册身份验证代码
     *他们还可以手动输入秘钥key
     * @param user
     * @param host
     * @param secret 之前为用户生成的秘钥
     * @return 二维码的url
     */
    /**
     * @Definition:
     * @author: TangWenWu
     * @Created date: 2014-11-24
     * @param user
     * @param host
     * @param secret
     * @return
     */
    public static String getQRBarcodeURL(String user, String host, String secret) {
	    /*
	     *   由于是内网系统，无法访问google，所以注释
	     *    String format = "https://www.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s@%s%%3Fsecret%%3D%s";
	     *    return String.format(format, user, host, secret);
	     */
        //采用com.google.zxing 自己生成 二维码图片，保存在项目某个目录下
        // 二维码内容
        String content = "otpauth://totp/" + user + "@" + host + "?secret=" + secret;
        // 二维码宽度
        int width = 300;
        // 二维码高度
        int height = 300;
        // 二维码存放地址
        String imageName = "googleAuthCode_" + user + "_" + host + ".png";
        return QRUtil.generateCommonQR(content, width, height, imageName);
    }

    /**
     * 不同user定制不同的secretKey ,这样通过用户名直接获取可以code，不再需要分配给用户key
     *
     */
    public static void m2() {
        String userName= "Bob";
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key = gAuth.createCredentials(userName);
        String secretKey = key.getKey();
        System.out.println(secretKey);
        int code;
        boolean isCodeValid ;


        code = gAuth.getTotpPassword(secretKey);
        isCodeValid = gAuth.authorizeUser(userName, code);
        System.out.println("here is one "+code+"-->"+isCodeValid);

        code = gAuth.getTotpPasswordOfUser(userName);
        isCodeValid = gAuth.authorizeUser(userName, code);
        System.out.println("here is two "+code+"-->"+isCodeValid);

    }

    /**
     * (1)获取secretKey
     * 把secretKey 安装到手机端的google auth上
     * 并从手机端获取pwd，验证直到通过
     *
     * (2)通过config，配置密码有效时间（默认30s）,配置密码长度(app端默认是6位，无法更改)
     *
     *
     */
    public static void m1(){
        GoogleAuthenticatorConfig config = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder().setCodeDigits(6).setTimeStepSizeInMillis(TimeUnit.SECONDS.toMillis(300L)).build();
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key = gAuth.createCredentials();

        String secretKey = key.getKey();
        System.out.println(secretKey);

        Scanner sc = new Scanner(System.in);
        System.out.println("enter key:");
        secretKey = sc.nextLine();

        //String.format("https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s@%s%%3Fsecret%%3D%s", user, host, secret);
        String path=getQRBarcodeURL("qinchao","",secretKey);
        System.out.println("image path is "+path);

        System.out.println("enter password:");
        do{
            String password = sc.nextLine();

            //根据secretKey获取code
            int code = gAuth.getTotpPassword(secretKey);
            String password2 = String.valueOf(code);
            System.out.println("here is pwd:"+password);
            System.out.println("here is pwd test :"+password2);
          /*  try {
                      Thread.sleep(60*1000);
            } catch (Exception e) {
                           e.printStackTrace();
            }

            System.out.println("go");*/

            boolean isCodeValid = gAuth.authorize(secretKey, Integer.parseInt(password));
            if(isCodeValid){
                System.out.println("right password!bye bye");
                break;
            }else{
                System.out.println("error password!enter again:");
            }
        }while (true);
    }
}
