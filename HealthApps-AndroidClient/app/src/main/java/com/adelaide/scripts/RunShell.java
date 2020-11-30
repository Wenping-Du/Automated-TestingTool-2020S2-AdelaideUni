package com.adelaide.scripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Wenping(Deb) Du
 */

public class RunShell {
    /**
     * apk 路径
     */
    public static final String path = "/Users/deb/IdeaProjects/2020ai/tutorial/src/apk/";
    /**
     * cmd命令集合
     */
    public static final String[] cmds = {"adb devices",
                                        "cd " + path,
                                        "adb install ",
                                        "adb logcat > "};

    public static void main(String[] args){

        File targetFiles = new File(path);
        if(targetFiles.isDirectory()){
            File[] fa = targetFiles.listFiles();
            if(fa!= null && fa.length > 0){

                for (File file : fa) {
                    // file: ends with .apk
                    if(file.isFile() && file.getName().endsWith(".apk")){

                        String line = null;
                        StringBuilder sb = new StringBuilder();
                        try{

                            for(int i = 0; i < cmds.length; i ++){
                                String cmd = cmds[i];

                                if(i == 2){
                                    cmd += path + file.getName();
                                }
                                if(i == 3){
                                    cmd += path + "out/" + file.getName().replace(".apk", ".txt");
                                }
                                System.out.println(cmd);
                                /**
                                 * 运行cmd shell
                                 */
                                Process process = Runtime.getRuntime().exec(cmd);
                                BufferedReader bufferedReader = new BufferedReader
                                        (new InputStreamReader(process.getInputStream()));
                                while ((line = bufferedReader.readLine()) != null) {
                                    sb.append(line + "\n");
                                    System.out.println(line);
                                }
                            }
                        }catch(IOException e){
                            System.out.println(e.toString());
                            e.printStackTrace();

                        }
                    }
                }
            }
        }
    }
}
