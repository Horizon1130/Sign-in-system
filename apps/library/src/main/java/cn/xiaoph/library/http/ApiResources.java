package cn.xiaoph.library.http;

/**
 * Created by Administrator on 2018/8/24.
 */

public class ApiResources {

    public static String domain_name;
    public static int timeout;
    public static String token;
    public static String account;
    public static Integer type;

    public static String getHttpRequestPath() {
        String http = String.format("http://%s/api/", domain_name);
        return http;
    }

}
