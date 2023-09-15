package com.lxqq.tools.pdf.service;

import cn.hutool.core.util.RandomUtil;
import com.aspose.pdf.devices.PngDevice;
import com.aspose.pdf.devices.Resolution;
import com.aspose.words.Document;
import com.aspose.words.HtmlSaveOptions;
import com.aspose.words.SaveFormat;
import com.lxqq.tools.common.constant.CommonConstant;
import com.lxqq.tools.common.handler.file.FileConstants;
import com.lxqq.tools.common.handler.file.FileProcessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

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
            log.info("imgToPdf successfully!");
            return upload(pdfPath);
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
            log.info("wordToPdf successfully!");
            return upload(tempPdfPath);
        } catch (Exception e) {
            log.error("wordToPdf failed: ", e);
        }
        return "";
    }

    private String upload(String tempPdfPath) throws IOException {
        File tempFile = new File(tempPdfPath);

        String s = RandomUtil.randomString(12);
        String suffix = FilenameUtils.getExtension(tempPdfPath);
        String uploadUrl = fileProcessHandler.upload(tempFile, FileConstants.PDF_SUB_PATH, s + "." + suffix);
        Files.delete(tempFile.toPath());
        return uploadUrl;
    }

    /**
     * PDF 转其他文件
     * @param inputStream
     * @param type
     * @return
     */
    public List<String> pdfToFile(InputStream inputStream, String type) {
        List<String> res = new ArrayList<>();
//        String checkType = FilenameUtils.getExtension(file.getOriginalFilename());
        try {
            switch (type.toUpperCase()) {
                case "WORD" : {
                    return convertFile(inputStream, com.aspose.pdf.SaveFormat.DocX, "docx");
                }
                case "XML" : {
                    return convertFile(inputStream, com.aspose.pdf.SaveFormat.PdfXml, "xml");
                }
                case "EXCEL" : {
                    return convertFile(inputStream, com.aspose.pdf.SaveFormat.Excel, "xlsx");
                }
                case "PPT" : {
                    return convertFile(inputStream, com.aspose.pdf.SaveFormat.Pptx, "pptx");
                }
                case "PNG" : {
                    // 图片类型的需要获取每一页PDF，一张一张转换
                    com.aspose.pdf.Document pdfDocument = new com.aspose.pdf.Document(inputStream);
                    //分辨率
                    Resolution resolution = new Resolution(130);
                    PngDevice pngDevice = new PngDevice(resolution);
                    for (int index = 0; index < pdfDocument.getPages().size(); index++) {
                        String filePath = CommonConstant.TEMP_FOLD + "/" + RandomUtil.randomString(12) + ".png";
                        File tmpFile = new File(filePath);
                        FileOutputStream fileOutputStream = new FileOutputStream(tmpFile);
                        pngDevice.process(pdfDocument.getPages().get_Item(index), fileOutputStream);
                        res.add(upload(filePath));
                        fileOutputStream.close();
                    }
                    return res;
                }
                case "HTML" : {
                    String filePath = CommonConstant.TEMP_FOLD + "/" + RandomUtil.randomString(12) + ".html";
                    com.aspose.pdf.Document doc = new com.aspose.pdf.Document(inputStream);
                    com.aspose.pdf.HtmlSaveOptions saveOptions = new com.aspose.pdf.HtmlSaveOptions();
                    saveOptions.setFixedLayout(true);
                    saveOptions.setSplitIntoPages(false);
                    saveOptions.setRasterImagesSavingMode(com.aspose.pdf.HtmlSaveOptions.RasterImagesSavingModes.AsExternalPngFilesReferencedViaSvg);
                    doc.save(filePath , saveOptions);
                    doc.close();
                    res.add(upload(filePath));
                    return res;
                }
                default:{}
            }
        } catch (Exception e) {
            log.error("pdf convert to other file error: ", e);
        }
        return null;
    }

    private List<String> convertFile(InputStream inputStream, com.aspose.pdf.SaveFormat saveFormat, String suffix) {
        List<String> resUrl = new ArrayList<>();
        try {
            long old = System.currentTimeMillis();
            // 输出路径
            String filePath = CommonConstant.TEMP_FOLD + "/" + RandomUtil.randomString(12) + "." + suffix;
            com.aspose.pdf.Document doc = new com.aspose.pdf.Document(inputStream);
            doc.save(filePath, saveFormat);
            doc.close();
            resUrl.add(upload(filePath));
            long now = System.currentTimeMillis();
            log.info("共耗时：" + ((now - old) / 1000.0) + "秒");
        }catch (IOException e) {
            log.error("pdf convert to other file error: ", e);
        }
        return resUrl;
    }

    /**
     * 生成pdf临时路径
     * @return pdf临时路径
     */
    private String getTempPdfPath() {
        File tempFold = new File(CommonConstant.TEMP_FOLD);
        if (!tempFold.exists()) {
            boolean mkdir = tempFold.mkdir();
            if (!mkdir) {
                log.error("创建临时文件夹失败!");
            }
        }
        return CommonConstant.TEMP_FOLD + "/" + RandomUtil.randomString(12) + "temp.pdf";
    }

    public static void main(String[] args) throws IOException {
        File pdf = new File("temp/1.pdf");
        com.aspose.pdf.Document doc = new com.aspose.pdf.Document(Files.newInputStream(pdf.toPath()));
        doc.save("temp/66.docx", com.aspose.pdf.SaveFormat.DocX);
    }
}
