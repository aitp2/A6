package com.hankcs.a6;

import java.io.BufferedReader;  
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List; 

public class ReadCsv {
	public static void main(String[] args) {  
        try {  
            BufferedReader reader = new BufferedReader(new FileReader("data/test/a6_text.csv"));//换成你的文件名 
            reader.readLine();//第一行信息，为标题信息，不用,如果需要，注释掉 
            String line = null;
            while((line=readLine(reader))!=null){  
                String item[] = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分 
                System.out.println(item[0]); 
                System.out.println(item[1]); 
                System.out.println(item[2]); 
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
	
	 public static List<A6Text> getTextList(){
		 try {  
	            BufferedReader reader = new BufferedReader(new FileReader("data/test/a6_text.csv"));//换成你的文件名 
	            reader.readLine();//第一行信息，为标题信息，不用,如果需要，注释掉 
	            String line = null;
	            List<A6Text> list = new ArrayList<A6Text>();
	            while((line=readLine(reader))!=null){  
	                String item[] = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分 
	                A6Text a6Text = new A6Text();
	                a6Text.setNickName(item[2]);
	                a6Text.setId(item[1]);
	                a6Text.setText(item[0]);
	                list.add(a6Text);
	            }  
	            
	            return list;
	        } catch (Exception e) {  
	            e.printStackTrace();  
	            return null;
	        }  
	 }

    public static String readLine(BufferedReader reader) throws Exception {
 
        StringBuffer readLine = new StringBuffer();
        boolean bReadNext = true;
 
        while (bReadNext) {
            //
            if (readLine.length() > 0) {
                readLine.append("\r\n");
            }
            // 一行
            String strReadLine = reader.readLine();
 
            // readLine is Null
            if (strReadLine == null) {
                return null;
            }
            readLine.append(strReadLine);
 
            // 如果双引号是奇数的时候继续读取。考虑有换行的是情况。
            if (countChar(readLine.toString(), '"', 0) % 2 == 1) {
                bReadNext = true;
            } else {
                bReadNext = false;
            }
        }
        return readLine.toString();
    }
    
    private static int countChar(String str, char c, int start) {
        int i = 0;
        int index = str.indexOf(c, start);
        return index == -1 ? i : countChar(str, c, index + 1) + 1;
    }


}
