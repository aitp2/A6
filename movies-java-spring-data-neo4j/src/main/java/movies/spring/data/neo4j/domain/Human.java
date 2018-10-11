package movies.spring.data.neo4j.domain;

import movies.spring.data.neo4j.domain.relationship.Played;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jianfei.yin
 * @create 2018-08-10 1:40 PM
 **/
@NodeEntity
public class Human {
    @Id
    @GeneratedValue
    private Long id;

    @Index(unique = true)
    private String name;

    private Integer mentioned;

    @Relationship(type = "PLAYED",direction = "INCOMING")
    private List<Played> playeds;

    public List<Played> getPlayeds() {
        return playeds;
    }

    public void setPlayeds(List<Played> playeds) {
        this.playeds = playeds;
    }
    public void addPlayed(Played played){
        if(played == null){
            playeds = new ArrayList<>();
        }
        playeds.add(played);
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

    public void addMentioned(){
        this.mentioned = this.mentioned+1;
    }

    @Override
    public String toString() {
        return "Human{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mentioned=" + mentioned +
                ", playeds=" + playeds +
                '}';
    }
}
