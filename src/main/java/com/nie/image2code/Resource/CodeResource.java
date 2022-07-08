package com.nie.image2code.Resource;

import com.nie.image2code.Domain.Result;
import com.nie.image2code.Services.ImageToCode;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.nie.image2code.Misc.Constants.*;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/v1")
@Slf4j
public class CodeResource {
    ImageToCode imageToCode = new ImageToCode();
    @PostMapping("/send-code")
    public String processCode(@RequestBody String code) throws IOException {
        log.error("received string is {}",code);
        String result = imageToCode.mainCode(code);
        log.error("send result as response {}",result);
        String encode = Base64.getEncoder().encodeToString(result.getBytes());
        log.error("encoded result {}",encode);
        return encode;
    }
}
