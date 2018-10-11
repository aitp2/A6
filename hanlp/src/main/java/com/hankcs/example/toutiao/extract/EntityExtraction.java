package com.hankcs.example.toutiao.extract;

import com.google.gson.Gson;
import com.hankcs.example.util.JDBCUtil;
import com.hankcs.example.toutiao.util.HttpUtil;
import com.hankcs.example.util.SegmentUtil;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * @author jianfei.yin
 * @create 2018-08-09 10:58 AM
 **/
public class EntityExtraction {
    public static final HashMap<String,String> SYNONYM_MAP = new HashMap();

    static {
        SYNONYM_MAP.put("LOL","英雄联盟");
        SYNONYM_MAP.put("绝地求生","吃鸡");
        SYNONYM_MAP.put("刺激战场","吃鸡");
        SYNONYM_MAP.put("王者农药","王者荣耀");
        SYNONYM_MAP.put("DNF","地下城与勇士");
        SYNONYM_MAP.put("黄金大奖赛","黄金联赛");
        SYNONYM_MAP.put("MDS剑仙","剑仙");
        SYNONYM_MAP.put("CF","穿越火线");

    }


    private static final String QUERY_SQL = "select id,content from news order by id";
    public static void main(String[] args) {
        List<String> games=Arrays.asList(games());
        List<String> roles = Arrays.asList(roles());
        List<String> teams = Arrays.asList(teams());
        List<String> equipments = Arrays.asList(equipments());
        List<String> zhubos = Arrays.asList(zhubos());
        List<String> matchs = Arrays.asList(matchs());
        customerGames(games);
        customerRoles(roles);
        customerTeams(teams);
        customerEquipments(equipments);
        customerZhubos(zhubos);
        customerMatchs(matchs);
//        extractEntity(games,roles,teams,equipments,zhubos,matchs);
        extractPlayed(roles,zhubos);
    }

