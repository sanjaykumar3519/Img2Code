package com.nie.image2code.Services;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import static com.nie.image2code.Misc.Constants.*;

public class CodeFormatter {
    public String formatCode(String uCode) throws IOException {
        URL url = new URL(CODE_FORMATTER_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type","application/json");
        String code = uCode.toString().replace("\"","\\\"");
        String reqBody = CODE_FORMATTER_REQUEST_BODY.replace("{{code}}",code);
        httpURLConnection.setDoOutput(true);

        //send request
        OutputStream outputStream = httpURLConnection.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        outputStreamWriter.write(reqBody);
        outputStreamWriter.flush();
        outputStreamWriter.close();
        outputStream.close();
        httpURLConnection.connect();

        //receive response
        String result;
        BufferedInputStream bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int r = bufferedInputStream.read();
        while(r!=-1)
        {
            byteArrayOutputStream.write((byte)r);
            r = bufferedInputStream.read();
        }
        result = byteArrayOutputStream.toString();
        //Get formatted code path
        JSONObject jsonObject = new JSONObject(result);
        return jsonObject.getString("codeDst");
    }
}
