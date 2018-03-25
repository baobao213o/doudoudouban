package com.xxx.library.entity;


import android.content.Context;
import android.util.SparseArray;

import com.xxx.library.BaseApplication;
import com.xxx.library.R;

public class ErrorResponse {
    /**
     * msg : book_not_found
     * code : 6000
     * request : GET /v2/book/122056212
     */
    public String msg = "";
    public int code;
    public String request = "";

    private static SparseArray<HandleErrorCode> map;

    public String getErrorExplan(int code) {
        if (map == null) {
            map = new SparseArray<>();
        }
        HandleErrorCode handle = map.get(code);
        if (handle == null) {
            handle = new HandleErrorCode(code);
            map.put(code, handle);
        }
        return handle.msgExplan;
    }


    static class HandleErrorCode {

        String msgExplan;

        int httpStatus;


        HandleErrorCode(int code) {
            Context context = BaseApplication.getInstance();
            switch (code) {
                case INVALID_REQUEST_SCHEME:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_invalid_request_scheme);
                    break;
                case INVALID_REQUEST_METHOD:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_invalid_request_method);
                    break;
                case ACCESS_TOKEN_IS_MISSING:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_access_token_is_missing);
                    break;
                case INVALID_ACCESS_TOKEN:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_invalid_access_token);
                    break;
                case INVALID_APIKEY:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_invalid_apikey);
                    break;
                case APIKEY_IS_BLOCKED:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_apikey_is_blocked);
                    break;
                case ACCESS_TOKEN_HAS_EXPIRED:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_access_token_has_expired);
                    break;
                case INVALID_REQUEST_URI:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_invalid_request_uri);
                    break;
                case INVALID_CREDENCIAL1:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_invalid_credencial1);
                    break;
                case INVALID_CREDENCIAL2:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_invalid_credencial2);
                    break;
                case NOT_TRIAL_USER:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_not_trial_user);
                    break;
                case RATE_LIMIT_EXCEEDED1:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_rate_limit_exceeded1);
                    break;
                case RATE_LIMIT_EXCEEDED2:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_rate_limit_exceeded2);
                    break;
                case REQUIRED_PARAMETER_IS_MISSING:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_required_parameter_is_missing);
                    break;
                case UNSUPPORTED_GRANT_TYPE:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_unsupported_grant_type);
                    break;
                case UNSUPPORTED_RESPONSE_TYPE:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_unsupported_response_type);
                    break;
                case CLIENT_SECRET_MISMATCH:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_client_secret_mismatch);
                    break;
                case REDIRECT_URI_MISMATCH:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_redirect_uri_mismatch);
                    break;
                case INVALID_AUTHORIZATION_CODE:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_invalid_authorization_code);
                    break;
                case INVALID_REFRESH_TOKEN:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_invalid_refresh_token);
                    break;
                case USERNAME_PASSWORD_MISMATCH:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_username_password_mismatch);
                    break;
                case INVALID_USER:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_invalid_user);
                    break;
                case USER_HAS_BLOCKED:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_user_has_blocked);
                    break;
                case ACCESS_TOKEN_HAS_EXPIRED_SINCE_PASSWORD_CHANGED:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_access_token_has_expired_since_password_changed);
                    break;
                case ACCESS_TOKEN_HAS_NOT_EXPIRED:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_access_token_has_not_expired);
                    break;
                case INVALID_REQUEST_SCOPE:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_invalid_request_scope);
                    break;
                case INVALID_REQUEST_SOURCE:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_invalid_request_source);
                    break;
                case THIRDPARTY_LOGIN_AUTH_FAIED:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_thirdparty_login_auth_faied);
                    break;
                case USER_LOCKED:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_user_locked);
                    break;
                case UNKNOW_V2_ERROR:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_unknow_v2_error);
                    break;
                case NEED_PERMISSION:
                    httpStatus = 403;
                    msgExplan = context.getString(R.string.common_network_need_permission);
                    break;
                case URI_NOT_FOUND:
                    httpStatus = 404;
                    msgExplan = context.getString(R.string.common_network_uri_not_found);
                    break;
                case MISSING_ARGS:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_missing_args);
                    break;
                case IMAGE_TOO_LARGE:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_image_too_large);
                    break;
                case HAS_BAN_WORD:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_has_ban_word);
                    break;
                case INPUT_TOO_SHORT:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_input_too_short);
                    break;
                case TARGET_NOT_FOUNT:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_target_not_fount);
                    break;
                case NEED_CAPTCHA:
                    httpStatus = 403;
                    msgExplan = context.getString(R.string.common_network_need_captcha);
                    break;
                case IMAGE_UNKNOW:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_image_unknow);
                    break;
                case IMAGE_WRONG_FORMAT:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_image_wrong_format);
                    break;
                case IMAGE_WRONG_CK:
                    httpStatus = 403;
                    msgExplan = context.getString(R.string.common_network_image_wrong_ck);
                    break;
                case IMAGE_CK_EXPIRED:
                    httpStatus = 403;
                    msgExplan = context.getString(R.string.common_network_image_ck_expired);
                    break;
                case TITLE_MISSING:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_title_missing);
                    break;
                case DESC_MISSING:
                    httpStatus = 400;
                    msgExplan = context.getString(R.string.common_network_desc_missing);
                    break;
                default:
                    break;
            }

        }

    }

    //100	invalid_request_scheme 错误的请求协议
    public static final int INVALID_REQUEST_SCHEME = 100;

    //101	invalid_request_method 错误的请求方法
    public static final int INVALID_REQUEST_METHOD = 101;

    //102	access_token_is_missing 未找到 access_token
    public static final int ACCESS_TOKEN_IS_MISSING = 102;

    //103	invalid_access_token access_token 不存在或已被用户删除，或者用户修改了密码
    public static final int INVALID_ACCESS_TOKEN = 103;

    //104	invalid_apikey apikey 不存在或已删除
    public static final int INVALID_APIKEY = 104;

    //105	apikey_is_blocked apikey 已被禁用
    public static final int APIKEY_IS_BLOCKED = 105;

    //106	access_token_has_expired access_token 已过期
    public static final int ACCESS_TOKEN_HAS_EXPIRED = 106;

    //107	invalid_request_uri 请求地址未注册
    public static final int INVALID_REQUEST_URI = 107;

    //108	invalid_credencial1 用户未授权访问此数据
    public static final int INVALID_CREDENCIAL1 = 108;

    //109	invalid_credencial2 apikey 未申请此权限
    public static final int INVALID_CREDENCIAL2 = 109;

    //110	not_trial_user 未注册的测试用户
    public static final int NOT_TRIAL_USER = 110;

    //111	rate_limit_exceeded1 用户访问速度限制
    public static final int RATE_LIMIT_EXCEEDED1 = 111;

    //112	rate_limit_exceeded2 IP 访问速度限制
    public static final int RATE_LIMIT_EXCEEDED2 = 112;

    //113	required_parameter_is_missing 缺少参数
    public static final int REQUIRED_PARAMETER_IS_MISSING = 113;

    //114	unsupported_grant_type 错误的 grant_type
    public static final int UNSUPPORTED_GRANT_TYPE = 114;

    //115	unsupported_response_type 错误的 response_type
    public static final int UNSUPPORTED_RESPONSE_TYPE = 115;

    //116	client_secret_mismatch client_secret不匹配
    public static final int CLIENT_SECRET_MISMATCH = 116;

    //117	redirect_uri_mismatch redirect_uri不匹配
    public static final int REDIRECT_URI_MISMATCH = 117;

    //118	invalid_authorization_code authorization_code 不存在或已过期
    public static final int INVALID_AUTHORIZATION_CODE = 118;

    //119	invalid_refresh_token refresh_token 不存在或已过期
    public static final int INVALID_REFRESH_TOKEN = 119;

    //120	username_password_mismatch 用户名密码不匹配
    public static final int USERNAME_PASSWORD_MISMATCH = 120;

    //121	invalid_user 用户不存在或已删除
    public static final int INVALID_USER = 121;

    //122	user_has_blocked 用户已被屏蔽
    public static final int USER_HAS_BLOCKED = 122;

    //123	access_token_has_expired_since_password_changed 因用户修改密码而导致 access_token 过期
    public static final int ACCESS_TOKEN_HAS_EXPIRED_SINCE_PASSWORD_CHANGED = 123;

    //124	access_token_has_not_expired access_token 未过期
    public static final int ACCESS_TOKEN_HAS_NOT_EXPIRED = 124;

    //125	invalid_request_scope 访问的 scope 不合法，开发者不用太关注，一般不会出现该错误
    public static final int INVALID_REQUEST_SCOPE = 125;

    //126	invalid_request_source 访问来源不合法
    public static final int INVALID_REQUEST_SOURCE = 126;

    //127	thirdparty_login_auth_faied 第三方授权错误
    public static final int THIRDPARTY_LOGIN_AUTH_FAIED = 127;

    //128	user_locked 用户被锁定
    public static final int USER_LOCKED = 128;

    //999	unknow_v2_error	未知错误	400
    public static final int UNKNOW_V2_ERROR = 999;

    //1000	need_permission	需要权限	403
    public static final int NEED_PERMISSION = 1000;

    //1001	uri_not_found	资源不存在	404
    public static final int URI_NOT_FOUND = 1001;

    //1002	missing_args	参数不全	400
    public static final int MISSING_ARGS = 1002;

    //1003	image_too_large	上传的图片太大	400
    public static final int IMAGE_TOO_LARGE = 1003;

    //1004	has_ban_word	输入有违禁词	400
    public static final int HAS_BAN_WORD = 1004;

    //1005	input_too_short	输入为空，或者输入字数不够	400
    public static final int INPUT_TOO_SHORT = 1005;

    //1006	target_not_fount	相关的对象不存在，比如回复帖子时，发现小组被删掉了	400
    public static final int TARGET_NOT_FOUNT = 1006;

    //1007	need_captcha	需要验证码，验证码有误	403;
    public static final int NEED_CAPTCHA = 1007;

    //1008	image_unknow	不支持的图片格式	400
    public static final int IMAGE_UNKNOW = 1008;

    //1009	image_wrong_format	照片格式有误(仅支持JPG,JPEG,GIF,PNG或BMP)	400
    public static final int IMAGE_WRONG_FORMAT = 1009;

    //1010	image_wrong_ck	访问私有图片ck验证错误	403
    public static final int IMAGE_WRONG_CK = 1010;

    //1011	image_ck_expired	访问私有图片ck过期	403
    public static final int IMAGE_CK_EXPIRED = 1011;

    //1012	title_missing	题目为空	400
    public static final int TITLE_MISSING = 1012;

    //1013	desc_missing	描述为空	400
    public static final int DESC_MISSING = 1013;

}
