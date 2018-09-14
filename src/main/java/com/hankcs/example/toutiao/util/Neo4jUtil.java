package com.hankcs.example.toutiao.util;

import org.neo4j.driver.v1.*;

import java.util.*;

/**
 * @author jianfei.yin
 * @create 2018-08-10 12:24 PM
 **/
public class Neo4jUtil {
    public static void main(String[] args) {
        Driver driver= connect();
        Session session = driver.session();

        session.run( "CREATE (a:Person {name: {name}, title: {title}})",
            parameters( "name", "Arthur", "title", "King" ) );

        StatementResult result = session.run( "MATCH (a:Person) WHERE a.name = {name} " +
                "RETURN a.name AS name, a.title AS title",
            parameters( "name", "Arthur" ) );
        while ( result.hasNext() )
        {
            Record record = result.next();
            System.out.println( record.get( "title" ).asString() + " " + record.get( "name" ).asString() );
        }

        session.close();
        driver.close();
    }
    public static Map<String,Object> parameters(String... param){
        Map<String,Object> parameters = new HashMap<>();
        for(int index=0;index<param.length;index++){
            if(index%2==0){
                parameters.put(param[index],param[index+1]);
            }
        }
        return parameters;
    }
    public static Driver connect(){
        return GraphDatabase.driver("bolt://192.168.56.181:7687");
    }

}
