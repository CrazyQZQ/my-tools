package com.lxqq.tools.common.handler.file.utils;

import com.google.common.base.Joiner;
import com.lxqq.tools.common.ServiceException;
import com.lxqq.tools.common.handler.file.FileSuffixEnum;
import org.apache.commons.io.FilenameUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author QinQiang
 * @date 2023/9/14
 **/
public class FileUtil {
    public static void checkSuffix(FileSuffixEnum fileSuffixEnum, String fileName) {
        String suffix = FilenameUtils.getExtension(fileName);
        if (!fileSuffixEnum.suffix.equalsIgnoreCase(suffix)) {
            throw new ServiceException("只支持" + fileSuffixEnum.suffix + "类型的文件!");
        }
    }

    /**
     * @param fileSuffixEnums
     * @param fileName
     */
    public static void checkSuffix(List<FileSuffixEnum> fileSuffixEnums, String fileName) {
        String suffix = FilenameUtils.getExtension(fileName);
        boolean pass = fileSuffixEnums.stream().anyMatch(fileSuffixEnum -> fileSuffixEnum.suffix.equalsIgnoreCase(suffix));
        if (!pass) {
            List<String> list = fileSuffixEnums.stream().map(e -> e.suffix).collect(Collectors.toList());
            throw new ServiceException("只支持" + Joiner.on("、").join(list) + "类型的文件!");
        }
    }
}
