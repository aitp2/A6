package com.hankcs.example.meituan.util;

import com.hankcs.hanlp.classification.classifiers.NaiveBayesClassifier;
import com.hankcs.hanlp.classification.models.NaiveBayesModel;
import com.hankcs.hanlp.utility.TestUtility;

import java.io.*;

/**
 * @author jianfei.yin
 * @create 2018-08-11 10:40 AM
 **/
public class SentimentUtil {

    public static final String CORPUS_FOLDER = TestUtility.ensureTestData("resturants", "");

    public static void trainNaiveBayes(String folder,String outputFile){
      TestUtility.ensureTestData(folder,"");
        NaiveBayesClassifier classifier = new NaiveBayesClassifier();
        File file =new File(outputFile);
        try {
            classifier.train(CORPUS_FOLDER);                     // 训练后的模型支持持久化，下次就不必训练了
            NaiveBayesModel model = (NaiveBayesModel) classifier.getModel();
            try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(file))) {
                oo.writeObject(model);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static NaiveBayesModel getModel(String filePath){
        File file = new File(filePath);
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            try {
                return (NaiveBayesModel) ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
