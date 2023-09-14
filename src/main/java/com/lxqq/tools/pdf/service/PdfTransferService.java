package com.lxqq.tools.pdf.service;

import cn.hutool.core.util.RandomUtil;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.lxqq.tools.common.constant.CommonConstant;
import com.lxqq.tools.common.handler.file.FileConstants;
import com.lxqq.tools.common.handler.file.FileProcessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @author QinQiang
 * @date 2023/9/13
 **/
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PdfTransferService {

    @Autowired
    private FileProcessHandler fileProcessHandler;

    /**
     * 图片转pdf
     * @param inputStream 输入流
     * @return 下载路径
     */
    public String imgToPdf(InputStream inputStream) {
        try {
            String pdfPath = getTempPdfPath();
            BufferedImage image = ImageIO.read(inputStream);
            PDDocument document = new PDDocument();
            PDPage page = new PDPage(new PDRectangle(image.getWidth(), image.getHeight()));
            document.addPage(page);
            PDImageXObject pdImage = LosslessFactory.createFromImage(document, image);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.drawImage(pdImage, 0, 0);
            contentStream.close();
            document.save(pdfPath);
            document.close();
            File tempFile = new File(pdfPath);
            String s = RandomUtil.randomString(12);
            String uploadUrl = fileProcessHandler.upload(tempFile, FileConstants.PDF_SUB_PATH, s + ".pdf");
            Files.delete(tempFile.toPath());
            log.info("imgToPdf successfully!");
            return uploadUrl;
        } catch (IOException e) {
            log.error("imgToPdf failed: ", e);
        }
        return "";
    }

    /**
     * word转pdf
     * @param inputStream 输入流
     * @return 下载路径
     */
    public String wordToPdf(InputStream inputStream) {
        try {
            String tempPdfPath = getTempPdfPath();
            Document doc = new Document(inputStream);
            doc.save(tempPdfPath, SaveFormat.PDF);
            File tempFile = new File(tempPdfPath);
            String s = RandomUtil.randomString(12);
            String uploadUrl = fileProcessHandler.upload(tempFile, FileConstants.PDF_SUB_PATH, s + ".pdf");
            Files.delete(tempFile.toPath());
            log.info("wordToPdf successfully!");
            return uploadUrl;
        } catch (Exception e) {
            log.error("wordToPdf failed: ", e);
        }
        return "";
    }

    /**
     * 生成pdf临时路径
     * @return pdf临时路径
     */
    private String getTempPdfPath() {
        return CommonConstant.TEMP_FOLD + "/" + RandomUtil.randomString(12) + "temp.pdf";
    }
}
