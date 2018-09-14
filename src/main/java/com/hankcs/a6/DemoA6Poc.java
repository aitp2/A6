/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>me@hankcs.com</email>
 * <create-date>16/3/14 AM11:49</create-date>
 *
 * <copyright file="DemoCustomNature.java" company="码农场">
 * Copyright (c) 2008-2016, 码农场. All Right Reserved, http://www.hankcs.com/
 * This source is subject to Hankcs. Please contact Hankcs to get more information.
 * </copyright>
 */
package com.hankcs.a6;

import com.hankcs.example.aitp.domain.Tag;
import com.hankcs.example.aitp.util.HttpUtil;
import com.hankcs.example.util.JDBCUtil;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.classification.classifiers.IClassifier;
import com.hankcs.hanlp.classification.classifiers.NaiveBayesClassifier;
import com.hankcs.hanlp.classification.models.NaiveBayesModel;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.mining.word2vec.Word2VecTrainer;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import com.hankcs.hanlp.utility.LexiconUtility;
import com.hankcs.hanlp.utility.TestUtility;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.hankcs.hanlp.corpus.tag.Nature.n;

/**
 * 基于HanLP demo 文本-> 标签 POC
 */
public class DemoA6Poc
{
	/**
	 * 文本分类
	 */
    public static final String CORPUS_FOLDER_CLASSIFIER = TestUtility.ensureTestData("搜狗文本分类语料库迷你版", "http://hanlp.linrunsoft.com/release/corpus/sogou-text-classification-corpus-mini.zip");
    public static final String MODEL_PATH_CLASSIFIER = "data/test/classification-model.ser";
    
    /**
     * 词向量分析
     */
	private static final String TRAIN_FILE_NAME = TestUtility.ensureTestData("搜狗文本分类语料库已分词.txt", "http://hanlp.linrunsoft.com/release/corpus/sogou-mini-segmented.zip");
    private static final String MODEL_FILE_NAME = "data/test/word2vec.txt";
	
    /**
     * 情感分析
     */
    public static final String CORPUS_FOLDER_SENTIMENT = TestUtility.ensureTestData("ChnSentiCorp情感分析酒店评论", "http://hanlp.linrunsoft.com/release/corpus/ChnSentiCorp.zip");
    public static final String MODEL_PATH_SENTIMENT = "data/test/sentiment-model.ser";
    
    public static void main(String[] args)throws IOException
    {
    	Map<String,List<String>> labelMap = new HashMap<String,List<String>>();
    	List<A6Text> listA6Text= ReadCsv.getTextList();  	
         
    	for(A6Text a6Text:listA6Text) {
//    		if(baseDecoder(a6Text.getNickName().replaceAll("\"", "")).equals("Harvey")) {
    			if(labelMap.get(a6Text.getId()) == null) {
        			labelMap.put(a6Text.getId(), getLabel(a6Text.getText()));
        		}else {
        			labelMap.get(a6Text.getId()).addAll(getLabel(a6Text.getText()));
        		}
//    		}
    		
    	}
    	
    	//去重
    	for(String nickname:labelMap.keySet()) {
    		removeDuplicate(labelMap.get(nickname)); 
    	}
    	
    	int objId = 1;
    	for(String key:labelMap.keySet()) {
//			System.out.println(baseDecoder(key.replaceAll("\"", "")) +"tag：");
    		for(String tag:labelMap.get(key)) {
//    			System.out.print(tag+"  ");
    	        try (Connection conn = JDBCUtil.getConn("dlif2")) {
    	            Tag tags = new Tag();
    	            tags.setTitle(tag);
    	            tags.setObjId(String.valueOf(objId));
    	            HttpUtil.post("Http://localhost:8080/api/tag",tags);
    	            HttpUtil.get("http://localhost:8080/api/tag/by/"+key.replaceAll("\"", "")+"/"+String.valueOf(objId));
    	            objId++;
    	        } catch (Exception e) {
    	            e.printStackTrace();
    	        }
    		}
    		System.out.println("\n");
    	}
    	
    	
//    	List<String> contentlist = new ArrayList<String>();
//    	contentlist.add("每天一杯咖啡，清醒一整天☕");
//    	
//    	 for (String ct : contentlist)
//         {
//    		 getLabel(ct);
//         }
    }
    
