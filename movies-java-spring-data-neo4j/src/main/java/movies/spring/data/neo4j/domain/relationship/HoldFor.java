package movies.spring.data.neo4j.domain.relationship;

import movies.spring.data.neo4j.domain.Game;
import movies.spring.data.neo4j.domain.Match;
import org.neo4j.ogm.annotation.*;

/**
 * @author jianfei.yin
 * @create 2018-08-13 3:55 PM
 **/
@RelationshipEntity(type = "HOLD_FOR")
public class HoldFor {
    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Match match;

    @EndNode
    private Game game;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public String toString() {
        return "HoldFor{" +
                "id=" + id +
                ", match=" + match +
                ", game=" + game +
                '}';
    }
}
