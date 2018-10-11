package com.hankcs.example.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author jianfei.yin
 * @create 2018-08-14 8:56 AM
 **/
public class StopWord {

    public static HashMap<String,Integer> stopwords(String path) {
        HashMap<String,Integer> stopwords= new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String tmp;
            while ((tmp=reader.readLine())!=null){
                stopwords.put(tmp,null);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stopwords;
    }
}