    public static List<String> getLabel(String content) throws IOException{
    	List<String> returnlist = new ArrayList<String>();
    	System.out.println(content);
    	
//    	System.out.println("----------------------------分词提取开始");
    	//将文本进行分词与词性标注，获取名词
        StandardTokenizer.SEGMENT.enablePartOfSpeechTagging(true);  // 依然支持隐马词性标注
        List<Term> termList = HanLP.segment(content);
//        System.out.println("文本分词与词性标注："+termList);
        List<String> list_segment_n = new ArrayList<String>();
        for (Term term : termList)
        {
        	for(Nature nature:getUsefulNature()) {
        		if (term.nature == nature)
                {
        			list_segment_n.add(term.word);
//                    System.out.printf("找到了 [%s] : %s\n", "名词", term.word);
                }
        	}
        }
//        System.out.println("----------------------------分词提取结束");
        
        //关键词提取
//        System.out.println("----------------------------关键词提取开始");
//        List<String> keywordList = HanLP.extractKeyword(content, 10);
//        System.out.println("关键词："+keywordList);
//        System.out.println("----------------------------关键词提取结束");
        
//        WordVectorModel wordVectorModel = trainOrLoadModel();
        
        //根据获取名称与关键词确认标签词
//        System.out.println("----------------------------标签词提取开始");
        List<String> list_tag = new ArrayList<String>();
        for(String seg:list_segment_n) {
//        	for(String keyword:keywordList) {
//        		if(seg.equals(keyword)) {
//        			System.out.println("标签词："+seg);
        			if(seg.length()>1) {
        				list_tag.add(seg);
        			}
//        			List<String> list_near = printNearest(keyword, wordVectorModel);
//        			list_tag.add(seg);
//        			list_tag.addAll(list_near);
//        		}
//        	}
        }
//        System.out.println("----------------------------标签词提取结束");
        
//        //文本分类，获取分类标签
//        System.out.println("----------------------------文本分类开始");
//        IClassifier classifier = new NaiveBayesClassifier(trainOrLoadModel_classifier());
//        predict_classifier(classifier, content);
//        System.out.println("----------------------------文本分类结束");
        //加入情感分析
//        System.out.println("----------------------------文本情感开始");
//        IClassifier sentiment = new NaiveBayesClassifier(trainOrLoadModel_sentiment()); // 创建分类器，更高级的功能请参考IClassifier的接口定义
//        predict_sentiment(sentiment, content);
//        System.out.println("----------------------------文本情感结束");
        
        String senti = "";
//        if(sentiment.classify(content).equals("负面")) {
//        	senti = "讨厌";
//        }
//        if(sentiment.classify(content).equals("正面")) {
//        	senti = "喜欢";
//        }
        for(String tag:list_tag) {
        	returnlist.add(senti+tag);
        }
//        returnlist.add(senti+classifier.classify(content));
//        System.out.println("*********************************************************************************");
//        System.out.println(content+" 标签：");
//        for(String label:returnlist) {
//        	System.out.println(label);
//        }
//        System.out.println("*********************************************************************************");
        return returnlist;
    }
    
    
    public static List<Nature> getUsefulNature(){
    	List<Nature> list = new ArrayList<Nature>();
    	list.add(Nature.fromString("n"));
    	list.add(Nature.fromString("nba"));
    	list.add(Nature.fromString("nr"));
    	list.add(Nature.fromString("nbp"));
    	list.add(Nature.fromString("nf"));
    	list.add(Nature.fromString("nh"));
    	list.add(Nature.fromString("nhd"));
    	list.add(Nature.fromString("nhm"));
    	list.add(Nature.fromString("nm"));
    	list.add(Nature.fromString("ns"));
    	list.add(Nature.fromString("vi"));
    	return list;
    }
    
    public static void predict_classifier(IClassifier classifier, String text)
    {
        System.out.printf("《%s》 属于分类 【%s】\n", text, classifier.classify(text));
    }
    
