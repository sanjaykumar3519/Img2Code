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

@Slf4j
public class FilterCode {
    //variables in use
    StringBuilder finalCode = new StringBuilder();
    boolean closeBrace = false;

    //apply filter on scanned code to correct small errors
    public String getFilteredCode(String scannedCode) throws IOException {
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
                log.error("import method invoked");
                importBuilder(temp);
            }
            else if(temp.toLowerCase().contains("class"))
            {
                log.error("class method invoked");
                classBuilder(temp);
            }
            else if(temp.matches("(.*)(M?m?ain)(.*)"))
            {
                log.error("main method invoked");
                methodBuilder(temp);
            }
            else if(temp.matches("(s?S?.*)(print)(.*)"))
            {
                log.error("print method invoked");
                printBuilder(temp);
            }
            else if(temp.matches("(.*)(int|float|double|s?S?tring)(.*)"))
            {
                log.error("variable method invoked");
                variableBuilder(temp);
            }
            else if(temp.equals("3"))
            {
                closeBrace = true;
            }
        }
        finalCode.append("}");

        //return filtered code
        return finalCode.toString();
    }
    //import builder
    public void importBuilder(String s)
    {
        String importRegex = "(import\\s)([[a-z\\s]*.?]*)";
        Pattern pattern = Pattern.compile(importRegex);
        Matcher matcher = pattern.matcher(s);
        while(matcher.find())
        {
            finalCode.append(matcher.group(0)).append(";");
        }

        /*//enhanced validation
        StringBuilder result = new StringBuilder();
        if(s.contains("Import"))
            s = s.replace("Import","import");
        String[] sArray = s.split(" ");
        for(String value:sArray)
        {
            if(value.matches("(\\w*)(\\.)(.*)(;?)"))
            {
                StringBuilder iValue = new StringBuilder();
                String[] iArray = value.split("\\.");
                {
                    for(int i = 0; i < iArray.length; i++)
                    {
                        if(iArray[i].contains(";")||iArray[i].contains(":"))
                        {
                            StringBuilder iStarBuilder = new StringBuilder();
                            String[] iStar = iArray[i].split("");
                            for(String val:iStar)
                            {
                                if(val.equals(";") || val.equals(":"))
                                    iStarBuilder.append(";");
                                else if(val.length() == 1)
                                    iStarBuilder.append("*");
                                else
                                    iStarBuilder.append(val);
                            }
                            iArray[i] = iStarBuilder.toString();
                        }
                        iValue.append(iArray[i]);
                        if(i < iArray.length-1)
                            iValue.append(".");
                    }
                }
                value = iValue.toString();
            }
            result.append(value).append(" ");
        }*/
        //finalCode.append(result);
    }

    //class builder
    public void classBuilder(String s)
    {
        String classRegex = "([public\\s]?)(class\\s)([a-zA-Z]*)";
        Pattern pattern = Pattern.compile((classRegex));
        Matcher matcher = pattern.matcher(s);
        while(matcher.find())
        {
            finalCode.append(matcher.group(0)).append("{");
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
        if(closeBrace)
        {
            finalCode.append("}");
            closeBrace = false;
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
