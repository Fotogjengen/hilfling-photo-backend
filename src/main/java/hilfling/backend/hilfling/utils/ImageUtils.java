package hilfling.backend.hilfling.utils;

import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtils {

    public static ByteArrayInputStream resizeImage(MultipartFile orginalFile, Integer size) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage resizedImage = null;
        BufferedImage img = ImageIO.read(orginalFile.getInputStream());
        resizedImage = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, size, Scalr.OP_ANTIALIAS);
        ImageIO.write(resizedImage, orginalFile.getContentType().split("/")[1] , baos);
        byte[] bytes = baos.toByteArray();
        return new ByteArrayInputStream(bytes);
    }
}
