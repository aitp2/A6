package com.hankcs.example.util;

import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Segment;

/**
 * @author jianfei.yin
 * @create 2018-08-14 9:05 AM
 **/
public class SegmentUtil {

    public static Segment nshort(){
        Segment segment =new NShortSegment()
            .enablePlaceRecognize(true)
            .enableOrganizationRecognize(false)
            .enableTranslatedNameRecognize(true)
            .enableJapaneseNameRecognize(true)
            .enableNameRecognize(true)
            .enableAllNamedEntityRecognize(true)
            .enableNumberQuantifierRecognize(true)
            .enableCustomDictionary(true)
            .enableMultithreading(10);
        return segment;
    }
}
