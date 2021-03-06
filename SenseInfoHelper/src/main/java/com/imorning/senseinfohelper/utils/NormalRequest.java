package com.imorning.senseinfohelper.utils;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 通用的客户端。
 */
public class NormalRequest {
    static {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
    }

    protected int connectTimeout = 3000;//3秒
    protected int readTimeout = 15000;//15秒
    protected String charset = "utf-8";  //出去时的编码
    protected String charset_out = "utf-8";  //读入时的编码，本来读入时编码可以程序自动识别，但有些网站输出头定义的是utf，但实际是gbk，此时就需要定义字段
    protected int limitReadSize = 0;//默认读取最大数据。<=0为不限，而10*1024*1024为10MB
    protected boolean printException = true; //是否输出异常
    protected boolean allowRedirect = true; //是否允许重定向
    protected byte[] body = null;//直接提交json数据或二进制流
    protected String bodyString = null;//直接提交body的字符串，字符集是charset
    protected int res_status = 200;//返回头的状态码
    protected String url;
    protected Map<String, String> textMap = new HashMap<String, String>();
    protected Map<String, File> uploadMap = new HashMap<String, File>();
    protected Map<String, String> headMap = new HashMap<String, String>();
    protected Map<String, List<String>> res_headMap = new HashMap<String, List<String>>();//返回时的Map
    Proxy proxy = null;

    public NormalRequest() {
        this.addHeadPara("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2427.7 Safari/537.36");
    }

    public NormalRequest(String url) {
        this.url = url;
        //先设置一个默认的头
        this.addHeadPara("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2427.7 Safari/537.36");
    }


    public boolean isAllowRedirect() {
        return allowRedirect;
    }

    public NormalRequest setAllowRedirect(boolean allowRedirect) {
        this.allowRedirect = allowRedirect;
        return this;
    }

    public boolean isPrintException() {
        return printException;
    }

    public NormalRequest setPrintException(boolean printException) {
        this.printException = printException;
        return this;
    }

    public int getLimitReadSize() {
        return this.limitReadSize;
    }

    public NormalRequest setLimitReadSize(int size) {
        this.limitReadSize = size;
        return this;
    }

    public NormalRequest setProxy(String proxyIp, int proxyPort) {
        this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp, proxyPort));
        return this;
    }

    public Proxy getProxy() {
        return this.proxy;
    }

    public String getCharset_out() {
        return charset_out;
    }

    public NormalRequest setCharset_out(String charset_out) {
        this.charset_out = charset_out;
        return this;
    }

    public String getBodyString() {
        return bodyString;
    }

    public NormalRequest setBodyString(String bodyString) {
        this.bodyString = bodyString;
        return this;
    }

    public int getRes_status() {
        return res_status;
    }

    public NormalRequest setRes_status(int res_status) {
        this.res_status = res_status;
        return this;
    }

    public byte[] getBody() {
        return body;
    }

    public NormalRequest setBody(byte[] body) {
        this.body = body;
        return this;
    }

    public Map<String, List<String>> getRes_headMap() {
        return res_headMap;
    }

    public void setRes_headMap(Map<String, List<String>> res_headMap) {
        this.res_headMap = res_headMap;
    }

    public Map<String, String> getTextMap() {
        return textMap;
    }

    public void setTextMap(Map<String, String> textMap) {
        this.textMap = textMap;
    }

    public String getUrl() {
        return url;
    }

    /**
     * 设置客户端与showapi网关的最大长连接数量。
     */
    public NormalRequest setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public NormalRequest setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public Map<String, File> getUploadMap() {
        return uploadMap;
    }

    public void setUploadMap(Map<String, File> uploadMap) {
        this.uploadMap = uploadMap;
    }

    public Map<String, String> getHeadMap() {
        return headMap;
    }

    public void setHeadMap(Map<String, String> headMap) {
        this.headMap = headMap;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public NormalRequest setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }
//	DEFAULT_CHARSET

    public int getReadTimeout() {
        return readTimeout;
    }

    public NormalRequest setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    /**
     * 添加post体的字符串参数
     */
    public NormalRequest addTextPara(String key, String value) {
        this.textMap.put(key, value);
        return this;
    }

    /**
     * 添加post体的上传文件参数
     */
    public NormalRequest addFilePara(String key, File item) {
        this.uploadMap.put(key, item);
        return this;
    }

    /**
     * 添加head头的字符串参数
     */
    public NormalRequest addHeadPara(String key, Object value) {
        if (value != null) {
            this.headMap.put(key, value.toString());
        } else {
            this.headMap.put(key, "");
        }
        return this;
    }





    /**
     * @param heads : Accept-Encoding: gzip, deflate\r\nHost: www.qichacha.com\r\n
     *              添加字符串的头参数,用换行符和冒号组成的多个键值对
     */

    public NormalRequest addHeads(String heads) {
        if (StringUtils.isEmpty(heads) || !heads.contains("\n")) {
            return this;
        }
        String[] hs;
        if (heads.contains("\r\n")) {
            hs = heads.split("\r\n");
        } else if (heads.contains("\n")) {
            hs = heads.split("\n");
        } else if (heads.contains(":")) {
            String pre = "";
            if (heads.startsWith(":")) {
                pre = ":";
                heads = heads.substring(1);
            }
            if (heads.contains(":")) {
                String[] kv = heads.split(":");
                this.headMap.put(pre + kv[0].trim(), heads.substring(heads.indexOf(":") + 1).trim());
            }
            return this;
        } else {
            return this;
        }
        for (String h : hs) {
            if (h.trim().equals("") || !h.contains(":")) {
                continue;
            }
            String pre = "";
            if (h.startsWith(":")) {
                pre = ":";
                h = h.substring(1);
            }
            if (h.contains(":")) {
                String[] kv = h.split(":");
                this.headMap.put(pre + kv[0].trim(), h.substring(h.indexOf(":") + 1).trim());
            }
        }
        return this;
    }

    public String post() {
        String res = "";
        try {
            res = WebUtils.doPost(this);
        } catch (Exception e) {
            if (printException) e.printStackTrace();
            res = "{\"ret_code\":-1,\"error\":\"" + e.toString() + "\"}";
        }
        return res;
    }

    public byte[] postAsByte() {
        byte[] res = null;
        try {
            res = WebUtils.doPostAsByte(this);
        } catch (Exception e) {
            if (printException) e.printStackTrace();
            res = ("{\"ret_code\":-1,\"error\":\"" + e.toString() + "\"}").getBytes(StandardCharsets.UTF_8);
        }
        return res;
    }

    public String get() {
        String res = "";
        try {
            res = WebUtils.doGet(this);
        } catch (Exception e) {
            if (printException) e.printStackTrace();
            res = "{\"ret_code\":-1,\"error\":\"" + e.toString() + "\"}";
        }
        return res;
    }

    public byte[] getAsByte() {
        byte[] res = null;
        try {
            res = WebUtils.doGetAsByte(this);
        } catch (Exception e) {
            if (printException) e.printStackTrace();
            res = ("{\"ret_code\":-1,\"error\":\"" + e.toString() + "\"}").getBytes(StandardCharsets.UTF_8);
        }
        return res;
    }

}
