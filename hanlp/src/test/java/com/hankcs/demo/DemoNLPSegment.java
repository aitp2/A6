/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/12/7 19:13</create-date>
 *
 * <copyright file="DemoNLPSegment.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package com.hankcs.demo;

import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.hankcs.hanlp.utility.TestUtility;

/**
 * NLP分词，更精准的中文分词、词性标注与命名实体识别。
 * 语料库规模决定实际效果，面向生产环境的语料库应当在千万字量级。欢迎用户在自己的语料上训练新模型以适应新领域、识别新的命名实体。
 * 标注集请查阅 https://github.com/hankcs/HanLP/blob/master/data/dictionary/other/TagPKU98.csv
 * 或者干脆调用 Sentence#translateLabels() 转为中文
 *
 * @author hankcs
 */
public class DemoNLPSegment extends TestUtility
{
    public static void main(String[] args)
    {
        System.out.println(NLPTokenizer.segment("世界杯决赛夜，巅峰对决！让我们一起见证这荣耀时刻")); // “正确”是副形词。
        // 注意观察下面两个“希望”的词性、两个“晚霞”的词性
        System.out.println(NLPTokenizer.analyze("美蛙王子的干锅鸡翅超级好吃！找人一起拼午饭").translateLabels());
        System.out.println(NLPTokenizer.analyze("美蛙王子的干锅鸡翅超级好吃！找人一起拼午饭"));
    }
}
