package com.nie.image2code.Services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import static com.nie.image2code.Misc.Constants.*;

@Slf4j
public class ImageToCode {
    public String mainCode(String imgBase64) throws IOException {
        log.info(WELCOME);
        ScanImg scanImg;
        FilterCode filterCode;
        CodeFormatter codeFormatter;
        Imag2GreyScale img2GreyScale = new Imag2GreyScale();

        //temp
        byte[] fileContent = FileUtils.readFileToByteArray(new File("D://testImg2.jpg"));
        String encodedString = Base64.getEncoder().encodeToString(fileContent);

        //convert base64 to image and save
        byte[] decodedBytes = Base64.getDecoder().decode(imgBase64);
        FileUtils.writeByteArrayToFile(new File(OUTPUT_PATH), decodedBytes);

        //filtered code and formatted code
        String scannedCode = null;
        String filteredCode = null;
        String formattedCode = null;

        //resource paths
        img2GreyScale.convertToGreyScale(OUTPUT_PATH);

        //scan image stored in path
        scanImg = new ScanImg();
        filterCode = new FilterCode();
        codeFormatter = new CodeFormatter();

        //scan image
        scannedCode = scanImg.scanImage(CV_DESTINATION);
        //filter scanned code
        filteredCode = filterCode.getFilteredCode(scannedCode);

        //format code
        formattedCode = codeFormatter.formatCode(filteredCode);

        return formattedCode;
    }
}
