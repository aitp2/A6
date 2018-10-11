package movies.spring.data.neo4j.domain.relationship;

import movies.spring.data.neo4j.domain.Game;
import movies.spring.data.neo4j.domain.Streamer;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jianfei.yin
 * @create 2018-08-13 3:55 PM
 **/
@RelationshipEntity(type = "STREAMING")
public class Streaming {
    @Id
    @GeneratedValue
    private Long id;
    private List<String> streamerNames = new ArrayList<>();

    @EndNode
    private Streamer streamer;

    @StartNode
    private Game game;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getStreamerNames() {
        return streamerNames;
    }

    public void setStreamerNames(List<String> streamerNames) {
        this.streamerNames = streamerNames;
    }

    public Streamer getStreamer() {
        return streamer;
    }

    public void setStreamer(Streamer streamer) {
        this.streamer = streamer;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void addStreamerName(String name){
        if(streamerNames == null){
            streamerNames = new ArrayList<>();
        }
        streamerNames.add(name);
    }

    @Override
    public String toString() {
        return "StreamerBy{" +
                "id=" + id +
                ", streamerNames=" + streamerNames +
                ", streamer=" + streamer +
                ", game=" + game +
                '}';
    }
}
