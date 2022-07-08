package com.nie.image2code.Services;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ScanImg {
    public String scanImage(String path) throws IOException {

        //logger
        Logger logger = LoggerFactory.getLogger(ScanImg.class);
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(path));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        try {
            AnnotateImageRequest request =
                    AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
            requests.add(request);
        }
        catch (Exception e)
        {
            logger.error("Failed to connect google cloud vision");
        }

        String result = null;

        //detect text
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                }
                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                    result = annotation.getDescription();
                    break;
                }
            }
        }
        catch (Exception e)
        {
            logger.error("error in getting scanned image from google cloud vision");
        }
        log.error("scanned code is {}",result);
        return result;
    }
}
