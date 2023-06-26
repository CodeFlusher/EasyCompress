package me.urbanfaust.easy_compress.util;

public class Logger {
    public static void message(String namespace, Object message){
        System.out.println("["+namespace+"]: "+message.toString());
    }
}
