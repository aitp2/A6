package movies.spring.data.neo4j.domain.relationship;

import movies.spring.data.neo4j.domain.Game;
import movies.spring.data.neo4j.domain.Charater;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jianfei.yin
 * @create 2018-08-13 3:55 PM
 **/
@RelationshipEntity(type = "ACTED_AS")
public class Hero {
    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Charater charater;

    @EndNode
    private Game game;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Charater getCharater() {
        return charater;
    }

    public void setCharater(Charater charater) {
        this.charater = charater;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
