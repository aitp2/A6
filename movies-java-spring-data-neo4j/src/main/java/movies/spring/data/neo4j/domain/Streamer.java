package movies.spring.data.neo4j.domain;

import movies.spring.data.neo4j.domain.relationship.Played;
import movies.spring.data.neo4j.domain.relationship.Streaming;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jianfei.yin
 * @create 2018-08-10 1:40 PM
 **/
@NodeEntity
public class Streamer extends Human{

    @Relationship(type = "STREAMING",direction = "INCOMING" )
    private Streaming streaming;

    public Streaming getStreaming() {
        return streaming;
    }

    public void setStreaming(Streaming streaming) {
        this.streaming = streaming;
    }

    @Override
    public String toString() {
        return super.toString()+"Streamer{" +
                "streaming=" + streaming +
                '}';
    }
}
