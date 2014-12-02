package com.kraqen.crackn;

import android.content.Context;

import com.loopj.android.http.*;

import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * CracknRestClient Class
 * Simple REST client interface for communications with the Crackn API.
 *
 * Created by Will on 11/23/14.
 */
public class CracknRestClient {
    private static final String BASE_URL = "http://54.69.211.217/";
    private static final int CONNECTION_TIMEOUT = 5000;

    private static AsyncHttpClient client = new AsyncHttpClient();

    private static Context context;

    public static void usePersistentCookieStore(Context context) {
        CracknRestClient.context = context;
        client.setCookieStore(new PersistentCookieStore(context));
    }

    public static void setContext(Context context) {
        CracknRestClient.context = context;
    }

    public static void get(String url,
                           RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(CONNECTION_TIMEOUT);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url,
                            RequestParams params,
                            AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(CONNECTION_TIMEOUT);
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url,
                            JSONObject json,
                            AsyncHttpResponseHandler responseHandler)
                            throws UnsupportedEncodingException {
        StringEntity se = new StringEntity(json.toString());
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, RequestParams.APPLICATION_JSON));
        client.setTimeout(CONNECTION_TIMEOUT);
        client.post(context, getAbsoluteUrl(url), se, RequestParams.APPLICATION_JSON,
                responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
