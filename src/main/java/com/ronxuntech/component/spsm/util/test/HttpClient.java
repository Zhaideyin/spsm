package com.ronxuntech.component.spsm.util.test;


import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by train on 16/2/22.
 */
public class HttpClient {
    private static OkHttpClient client;

    private HttpClient(){

    }
    public static final HttpClient single=new HttpClient();
    public static HttpClient getInstance(){
        if(client==null){
            client=new OkHttpClient();
        }
        return single;
    }
  /*  public static HttpClient getHttpsInstance(){
        if(client==null){
            X509TrustManager trustManager;
            SSLSocketFactory sslSocketFactory;
            final InputStream inputStream;
            try {
                File file = new File(PathUtil.getClassResources()+"/ssl2.cer");
                inputStream=new FileInputStream(file);// 得到证书的输入流

                trustManager = trustManagerForCertificates(inputStream);//以流的方式读入证书
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{trustManager}, null);
                sslSocketFactory = sslContext.getSocketFactory();
                client = new OkHttpClient.Builder()
                        .sslSocketFactory(sslSocketFactory, trustManager)
                        .build();
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return single;
    }*/
    /**
     * 同步http请求
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public String syncGet(String url,Map<String,String> params) throws IOException{
    	String prefix="?t="+System.currentTimeMillis();
    	if(url.indexOf("?")>0){
    		prefix="&t="+System.currentTimeMillis();
    	}
    	if(params!=null){
    		url+=prefix;
    		for (Map.Entry<String, String> entry : params.entrySet()) {  
      		  
        		url+="&"+entry.getKey()+"="+entry.getValue();
        	  
        	}  
    	}
    	Request request = new Request.Builder()
	      .url(url)
	      .build();

	  Response response = client.newCall(request).execute();
	  return response.body().string();
    }
    public void syncPost(String url,Map<String, String> params) throws IOException {
    	FormBody.Builder formBuilder=new FormBody.Builder();
    	if(params!=null){

    		for (Map.Entry<String, String> entry : params.entrySet()) {  
        		  
        		formBuilder.add(entry.getKey(), entry.getValue());  
        	  
        	}  
    	}
    	
    	RequestBody formBody = formBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        client.newCall(request).execute();
    }
    public void get(String url,Map<String,Object> params,final HttpListener httpListener) {
    	String prefix="?";//"?t="+System.currentTimeMillis();
    	if(url.indexOf("?")>0){
    		prefix="";
    	}
    	if(params!=null){
    		url+=prefix;
    		for (Map.Entry<String, Object> entry : params.entrySet()) {
      		  
        		url+="&"+entry.getKey()+"="+entry.getValue();
        	  
        	}  
    	}
    	
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                httpListener.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                httpListener.onSuccess(response);
            }
        });
    }
    public void post(String url,Map<String,String> params,final HttpListener httpListener) {
    	
    	FormBody.Builder formBuilder=new FormBody.Builder();
    	if(params!=null){
    		for (Map.Entry<String, String> entry : params.entrySet()) {  
      		  
        		formBuilder.add(entry.getKey(), entry.getValue());  
        	  
        	}  
    	}
    	
    	RequestBody formBody = formBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                httpListener.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                httpListener.onSuccess(response);
            }
        });
    }

    /**
     * 以流的方式添加信任证书
     */
    /**
     * Returns a trust manager that trusts {@code certificates} and none other. HTTPS services whose
     * certificates have not been signed by these certificates will fail with a {@code
     * SSLHandshakeException}.
     * <p>
     * <p>This can be used to replace the host platform's built-in trusted certificates with a custom
     * set. This is useful in development where certificate authority-trusted certificates aren't
     * available. Or in production, to avoid reliance on third-party certificate authorities.
     * <p>
     * <p>
     * <h3>Warning: Customizing Trusted Certificates is Dangerous!</h3>
     * <p>
     * <p>Relying on your own trusted certificates limits your server team's ability to update their
     * TLS certificates. By installing a specific set of trusted certificates, you take on additional
     * operational complexity and limit your ability to migrate between certificate authorities. Do
     * not use custom trusted certificates in production without the blessing of your server's TLS
     * administrator.
     */
    private static X509TrustManager trustManagerForCertificates(InputStream in)
            throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }

        // Put the certificates a key store.
        char[] password = "password".toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }


    /**
     * 添加password
     * @param password
     * @return
     * @throws GeneralSecurityException
     */
    private static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType()); // 这里添加自定义的密码，默认
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

}
