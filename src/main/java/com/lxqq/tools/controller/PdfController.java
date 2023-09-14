package com.lxqq.tools.controller;

import com.lxqq.tools.common.domain.AjaxResult;
import com.lxqq.tools.pdf.service.PdfTransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public AjaxResult imgToPdf(@RequestParam("file") MultipartFile file) {
        try {
            String downloadUrl = pdfTransferService.imgToPdf(file.getInputStream());
            return AjaxResult.success(downloadUrl);
        } catch (IOException e) {
            log.error("转换失败");
            return AjaxResult.error("转换失败");
        }
    }

    /**
     * wordToPdf
     *
     * @param file 上次文件
     * @return 结果
     */
    @PostMapping("wordToPdf")
    public AjaxResult wordToPdf(@RequestParam("file") MultipartFile file) {
        try {
            String downloadUrl = pdfTransferService.wordToPdf(file.getInputStream());
            return AjaxResult.success(downloadUrl);
        } catch (IOException e) {
            log.error("转换失败");
            return AjaxResult.error("转换失败");
        }

    }
}
