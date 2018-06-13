package com.xxx.library.fresco;

import android.net.Uri;

/**
 * Created by gaoruochen on 18-6-8.
 */

public interface FrescoBitmapCallback<T> {

    void onSuccess(Uri uri, T result);

    void onFailure(Uri uri, Throwable throwable);

    void onCancel(Uri uri);
}
