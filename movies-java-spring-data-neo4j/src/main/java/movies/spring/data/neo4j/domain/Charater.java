package movies.spring.data.neo4j.domain;

import org.neo4j.ogm.annotation.*;

/**
 * @author jianfei.yin
 * @create 2018-08-10 1:38 PM
 **/
@NodeEntity
public class Charater {
    @Id
    @GeneratedValue
    private Long id;
    @Index(unique = true)
    private String name;
    private Integer mentioned;

    @Relationship(type = "ACTED_AS")
    private Game game;

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

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void addMentioned(){
        this.mentioned = this.mentioned+1;
    }

    @Override
    public String toString() {
        return "Hero{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mentioned=" + mentioned +
                ", game=" + game +
                '}';
    }
}
