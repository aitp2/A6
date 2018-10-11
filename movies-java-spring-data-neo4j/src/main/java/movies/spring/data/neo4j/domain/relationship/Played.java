package movies.spring.data.neo4j.domain.relationship;

import movies.spring.data.neo4j.domain.Charater;
import movies.spring.data.neo4j.domain.Human;
import org.neo4j.ogm.annotation.*;

/**
 * @author jianfei.yin
 * @create 2018-08-14 10:13 AM
 **/
@RelationshipEntity(type = "PLAYED")
public class Played {
    @Id
    @GeneratedValue
    private Long id;

    private Integer mentioned;
    @EndNode
    private Human human;

    @StartNode
    private Charater character;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Human getHuman() {
        return human;
    }

    public void setHuman(Human human) {
        this.human = human;
    }

    public Charater getCharacter() {
        return character;
    }

    public void setCharacter(Charater character) {
        this.character = character;
    }

    public Integer getMentioned() {
        return mentioned;
    }

    public void setMentioned(Integer mentioned) {
        this.mentioned = mentioned;
    }
    public void addMentioned(){
        this.mentioned = this.mentioned==null?0:mentioned+1;
    }


}
