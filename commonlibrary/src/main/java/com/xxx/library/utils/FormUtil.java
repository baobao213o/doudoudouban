package com.xxx.library.utils;


import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormUtil {

    /**
     * 是否是有效的手机号码
     *
     * @param mobiles 手机号码
     * @return 是否有效
     */
    public static boolean isValidPhoneNumber(String mobiles) {
        String telRegex = "[1][0123456789]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex);
    }


    public static boolean checkEmail(String email){
        try{
            String check = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            return matcher.matches();
        }catch(Exception e){
            return false;
        }
    }

}