    public static NaiveBayesModel trainOrLoadModel_classifier() throws IOException
    {
        NaiveBayesModel model = (NaiveBayesModel) IOUtil.readObjectFrom(MODEL_PATH_CLASSIFIER);
        if (model != null) return model;

        File corpusFolder = new File(CORPUS_FOLDER_CLASSIFIER);
        if (!corpusFolder.exists() || !corpusFolder.isDirectory())
        {
            System.err.println("没有文本分类语料，请阅读IClassifier.train(java.lang.String)中定义的语料格式与语料下载：" +
                                   "https://github.com/hankcs/HanLP/wiki/%E6%96%87%E6%9C%AC%E5%88%86%E7%B1%BB%E4%B8%8E%E6%83%85%E6%84%9F%E5%88%86%E6%9E%90");
            System.exit(1);
        }

        IClassifier classifier = new NaiveBayesClassifier(); // 创建分类器，更高级的功能请参考IClassifier的接口定义
        classifier.train(CORPUS_FOLDER_CLASSIFIER);                     // 训练后的模型支持持久化，下次就不必训练了
        model = (NaiveBayesModel) classifier.getModel();
        IOUtil.saveObjectTo(model, MODEL_PATH_CLASSIFIER);
        return model;
    }
    
    public static NaiveBayesModel trainOrLoadModel_sentiment() throws IOException
    {
        NaiveBayesModel model = (NaiveBayesModel) IOUtil.readObjectFrom(MODEL_PATH_SENTIMENT);
        if (model != null) return model;

        File corpusFolder = new File(CORPUS_FOLDER_SENTIMENT);
        if (!corpusFolder.exists() || !corpusFolder.isDirectory())
        {
            System.err.println("没有情感语料，请阅读IClassifier.train(java.lang.String)中定义的语料格式与语料下载：" +
                                   "https://github.com/hankcs/HanLP/wiki/%E6%96%87%E6%9C%AC%E5%88%86%E7%B1%BB%E4%B8%8E%E6%83%85%E6%84%9F%E5%88%86%E6%9E%90");
            System.exit(1);
        }

        IClassifier classifier = new NaiveBayesClassifier(); // 创建分类器，更高级的功能请参考IClassifier的接口定义
        classifier.train(CORPUS_FOLDER_SENTIMENT);                     // 训练后的模型支持持久化，下次就不必训练了
        model = (NaiveBayesModel) classifier.getModel();
        IOUtil.saveObjectTo(model, MODEL_PATH_SENTIMENT);
        return model;
    }
    
    private static void predict_sentiment(IClassifier classifier, String text)
    {
        System.out.printf("《%s》 情感极性是 【%s】\n", text, classifier.classify(text));
    }

    static
    {
        File corpusFolder = new File(CORPUS_FOLDER_SENTIMENT);
        if (!corpusFolder.exists() || !corpusFolder.isDirectory())
        {
            System.err.println("没有文本分类语料，请阅读IClassifier.train(java.lang.String)中定义的语料格式、准备语料");
            System.exit(1);
        }
    }
    
    
    static WordVectorModel trainOrLoadModel() throws IOException
    {
        if (!IOUtil.isFileExisted(MODEL_FILE_NAME))
        {
            if (!IOUtil.isFileExisted(TRAIN_FILE_NAME))
            {
                System.err.println("语料不存在，请阅读文档了解语料获取与格式：https://github.com/hankcs/HanLP/wiki/word2vec");
                System.exit(1);
            }
            Word2VecTrainer trainerBuilder = new Word2VecTrainer();
            return trainerBuilder.train(TRAIN_FILE_NAME, MODEL_FILE_NAME);
        }

        return loadModel();
    }

    static WordVectorModel loadModel() throws IOException
    {
        return new WordVectorModel(MODEL_FILE_NAME);
    }
    
    static List<String> printNearest(String word, WordVectorModel model)
    {
    	List<String> nearWordList = new ArrayList<String>();
        for (Map.Entry<String, Float> entry : model.nearest(word))
        {
            //System.out.printf("%50s\t\t%f\n", entry.getKey(), entry.getValue());
        	if(entry.getValue() > 0.5) {
        		nearWordList.add(entry.getKey());
        	}
        }
        return nearWordList;
    }
    
    public static String baseDecoder(String encodeText){
        final Base64.Decoder decoder = Base64.getDecoder();
        String decodeData=encodeText;
        try {
            decodeData=new String(decoder.decode(encodeText), "UTF-8");
        } catch (Exception e) {
        }
        return decodeData;
    }
    
    public static String baseEncoder(String text){
        final Base64.Encoder encoder = Base64.getEncoder();
        String encoderData=text;
        try {
            final byte[] textByte = text.getBytes("UTF-8");
            encoderData=encoder.encodeToString(textByte);
        } catch (Exception e) {
        }
        return encoderData;
    }
    
    
    public static List removeDuplicate(List list) {   
        HashSet h = new HashSet(list);   
        list.clear();   
        list.addAll(h);   
        return list;   
    }   
}
