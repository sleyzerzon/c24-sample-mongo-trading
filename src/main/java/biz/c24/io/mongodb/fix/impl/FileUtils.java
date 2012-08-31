package biz.c24.io.mongodb.fix.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static biz.c24.io.mongodb.fix.impl.C24StandardMessages.MANDATORY_ARGUMENT_MISSING;

/**
 * Produced on behalf of C24 Technologies Ltd.
 * 
 * @author Matt Vickery - matt.vickery@incept5.com
 * @since 31/08/2012
 */
public class FileUtils {

    public static String readClasspathResourceAsString(String resourceName) {

        Assert.isTrue(StringUtils.isNotEmpty(resourceName), MANDATORY_ARGUMENT_MISSING);
        try {
            File resourceFile = new ClassPathResource(resourceName).getFile();
            byte[] buffer = new byte[(int) resourceFile.length()];
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(resourceFile));
            bufferedInputStream.read(buffer);

            return new String(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static File readClasspathResourceAsFile(String resourceName) {

        Assert.isTrue(StringUtils.isNotEmpty(resourceName), MANDATORY_ARGUMENT_MISSING);
        try {
            return new ClassPathResource(resourceName).getFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
