package com.nie.image2code.Services;

import lombok.extern.slf4j.Slf4j;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.nie.image2code.Misc.Constants.*;

@Slf4j
public class FilterCode {
    //variables in use
    StringBuilder finalCode = new StringBuilder();
    boolean classBraket = false;
    boolean mainMethodBraket = false;

    //apply filter on scanned code to correct small errors
    public String getFilteredCode(String scannedCode) throws IOException {
        //append default package
        finalCode.append(DEFAULT_PACKAGE);
        //save detected text
        Path path = Paths.get("C:\\Users\\sanja\\Downloads\\output.txt");
        assert scannedCode != null;
        byte[] bytes = scannedCode.getBytes();
        try{
            Files.write(path,bytes);
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        //filter code line by line
        FileReader fileReader = new FileReader(String.valueOf(path));
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String temp = null;
        while ((temp=bufferedReader.readLine())!=null)
        {
            if(temp.toLowerCase().matches("(i?I?mport)(.*)"))
            {
                log.error("import method invoked : {}",temp);
                importBuilder(temp);
            }
            else if(temp.toLowerCase().contains("class"))
            {
                log.error("class method invoked : {}",temp);
                classBuilder(temp);
            }
            else if(temp.matches("(.*)(M?m?ain)(.*)"))
            {
                log.error("main method invoked : {}",temp);
                methodBuilder(temp);
            }
            else if(temp.matches("(s?S?.*)(print)(.*)"))
            {
                log.error("print method invoked : {}",temp);
                printBuilder(temp);
            }
            else if(temp.matches("(.*)(int|float|double|s?S?tring)(.*)"))
            {
                log.error("variable method invoked : {}",temp);
                variableBuilder(temp);
            }
            /*else if(temp.equals("3"))
            {
                closeBrace = true;
            }*/
        }
        if(mainMethodBraket)
            finalCode.append("}");
        if(classBraket)
            finalCode.append("}");

        //return filtered code
        return finalCode.toString();
    }
    //import builder
    public void importBuilder(String s)
    {
        //make sure the string is small

        String importRegex = "(import\\s)([[a-z\\s]*.?]*)";
        Pattern pattern = Pattern.compile(importRegex);
        Matcher matcher = pattern.matcher(s);
        while(matcher.find())
        {
            finalCode.append(matcher.group(0)).append(";");
        }
    }

    //class builder
    public void classBuilder(String s)
    {
        String classRegex = "(public\\s)?(class\\s)([a-zA-Z]*)";
        Pattern pattern = Pattern.compile((classRegex));
        Matcher matcher = pattern.matcher(s);
        while(matcher.find())
        {
            log.error("class is {}",matcher.group(0));
            finalCode.append(matcher.group(0)).append("{");
            classBraket = true;
        }
    }

    //method builder
    public void methodBuilder(String s)
    {
        /*if(s.contains("string"))
        {
            s = s.replace("string","String");
        }
        //ready string
        StringBuilder readyString = new StringBuilder();
        //split main
        String[] mainArray = s.split(" ");
        for(int i = 0 ; i < mainArray.length; i++)
        {
            mainArray[i] = mainArray[i].toLowerCase();
            if(mainArray[i].contains("main"))
            {
                mainArray[i] = validateMainParam(mainArray[i],mainArray[i+1]);
            }
            readyString.append(mainArray[i]);
            if(i != mainArray.length-1)
            {
                readyString.append(" ");
            }
        }
        log.error("readyString is {}",readyString.toString());*/
        String methodRegex = "(public\\s)?(static\\s)?(void\\s)?(main\\(.*\\))";
        Pattern pattern = Pattern.compile(methodRegex);
        Matcher matcher = pattern.matcher(s.toString().replace("string","String"));
        while(matcher.find())
        {
            finalCode.append(matcher.group(0)).append("{");
            mainMethodBraket = true;
        }
    }

    public String validateMainParam(String s,String args)
    {
        StringBuilder result = new StringBuilder();
        s = s + " " + args;
        log.error("main plus args {}",s);
        s = s.replace("("," ");
        log.error("split main is {}",s);
        String[] pArray = s.split(" ");
        for(int i = 0; i < pArray.length; i++)
        {
            if(pArray[i].contains("String"))
                result.append("(").append(pArray[i]).append(" ");
            else
                result.append(pArray[i]);
            if(i == pArray.length-1)
            {
                result.append(")");
            }
        }
        log.error("main filter is {}",result.toString());
        return result.toString();
    }

    //print builder
    public void printBuilder(String s)
    {
        if(s.contains("system"))
        {
            s = s.replace("system","System");
        }
        String printRegex = "(System.out.println\\(.*\\))";
        Pattern pattern = Pattern.compile(printRegex);
        Matcher matcher = pattern.matcher(s);
        while(matcher.find())
        {
            finalCode.append(matcher.group(0)).append(";");
        }
    }

    //variable builder
    public void variableBuilder(String s)
    {
        if(s.contains("string"))
        {
            s = s.replace("string","String");
        }
        String variableRegex = "(static\\s)?(final\\s)?([a-zA-Z]*)\\s([a-zA-Z_]*)\\s?(=)\\s?([a-zA-Z\\d]*)";
        Pattern pattern = Pattern.compile(variableRegex);
        Matcher matcher = pattern.matcher(s);
        while(matcher.find())
        {
            finalCode.append(matcher.group(0)).append(";");
        }
    }
}
