package com.adelaide.scripts;

import java.io.*;
import java.util.*;

/**
 * @author Wenping(Deb) Du
 */

public class SplitString {
    public static void main(String args[]){
//        new SplitString().getMobSFString("/Users/deb/IdeaProjects/2020ai/tutorial/src/mobile/probmatic.txt");
//        SplitString ss = new SplitString();
//        String path = "/Users/deb/IdeaProjects/2020ai/tutorial/src/mobile/oldreport/1008.xml";
//        String out = ss.getFlowDroidString("/Users/deb/IdeaProjects/2020ai/tutorial/src/mobile/oldreport/1008.xml");
//        ss.writeFile(out, path);

        new SplitString().readFolder("/Users/deb/IdeaProjects/2020ai/tutorial/src/mobile/oldreport");
    }

    /**
     * For Tool-MOBSF 提取所有字符串
     */
    public String getMobSFString(String path){
        //read content
        String content = readFile("/Users/deb/IdeaProjects/2020ai/tutorial/src/mobile/probmatic.txt");
        String[] splits = content.split("[\\s\\\\//;,.\"_()]+");
        Set<String> stringSet = new HashSet<>(Arrays.asList(splits));
        String out = "";
        for (String str : stringSet) {
            System.out.println(str);
            out += str + "\n";
        }
        return out;
    }

    /**
     * For Tool-FlowDroid 提取所有方法
     */
    public String getFlowDroidString(String path){
        String content = readFile(path);

        String[] splits = content.split("[()]+");
        Set<String> stringSet = new HashSet<>();
        for (String s: splits){
            if(s.contains("&lt")){
                String[] ss = s.split(" ");
                String func = ss[ss.length - 1];
//                    System.out.println(func);
                stringSet.add(func);
            }
        }
        System.out.println(stringSet.toString());
        String out = "";
        for (String str : stringSet) {
            out += str + "\n";
        }
        return out;
    }

    /**
     * 读取文件
     * @param path
     * @return
     */
    public String readFile(String path){
        String content = "";
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(path))));
            String lineTxt;
            while((lineTxt = br.readLine())!= null){
                content += lineTxt + "\n";
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 写文件
     * @param str
     * @param path
     */
    public void writeFile(String str, String path){
        try{
            String[] n = path.split("/");
            File writename = new File(n[n.length - 1] + ".txt");
            writename.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write(str);
            out.flush();
            out.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    /**
     * 批量读文件夹并写出对应文件的function组合
     * @param path
     */
    public void readFolder(String path){
        try {
            File targetFiles = new File(path);
            if(targetFiles.isDirectory()){
                File[] fa = targetFiles.listFiles();
                if(fa!= null && fa.length > 0){

                    for (File file : fa) {
                        SplitString ss = new SplitString();
                        String out = ss.getFlowDroidString(file.getAbsolutePath());
                        if(out != null && out.trim() != ""){
                            ss.writeFile(out, file.getAbsolutePath());
                        }

                    }
                }
            }
        } catch (Exception e) {
        }
    }
}
