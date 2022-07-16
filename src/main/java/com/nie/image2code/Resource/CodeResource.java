package com.nie.image2code.Resource;

import com.nie.image2code.Domain.Result;
import com.nie.image2code.Services.ImageToCode;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.util.StringUtil;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import static com.nie.image2code.Misc.Constants.*;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/v1")
@Slf4j
public class CodeResource {
    ImageToCode imageToCode = new ImageToCode();
    boolean responseFlag = false;
    String encode;
    @PostMapping("/send-code")
    public void processCode(@RequestBody String code) throws IOException {
        log.error("received string is {}",code);
        String result = imageToCode.mainCode(code);
        log.error("send result as response {}",result);
        encode = Base64.getEncoder().encodeToString(result.getBytes());
        log.error("encoded result {}",encode);
        if(!StringUtil.isEmpty(encode))
        {
            responseFlag = true;
        }
    }

    @GetMapping("/scan")
    public String toUser()
    {
        while(!responseFlag)
        {
            log.error("waiting for image to process...");
        }
        return encode;
    }
}
