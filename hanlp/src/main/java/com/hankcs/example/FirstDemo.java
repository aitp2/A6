package com.hankcs.example;

import com.hankcs.example.util.JDBCUtil;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jianfei.yin
 * @create 2018-07-31 10:29 AM
 **/
public class FirstDemo {

    public static void main(String[] args) {

        List<HashMap<String,String>> resultList=new ArrayList<>();
        try ( Connection conn = JDBCUtil.getConn("cluster")){
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from POST_CATEGORY ORDER by category");

            JDBCUtil.extract(resultSet);

        Map<String, List<HashMap<String, String>>> collect = resultList.stream().collect(Collectors.groupingBy(m -> m.get("category")));




//
// System.out.println(HanLP.segment("自动缓存的目的是为了加速词典载入速度"));
//        List<Term> termList = HanLP.segment("商品和服务");
//        System.out.println(termList);
            CustomDictionary.add("狼人杀");
            CustomDictionary.add("王者荣耀");
            CustomDictionary.add("咖啡");
            CustomDictionary.add("吃鸡");
            CustomDictionary.add("开黑");
            CustomDictionary.add("欧莱雅");
            CustomDictionary.add("洗面奶");
            CustomDictionary.add("微信");

            Iterator<Map.Entry<String, List<HashMap<String, String>>>> iterator = collect.entrySet().iterator();

            StringBuilder content=new StringBuilder();
            while (iterator.hasNext()) {
                Map.Entry<String, List<HashMap<String, String>>> next = iterator.next();
                List<HashMap<String, String>> values = next.getValue();
                for(HashMap<String,String> v:values){
                    content.append(v.get("key_word").replaceAll("'","")).append(" ");
                }
                List<String> keywordList = HanLP.extractKeyword(content.toString(), 2);

                statement.execute("update POST_CATEGORY set tag='"+keywordList.toString()+"' WHERE category="+next.getKey());

                content.delete(0,content.length());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
////
//
//
//        String content = "咖啡 运费 成团 咖啡 分享 全场 运费 拼";
//        List<String> keywordList = HanLP.extractKeyword(content, 2);
//        System.out.println(keywordList);
//        List<Term> termList = NLPTokenizer.segment("中国科学院计算技术研究所的宗成庆教授正在教授自然语言处理课程");
//       System.out.println(termList);
//        List<Term> termList = IndexTokenizer.segment("主副食品");
//
//        for (Term term : termList)
//        {
//            System.out.println(term + " [" + term.offset + ":" + (term.offset + term.word.length()) + "]");
//        }
//        List<Term> termList = TraditionalChineseTokenizer.segment("大衛貝克漢不僅僅是名著名球員，球場以外，其妻為前辣妹合唱團成員維多利亞·碧咸，亦由於他擁有突出外表、百變髮型及正面的形象，以至自己品牌的男士香水等商品，及長期擔任運動品牌Adidas的代言人，因此對大眾傳播媒介和時尚界等方面都具很大的影響力，在足球圈外所獲得的認受程度可謂前所未見。");
//        System.out.println(termList);
//        String text = "江西鄱阳湖干枯，中国最大淡水湖变成大草原";
//        System.out.println(SpeedTokenizer.segment(text));
//        long start = System.currentTimeMillis();
//        int pressure = 1000000;
//        for (int i = 0; i < pressure; ++i)
//        {
//            SpeedTokenizer.segment(text);
//        }
//        double costTime = (System.currentTimeMillis() - start) / (double)1000;
//        System.out.printf("分词速度：%.2f字每秒", text.length() * pressure / costTime);
//        Segment nShortSegment = new NShortSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
//        Segment shortestSegment = new ViterbiSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
//        String[] testCase = new String[]{
//            "刘喜杰石国祥会见吴亚琴先进事迹报告团成员",
//        };
//        for (String sentence : testCase) {
//            System.out.println("N-最短分词：" + nShortSegment.seg(sentence) + "\n最短路分词：" + shortestSegment.seg(sentence));
//
//        }
//        Segment segment = new CRFSegment();
//        segment.enablePartOfSpeechTagging(true);
//        List<Term> termList = segment.seg("你看过穆赫兰道吗");
//        System.out.println(termList);
//        for (Term term : termList)
//        {
//            if (term.nature == null)
//            {
//                System.out.println("识别到新词：" + term.word);
//            }
//        }
//        System.out.println(NLPTokenizer.segment("我新造一个词叫一银框你能识别并标注正确词性吗？"));

    }
}
