package movies.spring.data.neo4j.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class readFileUtile {

	public static List<File> getFileList(String strPath) {
	        File dir = new File(strPath);
	        List<File> filelist = new ArrayList<File>();
	        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
	        if (files != null) {
	            for (int i = 0; i < files.length; i++) {
	                String fileName = files[i].getName();
	                if (files[i].isDirectory()) { // 判断是文件还是文件夹
	                    getFileList(files[i].getAbsolutePath()); // 获取文件绝对路径
	                } else if (fileName.endsWith("txt")) { // 判断文件名是否以.txt结尾
	                    String strFileName = files[i].getAbsolutePath();
	                    System.out.println("---" + strFileName);
	                    filelist.add(files[i]);
	                } else {
	                    continue;
	                }
	            }
	
	        }
	        return filelist;
	}
	
	public static Map<String,List<String>> readLine(String filrPath) {
		List<File> filelist = getFileList(filrPath);
		Map<String,List<String>> map= new HashMap<String,List<String>>();
		for(File file:filelist)
		{
		    BufferedReader reader = null;
		    String tempString = null;
		    int line =1;
		    List<String> contexts = new ArrayList<String>();
		    String name = file.getName().substring(0,file.getName().length()-4);
		    try {
		        System.out.println("以行为单位读取文件内容，一次读一整行：");
		        reader = new BufferedReader(new FileReader(file));
		        while ((tempString = reader.readLine()) != null) {
		            System.out.println("Line"+ line + ":" +tempString);
		            contexts.add(tempString);
		            line ++ ;
		        }
		        reader.close();
		        map.put(name, contexts);
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }finally{
		        if(reader != null){
		            try {
		                reader.close();
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
		        }
		    }
		}
		return map;
	}
}
