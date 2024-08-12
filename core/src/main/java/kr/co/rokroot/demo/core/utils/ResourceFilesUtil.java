/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 01
 * All Rights Reserved
 */

package kr.co.rokroot.demo.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceFilesUtil {

    public static File getResourcesFile(String path, String suffix) throws IOException{
        InputStream input = new ClassPathResource(path).getInputStream();
        File file = File.createTempFile("temp", String.format(".%s", suffix));

        try (FileOutputStream output = new FileOutputStream(file)) {
            int read;
            byte[] bytes = new byte[1024];
            while ((read = input.read(bytes)) != -1) {
                output.write(bytes, 0, read);
            }
        }

        return file;
    }
}
