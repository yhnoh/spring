package org.example.springmetricexternalapi.retrofit;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

public class RetrofitEventListener extends EventListener {
    @Override
    public void secureConnectStart(@NotNull Call call) {
        System.out.println("RetrofitEventListener.secureConnectStart");
    }

    @Override
    public void secureConnectEnd(@NotNull Call call, @Nullable Handshake handshake) {
        System.out.println("RetrofitEventListener.secureConnectEnd");
    }

    @Override
    public void satisfactionFailure(@NotNull Call call, @NotNull Response response) {
        System.out.println("RetrofitEventListener.satisfactionFailure");
    }

    @Override
    public void responseHeadersStart(@NotNull Call call) {
        System.out.println("RetrofitEventListener.responseHeadersStart");
    }

    @Override
    public void responseHeadersEnd(@NotNull Call call, @NotNull Response response) {
        System.out.println("RetrofitEventListener.responseHeadersEnd");
    }

    @Override
    public void responseFailed(@NotNull Call call, @NotNull IOException ioe) {
        System.out.println("RetrofitEventListener.responseFailed");
    }

    @Override
    public void responseBodyStart(@NotNull Call call) {
        System.out.println("RetrofitEventListener.responseBodyStart");
    }

    @Override
    public void responseBodyEnd(@NotNull Call call, long byteCount) {
        System.out.println("RetrofitEventListener.responseBodyEnd");
    }

    @Override
    public void requestHeadersStart(@NotNull Call call) {
        System.out.println("RetrofitEventListener.requestHeadersStart");
    }

    @Override
    public void requestHeadersEnd(@NotNull Call call, @NotNull Request request) {
        System.out.println("RetrofitEventListener.requestHeadersEnd");
    }

    @Override
    public void requestFailed(@NotNull Call call, @NotNull IOException ioe) {
        System.out.println("RetrofitEventListener.requestFailed");
    }

    @Override
    public void requestBodyStart(@NotNull Call call) {
        System.out.println("RetrofitEventListener.requestBodyStart");
    }

    @Override
    public void requestBodyEnd(@NotNull Call call, long byteCount) {
        System.out.println("RetrofitEventListener.requestBodyEnd");
    }

    @Override
    public void proxySelectStart(@NotNull Call call, @NotNull HttpUrl url) {
        System.out.println("RetrofitEventListener.proxySelectStart");
    }

    @Override
    public void proxySelectEnd(@NotNull Call call, @NotNull HttpUrl url, @NotNull List<Proxy> proxies) {
        System.out.println("RetrofitEventListener.proxySelectEnd");
    }

    @Override
    public void dnsStart(@NotNull Call call, @NotNull String domainName) {
        System.out.println("RetrofitEventListener.dnsStart");
    }

    @Override
    public void dnsEnd(@NotNull Call call, @NotNull String domainName, @NotNull List<InetAddress> inetAddressList) {
        System.out.println("RetrofitEventListener.dnsEnd");
    }

    @Override
    public void connectionReleased(@NotNull Call call, @NotNull Connection connection) {
        System.out.println("RetrofitEventListener.connectionReleased");
    }

    @Override
    public void connectionAcquired(@NotNull Call call, @NotNull Connection connection) {
        System.out.println("RetrofitEventListener.connectionAcquired");
    }

    @Override
    public void connectStart(@NotNull Call call, @NotNull InetSocketAddress inetSocketAddress, @NotNull Proxy proxy) {
        System.out.println("RetrofitEventListener.connectStart");
    }

    @Override
    public void connectFailed(@NotNull Call call, @NotNull InetSocketAddress inetSocketAddress, @NotNull Proxy proxy, @Nullable Protocol protocol, @NotNull IOException ioe) {
        System.out.println("RetrofitEventListener.connectFailed");
    }

    @Override
    public void connectEnd(@NotNull Call call, @NotNull InetSocketAddress inetSocketAddress, @NotNull Proxy proxy, @Nullable Protocol protocol) {
        System.out.println("RetrofitEventListener.connectEnd");
    }

    @Override
    public void canceled(@NotNull Call call) {
        System.out.println("RetrofitEventListener.canceled");
    }

    @Override
    public void callStart(@NotNull Call call) {
        System.out.println("RetrofitEventListener.callStart");
    }

    @Override
    public void callFailed(@NotNull Call call, @NotNull IOException ioe) {
        System.out.println("RetrofitEventListener.callFailed");
    }

    @Override
    public void callEnd(@NotNull Call call) {
        System.out.println("RetrofitEventListener.callEnd");
    }

    @Override
    public void cacheMiss(@NotNull Call call) {
        System.out.println("RetrofitEventListener.cacheMiss");
    }

    @Override
    public void cacheHit(@NotNull Call call, @NotNull Response response) {
        System.out.println("RetrofitEventListener.cacheHit");
    }

    @Override
    public void cacheConditionalHit(@NotNull Call call, @NotNull Response cachedResponse) {
        System.out.println("RetrofitEventListener.cacheConditionalHit");
    }
}
