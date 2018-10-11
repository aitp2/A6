/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/12/9 13:55</create-date>
 *
 * <copyright file="DemoPhraseExtractor.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package com.hankcs.demo;

import com.hankcs.hanlp.HanLP;

import java.util.List;

/**
 * 短语提取
 * @author hankcs
 */
public class DemoPhraseExtractor
{
    public static void main(String[] args)
    {
        String text = "世界杯决赛夜，巅峰对决！让我们一起见证这荣耀时刻";
        List<String> phraseList = HanLP.extractPhrase(text, 5);
        System.out.println(phraseList);
    }
}
