package com.xxx.library;


public interface Constant {

    interface Authentication {

        String URL = "https://www.douban.com/service/auth2/token";

        String KEY = "0dad551ec0f84ed02907ff5c42e8ec70";

        String SECRET = "9e8bb54dc3288cdf";

        String REDIRECT_URI = "frodo://app/oauth/callback/";

        interface GrantType {

            String GRANT_TYPE_PASSWORD = "password";

            String GRANT_TYPE_REFRESH = "refresh_token";

        }

        interface Header {

            String AUTHORIZATION = "Authorization";

        }

    }

    interface AuthenticationAccount {

        String ACCOUNT_TYPE = BuildConfig.application_id;

        String ACCOUNT_AUTH_TOKEN = BuildConfig.application_id + "auth_token";

        String ACCOUNT_REFRESH_TOKEN = BuildConfig.application_id + "refresh_token";

        String ACCOUNT_USER_NAME = BuildConfig.application_id + "user_name";

        String ACCOUNT_USER_ID = BuildConfig.application_id + "user_id";

        int INVALID_USER_ID = -1;

        String EXTRA_AUTH_MODE = "extra_auth_mode";

        int AUTH_MODE_NEW = 0;

        int AUTH_MODE_UPDATE = 1;

        int AUTH_MODE_CONFIRM = 2;


    }

    String BASE_URL = "https://api.douban.com/";


    interface ARouter {

        String AROUTER_MAIN_SPLASH = "/main/splash/SplashActivity";

        String AROUTER_MAIN_MAIN = "/main/main/MainActivity";
    }

}
