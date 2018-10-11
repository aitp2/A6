package movies.spring.data.neo4j.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import movies.spring.data.neo4j.domain.relationship.Hero;
import movies.spring.data.neo4j.domain.relationship.HoldFor;
import movies.spring.data.neo4j.domain.relationship.OwnedGame;
import movies.spring.data.neo4j.domain.relationship.Streaming;
import org.neo4j.ogm.annotation.*;

import java.util.List;

/**
 * @author jianfei.yin
 * @create 2018-08-10 1:41 PM
 **/
@NodeEntity
public class Game {
    @Id
    @GeneratedValue
    private Long id;
    @Index(unique = true)
    private String name;

    private Integer mentioned;

    @JsonIgnoreProperties("game")
    @Relationship(type = "ACTED_AS",direction = "INCOMING")
    private List<Hero> heroes;

    @Relationship(type = "STREAMING")
    private List<Streamer> streamers;

    public void addMentioned(){
        this.mentioned = this.mentioned+1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMentioned() {
        return mentioned;
    }

    public void setMentioned(Integer mentioned) {
        this.mentioned = mentioned;
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    public void setHeroes(List<Hero> heroes) {
        this.heroes = heroes;
    }

    public List<Streamer> getStreamers() {
        return streamers;
    }

    public void setStreamers(List<Streamer> streamers) {
        this.streamers = streamers;
    }
}