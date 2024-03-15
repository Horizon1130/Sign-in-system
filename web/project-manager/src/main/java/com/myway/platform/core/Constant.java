package com.myway.platform.core;

import com.myway.platform.utils.UUIDUtils;

public class Constant {

    public final static String SYS_SESSION_USER = "SYS_SESSION_USER";

    public static String getImageDir(String dir, String originalFilename) {
        String fileName = UUIDUtils.getEncryUUID();
        String fileSuffix = getFileSuffix(originalFilename);
        return String.format("%s%s.%s", dir, fileName, fileSuffix);
    }

    public static String getFileSuffix(String originalFilename) {
        String fileExt = originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
                .toLowerCase();
        return fileExt;
    }
}
