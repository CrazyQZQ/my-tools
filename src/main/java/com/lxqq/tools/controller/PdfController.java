package com.lxqq.tools.controller;

import com.lxqq.tools.common.domain.AjaxResult;
import com.lxqq.tools.common.handler.file.FileSuffixEnum;
import com.lxqq.tools.common.handler.file.utils.FileUtil;
import com.lxqq.tools.pdf.service.PdfTransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author QinQiang
 * @date 2023/9/13
 **/
@Slf4j
@RestController
@RequestMapping("tools/pdf")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PdfController {

    @Autowired
    private PdfTransferService pdfTransferService;

    /**
     * imgToPdf
     *
     * @param file 上次文件
     * @return 结果
     */
    @PostMapping("imgToPdf")
    public AjaxResult imgToPdf(@RequestParam("file") MultipartFile file) throws IOException {
        FileUtil.checkSuffix(Arrays.asList(FileSuffixEnum.PNG, FileSuffixEnum.JPG, FileSuffixEnum.JPEG), file.getOriginalFilename());
        String downloadUrl = pdfTransferService.imgToPdf(file.getInputStream());
        return AjaxResult.success(downloadUrl);
    }

    /**
     * wordToPdf
     *
     * @param file 上次文件
     * @return 结果
     */
    @PostMapping("wordToPdf")
    public AjaxResult wordToPdf(@RequestParam("file") MultipartFile file) throws IOException {
        FileUtil.checkSuffix(FileSuffixEnum.DOCX, file.getOriginalFilename());
        String downloadUrl = pdfTransferService.wordToPdf(file.getInputStream());
        return AjaxResult.success(downloadUrl);
    }

    /**
     * wordToPdf
     *
     * @param file 上次文件
     * @return 结果
     */
    @PostMapping("pdfToFile")
    public AjaxResult pdfToFile(@RequestParam("file") MultipartFile file, @RequestParam("targetType") String targetType) throws IOException {
        List<String> urls = pdfTransferService.pdfToFile(file.getInputStream(), targetType);
        return AjaxResult.success(urls.get(0));
    }
}
