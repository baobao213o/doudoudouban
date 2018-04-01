package com.xxx.library.network.exception;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.xxx.library.BaseApplication;
import com.xxx.library.R;
import com.xxx.library.entity.ErrorResponse;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;


public class ExceptionHandle {

    private ErrorResponse error = new ErrorResponse();

    ResponseThrowable handleException(Throwable e) {
        ResponseThrowable ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponseThrowable(e, httpException.code());
            ex.message = e.getMessage();
            try {
                ResponseBody body = httpException.response().errorBody();
                if (body != null) {
                    error = new Gson().fromJson(body.string(), ErrorResponse.class);
                    if (error != null) {
                        ex.message = error.getErrorExplan(error.code);
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return ex;
        } else if (e instanceof ServerException) {
            ServerException resultException = (ServerException) e;
            ex = new ResponseThrowable(resultException, resultException.code);
            ex.message = resultException.message;
            return ex;
        } else if (e instanceof JsonParseException || e instanceof JSONException) {
            ex = new ResponseThrowable(e, ERROR.PARSE_ERROR);
            ex.message = BaseApplication.getInstance().getString(R.string.common_network_parse_error);
            return ex;
        } else if (e instanceof ConnectException || e instanceof UnknownHostException) {
            ex = new ResponseThrowable(e, ERROR.NETWORD_ERROR);
            ex.message = BaseApplication.getInstance().getString(R.string.common_network_error);
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ResponseThrowable(e, ERROR.SSL_ERROR);
            ex.message = BaseApplication.getInstance().getString(R.string.common_network_ssl_error);
            return ex;
        } else {
            ex = new ResponseThrowable(e, ERROR.UNKNOW);
            ex.message = e.getMessage();
            return ex;
        }
    }


    /**
     * 通用错误码
     */

    public interface ERROR {
        /**
         * 未知错误
         */
        int UNKNOW = 10000;
        /**
         * 解析错误
         */
        int PARSE_ERROR = 10001;
        /**
         * 网络错误
         */
        int NETWORD_ERROR = 10002;
        /**
         * 证书出错
         */
        int SSL_ERROR = 10003;
    }

    public class ResponseThrowable extends Exception {
        public int code;
        public String message;

        ResponseThrowable(Throwable throwable, int code) {
            super(throwable);
            this.code = code;
            this.message = throwable.getMessage();
        }

        public ErrorResponse getResponseError() {
            return error;
        }

    }

    /**
     * ServerException发生后，将自动转换为ResponeThrowable返回
     */
    class ServerException extends RuntimeException {
        int code;
        String message;
    }
}
