package org.misterej.engine.util;


// TODO change to static class remove singleton

import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    private static Logger instance;

    public static Logger getInstance() {
        if(instance == null) instance = new Logger("log/log.txt");
        return instance;
    }

    private String filePath;
    private FileWriter fw;
    boolean working;

    private Logger(String filePath)
    {
        this.filePath = filePath;
        try{
            fw = new FileWriter(filePath);
            working = true;
        } catch (IOException e)
        {
            working = false;
            System.out.println(e.getMessage());
        }
    }

    public static void log(String message)
    {
        if(!getInstance().working) return;

        try
        {
            getInstance().fw.write("Time: '" + Time.getTime() + "' | ");
            getInstance().fw.write(message + "\n");
            getInstance().fw.flush();
        } catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static void logGL(int message)
    {
        if(!getInstance().working) return;
        if(message == 0) return;

        try
        {
            getInstance().fw.write("Time: '" + Time.getTime() + "' | ");
            getInstance().fw.write("opengl Error: '"+ message + "'\n");
            getInstance().fw.flush();
        } catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

}
