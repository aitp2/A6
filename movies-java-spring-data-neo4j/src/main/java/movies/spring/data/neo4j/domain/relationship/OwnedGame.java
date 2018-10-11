package movies.spring.data.neo4j.domain.relationship;

import movies.spring.data.neo4j.domain.Game;
import movies.spring.data.neo4j.domain.Team;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jianfei.yin
 * @create 2018-08-13 5:09 PM
 **/
@RelationshipEntity(type = "OWNED_GAME")
public class OwnedGame {

    @Id
    @GeneratedValue
    private Long id;
    private List<String> teamNames= new ArrayList<>();

    @EndNode
    private Team team;

    @StartNode
    private Game game;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getTeamNames() {
        return teamNames;
    }

    public void setTeamNames(List<String> teamNames) {
        this.teamNames = teamNames;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void addTeamNames(String name){
        if(teamNames == null){
            teamNames = new ArrayList<>();
        }
        teamNames.add(name);
    }

    @Override
    public String toString() {
        return "OwnedGame{" +
                "id=" + id +
                ", teamNames=" + teamNames +
                ", team=" + team +
                ", game=" + game +
                '}';
    }
}