    public static void extractPlayed(List<String> roles,List<String> zhubos){
        Segment segment = SegmentUtil.nshort();
        try (Connection conn = JDBCUtil.getConn("cluster")) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(QUERY_SQL);
            List<Map<String,String>> resultList= JDBCUtil.extract(resultSet);
            for(Map<String,String> item: resultList) {
                if (item.containsKey("content") && item.get("content").length() > 5) {
                    String content = item.get("content").toUpperCase();
                    Set<Map.Entry<String, String>> entries = SYNONYM_MAP.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        content = content.replaceAll(entry.getKey(), entry.getValue());
                    }
                    List<Term> terms = segment.seg(content);
                    terms.stream().forEach(term -> {
                        if(term.nature.toString().equals("nr")){
                            if(zhubos.indexOf(term.word.toUpperCase())>=0){
                                Optional<List<String>> strings = roleContained(terms, roles);
                                if(strings.isPresent()){
                                    Map<String,Object> params=new HashMap<>();
                                    params.put("streamerName",term.word.toUpperCase());
                                    params.put("names", new Gson().toJson(strings.get()));
                                    HttpUtil.post("http://localhost:8080/played",params);
                                }
                            }
                        }
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Optional<List<String>> roleContained(List<Term> terms,List<String> roles){
        List<String> roleContained= new ArrayList<>();
        terms.stream().forEach(term -> {
            if(term.nature.toString().equals("nrf")){
                String hero=term.word.toUpperCase().replaceAll(" ","");
                if(roles.indexOf(hero)>=0){
                    roleContained.add(hero);
                }
            }
        });
        return roleContained.isEmpty()?Optional.empty(): Optional.of(roleContained);
    }
    //实体抽取
    private static void extractEntity(List<String> games,List<String> roles,List<String> teams,
                               List<String> equipments,List<String> zhubos,List<String> matchs){
        Segment segment = SegmentUtil.nshort();
        try (Connection conn = JDBCUtil.getConn("cluster")) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(QUERY_SQL);
            List<Map<String,String>> resultList= JDBCUtil.extract(resultSet);
            for(Map<String,String> item: resultList){
                if(item.containsKey("content")&&item.get("content").length()>5){
                    String content = item.get("content").toUpperCase();
                    Set<Map.Entry<String, String>> entries = SYNONYM_MAP.entrySet();
                    for(Map.Entry<String, String> entry:entries){
                        content = content.replaceAll(entry.getKey(),entry.getValue());
                    }
                    List<Term> terms = segment.seg(content);
                    terms.stream().forEach(term -> {
                        if(term.nature.toString().equals("nr") || term.nature.toString().equals("l") || term.nature.toString().equals("nrf")
                            ||term.nature.toString().equals("nt") || term.nature.toString().equals("nz") || term.nature.toString().equals("nr2")){

                            if(term.nature.toString().equals("nr")){
                                if(zhubos.indexOf(term.word.toUpperCase())>=0){
                                    HttpUtil.post("streamer",term.word.toUpperCase().replaceAll(" ",""));
                                }else{
                                    HttpUtil.post("human",term.word.toUpperCase().replaceAll(" ",""));
                                }

                            }
                            if(term.nature.toString().equals("l")){
                                String equipment= term.word.toUpperCase().replaceAll(" ","");
                                if(equipments.indexOf(equipment)>=0){
                                    HttpUtil.post("equipment",term.word.toUpperCase().replaceAll(" ",""));
                                }
                            }

                            // NER
                            if(term.nature.toString().equals("nrf")){
                                String hero=term.word.toUpperCase().replaceAll(" ","");
                                if(roles.indexOf(hero)>=0){
                                    HttpUtil.post("hero",hero);
                                }
                            }
                            if(term.nature.toString().equals("nt")){
                                String team=term.word.toUpperCase().replaceAll(" ","");
                                if(teams.indexOf(team)>=0){
                                    HttpUtil.post("team",team);
                                }
                            }
                            if(term.nature.toString().equals("nz")){
                                String match=term.word.toUpperCase().replaceAll(" ","");
                                if(matchs.indexOf(match)>=0){
                                    HttpUtil.post("match",match);
                                }
                            }
                            if(term.nature.toString().equals("nr2")){
                                String game=term.word.toUpperCase().replaceAll(" ","");
                                if(games.indexOf(game)>=0){
                                    HttpUtil.post("game",game);
                                }
                            }
                        }
                    });
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void customerGames(List<String > games){
        games.forEach(w->CustomDictionary.add(w,"nr2 1"));

    }
    public static void customerRoles(List<String > roles){
        roles.forEach(ro ->{
            CustomDictionary.add(ro,"nrf 1");
        });

    }
    public static void customerEquipments(List<String > equipments){
        equipments.forEach(e->{
            CustomDictionary.add(e,"l 1");
        });

    }
    public static void customerTeams(List<String > teams){
        teams.forEach(t->{
            CustomDictionary.add(t,"nt 1");
        });
    }

    public static void customerZhubos(List<String > zhubos){
        zhubos.forEach(z->{
            CustomDictionary.add(z,"nr 10");
        });


    }
    public static void customerMatchs(List<String > matchs){
        matchs.forEach(m->{
            CustomDictionary.add(m,"nz 1");
        });
    }


    public static String[] games(){
        //"秒选","入榜","新版本","首秀","虐汪","体验服",,"黄金联赛","S11","S12","S10",
        String[] words= {"植物大战僵尸","我的世界","98K","穿越火线","荒野行动","王者荣耀","英雄联盟","绝地求生","旅行青蛙","迷你世界","辐射4",
            "第五人格","逆水寒","剑网","LOL","战神","DOTA","刺激战场","吃鸡","梦幻西游","DNF","绝地求生","CF","三国杀","哈希世界","区块链","魔兽争霸3",
            "空调骑士","碧蓝航线","莲花迷宫","不可思议的幻想乡","失落城堡","狂野飙车","业余游玩","天龙八部","炉石传说","QQ飞车","尼尔 机械军团",
            "荒野大镖客","东方梦符祭","魔兽世界","坦克世界","RPG","梦幻西游","阴阳师","东归英雄传","堡垒之夜","麻将","神武",
            "大话西游","守望先锋","拳皇","方舟","皇室战争","红色警戒","黎明杀机","三国志","GTA","冰汽时代","地下城与勇士"};
        return words;
    }

    public static String[] roles(){

        String[] wangzhe = {"诸葛亮","安其拉","白起","不知火舞","妲己","狄仁杰","典韦","韩信","老夫子","刘邦","干将莫邪","刘禅","鲁班七号","墨子","孙膑","孙尚香","孙悟空","项羽","亚瑟","周瑜","庄周","蔡文姬","甄姬","廉颇","程咬金","后羿","扁鹊","大乔","钟无艳","小乔","王昭君","虞姬","李元芳","张飞","刘备","牛魔","张良","兰陵王","露娜","貂蝉","达摩","曹操","芈月","荆轲","高渐离","钟馗","花木兰","关羽","李白","宫本武藏","吕布","嬴政","娜可露露","武则天","赵云","姜子牙"};
        String[] lol = {"凯隐","","洛","","霞","","卡蜜尔","","艾翁","","克烈","","塔莉垭","","索尔","","烬","","俄洛伊","","千珏","","塔姆","","艾克","","巴德","","雷克赛","","卡莉丝塔","","阿兹尔","","纳尔","","布隆"," ","维克兹"," ","亚索"," ","金克丝","","卢锡安"," ","亚托克斯","","丽桑卓"," ","扎克"," ","奎因"," ","锤石"," ","蔚"," ","娜美"," ","劫"," ","伊莉丝"," ","卡'兹克"," ","辛德拉"," ","雷恩加尔"," ","黛安娜"," ","婕拉","","杰斯","","德莱文"," ","德莱厄斯"," ","赫卡里姆"," ","韦鲁斯"," ","璐璐"," ","菲奥娜","","诺提勒斯"," ","瑟庄妮"," ","吉格斯"," ","维克托","","阿狸","","沃利贝尔","","菲兹","","特朗德尔"," ","拉克丝"," ","布兰德","","希瓦娜","","格雷福斯"," ","泽拉斯","","图奇","","锐雯","","泰隆","","斯卡纳","","孙悟空","","蕾欧娜","","约里克","","瑞兹","","卡特琳娜","","裟娜","","斯维因","","乐芙兰","","艾瑞莉娅","","卡西奥佩娅","","凯特琳","","雷克顿","","卡尔玛","","茂凯","","嘉文四世","","魔腾","","李青","","兰博","","薇恩","","奥莉安娜","","科'加斯","","努努","","阿木木","","维迦","","辛吉德","","泰达米尔","","蒙多","","普朗克","","墨菲特","","希维尔","","费德提克","","凯尔","","艾尼维亚","","提莫","","艾希","","安妮","","崔丝塔娜","","贾克斯","","易","","库奇","","阿利斯塔","","布里茨","","索拉卡","","内瑟斯","","沃里克","","莫甘娜","","拉莫斯","","卡萨丁","","赛恩","","塔里克","","迦娜","","基兰","","崔斯特"," ","伊芙琳","","卡尔萨斯","","萨科","","黑默丁格","","阿卡丽","","乌迪尔","","盖伦","","凯南","","克格'莫","","玛尔扎哈","","莫德凯撒","","奈德丽","","伊泽瑞尔","","古拉加斯","","奥拉夫","","潘森","","波比","","慎","","赵信","","弗拉基米尔","","加里奥","","厄加特","","厄运小姐"};

//        List<String> list = Arrays.asList(wangzhe);

        return wangzhe;
    }
    public static String[] equipments(){
        String[] wangzhe = {"黑色战斧","闪电匕首","三圣之力","无尽刀锋","泣血之刃","破甲弓","破灭君主","破魔刀","凶残之力","狂暴双刃","日蚀","风暴巨剑","吸血之镰","搏击拳套","匕首","铁剑","破军","影刃","逐日之弓","咒术典籍","时之预言","折磨面具","巫术法杖","冰霜法杖","回响之杖","博学者之怒","虚无法杖","炽热之拥","圣杯","进化水晶","小丑面罩","光辉之剑","血族之书","大棒","圣者法典","炼金护符","蓝宝石","贤者之书","反伤刺甲","贤者的庇护","寒冰之心","魔女面纱","不祥之兆","振兴之铠","霸者重装","军团荣耀","红莲斗篷","守护者之铠","雪山圆盾","神隐斗篷","熔炼之心","力量腰带","生命宝珠","抗魔披风","布甲","红玛瑙","暴烈之甲","影忍之足","急速战靴","神速之靴","抵抗之靴","冷静之靴","秘法之靴","游击弯刀","狩猎宽刃","巡守利斧","追击刀锋","符文大剑","巨人之握","贪婪之噬"};
        String[] lol= {"活力夹心饼干","附魔：欢欣","附魔：喧哗","附魔：失真","附魔：家园守卫","附魔：统帅","生命药水","法力药水","侦查守卫","真视守卫","智慧合剂","坚韧药剂","神谕精粹","洞察之石","洞察红宝石","水晶瓶","多兰之戒","多兰之刃","多兰之盾","风暴大剑","巨人腰带","蓝水晶","反曲之弓","十字镐","格斗手套","速度之靴","爆裂魔杖","增幅典籍","吸血鬼节杖","长剑","短剑","仙女护符","抗魔斗篷","布甲","锁子甲","红水晶","负极斗篷","治疗宝珠","无用大棒","骨牙项链","猎人宽刃刀","精魄之石","灵巧披风","基克的使徒","探索者护臂","狂热","守护天使","狂徒铠甲","瑞莱的水晶节杖","死刑宣告","恶魔法典","守护者铠甲","无尽之刃","荆棘之甲","智慧末刃","阿塔玛战戟","梅贾的窃魂卷","饮血剑","轻灵之靴","圣杯","净蚀","残暴之刃","军团圣盾","女神之泪","水银之靴","冰川护甲","提亚玛特","麦瑞德之爪","日炎斗篷","催化神石","鬼索的狂暴之刃","冥火之拥","疾行之靴","深渊权杖","蜂刺","狂战士胫甲","忍者足具","法师之靴","耀光","最后的轻语","虚空法杖","水银饰带","贪婪之刃","神秘之剑","幽魂面具","海克斯科技左轮枪","比尔吉沃特弯刀","燃烧宝石","海克斯饮魔刀","明朗之靴","中娅的沙漏","灭世者的死亡之帽","卢安娜的飓风","号令之旗","神圣之剑","冰霜之锤","巫妖之祸","时光之杖","三相之力","纳什之牙","女妖面纱","大天使法杖","冰霜之心","振奋铠甲","幻影之舞","黑色切割者","幽梦之灵","兰顿之兆","海克斯科技枪","远古意志","瑞格之灯","魔宗","莫雷洛的邪术秘典","钢铁烈阳之匣","雅典娜的邪恶圣杯","玛莫提乌斯之噬","破败王者之刃","蜥蜴长者之精魄","远古魔像之精魄","破碎幽魂之精魄","魔切","米克尔的坩锅","灵风","双生暗影","水银弯刀","冰脉护手","斯塔缇克电刃","炽天使之拥","贪欲九头蛇","兰德里的折磨","干扰水晶","上古钱币","偷猎者的匕首"};
        return wangzhe;
    }
    public static String[] teams(){
        String[] wangzhe={"eStarPro","BA黑凤梨","QGhappy","KZ","GO","EDG.M","RNG.M","AHQ","Hero","XQ","EMC","IMT","JC","GK","YTK","M8H"};
        return wangzhe;
    }
    public static String[] zhubos(){
        String[] wangzhe = {"骚白","剑仙","李太白","MDS剑仙","嗨氏","蓝烟","张大仙","韩跑跑","小浪浪","触手","孤影"};
        return wangzhe;
    }
//    public static Map<String,String[]> teamMember(){
//        String[] GK = {"老帅"};
//
//    }
    public static String[] matchs(){
        String[] wangzhe={"KPL"};
        return wangzhe;
    }

}
