//package com.wzy.bp.util;
//
//import io.xjar.boot.XBoot;
//
//import java.io.File;
//
//public class XjarUtils {
//
//    public static void main(String[] args) {
//        String password = "123456";
//        File plaintext = new File("C:\\HOLYSHT\\CodingSht\\wzy\\AIYun\\apimonitor\\target\\apimonitor-0.0.1-SNAPSHOT.jar");
//        File encrypted = new File("C:\\HOLYSHT\\CodingSht\\wzy\\AIYun\\apimonitor\\target\\apimonitor-en.jar");
//        File decrypted = new File("C:\\HOLYSHT\\CodingSht\\wzy\\AIYun\\apimonitor\\target\\apimonitor-de.jar");
//
//        try {
//            XBoot.encrypt(plaintext, encrypted, password);
//            XBoot.decrypt(encrypted, decrypted, password);
//        } catch (Exception e) {
//
//
//        }
//    }
//}
