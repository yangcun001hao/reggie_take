package com.guole.reggie.controller;

import com.guole.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件的上传和下载
 * @author 郭乐
 * @date 2022/10/17
 */
@Controller
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${picture.path}")
    private String basePath;

    /**
     * 文件的上传
     * 参数为MultipartFile，由spring_web提供，形参名不能随便写，前端的name一致
     * @param file 文件
     * @return {@link R}<{@link String}>
     */
    @PostMapping("/upload")
    @ResponseBody
    public R<String> upload(MultipartFile file){
        //file是临时文件，需要进行转存，否则本次请求完成之后会消失

        //获取原始文件名,有重名，随机生成名字
        String name = file.getOriginalFilename();

        String suffix = name.substring(name.lastIndexOf("."));

        //使用UUID重新生成文件名，防止文件名重复造车覆盖
        String filename = UUID.randomUUID().toString() + suffix;
        File dir = new File(basePath);
        //目录不存在，创建目录
        if (!dir.exists()){
            dir.mkdirs();
        }

        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(basePath+filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("存储路径为：{}",basePath+filename);
        return R.success(filename);
    }

    /**
     * 文件下载
     *
     * @param name 名字
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        log.info("回显图片：{}",name);

        try {
            //输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            //输出流写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ( (len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
