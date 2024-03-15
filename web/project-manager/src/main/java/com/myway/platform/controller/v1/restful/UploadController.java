package com.myway.platform.controller.v1.restful;

import com.alibaba.fastjson.JSONObject;
import com.myway.platform.api.ReturnResult;
import com.myway.platform.controller.BaseController;
import com.myway.platform.utils.FileUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Iterator;

@Slf4j
@RestController
@RequestMapping("/v1/files/")
public class UploadController extends BaseController {

    @SneakyThrows
    @PostMapping("upload")
    public ReturnResult save(MultipartHttpServletRequest multipartRequest) {
        ReturnResult result = new ReturnResult();
        for (Iterator it = multipartRequest.getFileNames(); it.hasNext(); ) {
            String key = (String) it.next();
            MultipartFile imgFile = multipartRequest.getFile(key);

            if (imgFile != null) {
                FileUtil util = new FileUtil(imgFile, filePath, String.valueOf(System.currentTimeMillis()));
                Thread.sleep(500);
                result.setCode(0);
                JSONObject data = new JSONObject();
                data.put("src", doMain + util.saveFile());
                result.setData(data);
            }
        }
        return result;
    }
}
