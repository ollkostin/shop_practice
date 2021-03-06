package ru.practice.kostin.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.practice.kostin.shop.config.ImageConfig;
import ru.practice.kostin.shop.util.FileUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

import static ru.practice.kostin.shop.util.FileUtil.FILE_SEPARATOR;
import static ru.practice.kostin.shop.util.PhotoFileUtil.fileHasImageExtension;
import static ru.practice.kostin.shop.util.PhotoFileUtil.isSizeAllowed;

@Service
@RequiredArgsConstructor
public class FileService {
    private final ImageConfig imageConfig;

    /**
     * Saves image
     *
     * @param multipartFile file in in multipart/form-data format
     * @param productId     product id
     * @return path to file
     */
    String saveFile(MultipartFile multipartFile, Integer productId) {
        if (isSizeAllowed(multipartFile) && fileHasImageExtension(multipartFile)) {
            String prefix = imageConfig.getPrefix();
            return FileUtil.write(
                    multipartFile,
                    prefix + productId + "_" + multipartFile.hashCode(),
                    imageConfig.getDirectoryPath() + FILE_SEPARATOR + prefix + productId
            );
        }
        return null;
    }

    /**
     * Gets file from file system
     *
     * @param file     file
     * @param response http response
     */
    public void readFileIntoResponse(File file, HttpServletResponse response) {
        FileUtil.readFileIntoResponse(file, response);
    }

    /**
     * Sets content type, disposition and length into response headers
     *
     * @param file     file
     * @param response http response
     */
    public void setResponseContentImageHeaders(File file, HttpServletResponse response) {
        response.setHeader(HttpHeaders.CONTENT_TYPE, "image/jpeg");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + ".jpg\"");
        response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));
    }

    /**
     * Gets image by product id and name of the file
     *
     * @param productId product id
     * @param filename  name of the file
     * @return product image
     */
    public File getFileByProductIdAndFilename(Integer productId, String filename) {
        return new File(imageConfig.getDirectoryPath()
                + FILE_SEPARATOR
                + imageConfig.getPrefix()
                + productId
                + FILE_SEPARATOR
                + filename);
    }

    /**
     * Gets image placeholder.
     * Use for case when product image was not found
     *
     * @return placeholder image
     */
    public File getPlaceholderImage() {
        return new File(imageConfig.getDirectoryPath() + FILE_SEPARATOR + imageConfig.getPlaceholderName());
    }
}
