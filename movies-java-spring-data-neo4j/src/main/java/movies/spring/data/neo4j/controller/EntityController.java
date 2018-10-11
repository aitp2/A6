package movies.spring.data.neo4j.controller;

import com.google.gson.Gson;
import movies.spring.data.neo4j.domain.*;
import movies.spring.data.neo4j.domain.relationship.Hero;
import movies.spring.data.neo4j.domain.relationship.Played;
import movies.spring.data.neo4j.domain.relationship.Streaming;
import movies.spring.data.neo4j.repositories.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jianfei.yin
 * @create 2018-08-10 1:45 PM
 **/
@RestController
public class EntityController {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private CharaterRepository charaterRepository;
    @Autowired
    private HumanRepository humanRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private StreamerRepository streamerRepository;
    @Autowired
    private PersonRepository personRepository;
    @GetMapping("/clear")
    public void clear(@RequestParam String type){
        if(StringUtils.isNotBlank(type)){
            switch (type){
                case "game":
                    gameRepository.deleteAll();
                    break;
                case "hero":
                    charaterRepository.deleteAll();
                    break;
                case "human":
                    humanRepository.deleteAll();
                    break;
                case "team":
                    teamRepository.deleteAll();
                    break;
                case "equipment":
                    equipmentRepository.deleteAll();
                    break;
                case "match":
                    matchRepository.deleteAll();
                    break;
                case "streamer":
                    streamerRepository.deleteAll();
                    break;
                default:
                    break;
            }
            return;
        }
        gameRepository.deleteAll();
        humanRepository.deleteAll();
        charaterRepository.deleteAll();
        equipmentRepository.deleteAll();
        matchRepository.deleteAll();
        teamRepository.deleteAll();
        streamerRepository.deleteAll();
        personRepository.deleteAll();
  }
    @GetMapping("/save/{type}")
    public void createGame(@PathVariable String type,@RequestParam String name){
        switch (type){
            case "game":
                Game game = gameRepository.findByName(name);
                if(game==null){
                    game = new Game();
                    game.setMentioned(0);
                    game.setName(name);
                }
                game.addMentioned();
                gameRepository.save(game);
                break;
            case "hero":
                Charater charater = charaterRepository.findByName(name);
                if(charater ==null){
                    charater = new Charater();
                    charater.setName(name);
                    charater.setMentioned(0);
                }
                charater.addMentioned();
                charaterRepository.save(charater);
                break;
            case "human":
                Human human = humanRepository.findByName(name);
                if (human == null){
                    human = new Human();
                    human.setName(name);
                    human.setMentioned(0);
                }
                human.addMentioned();
                humanRepository.save(human);
                break;
            case "team":
                Team team = teamRepository.findByName(name);
                if (team == null){
                    team = new Team();
                    team.setName(name);
                    team.setMentioned(0);
                }
                team.addMentioned();
                teamRepository.save(team);
                break;
            case "equipment":
                Equipment equipment = equipmentRepository.findByName(name);
                if (equipment == null){
                    equipment = new Equipment();
                    equipment.setName(name);
                    equipment.setMentioned(0);
                }
                equipment.addMentioned();
                equipmentRepository.save(equipment);
                break;
            case "match":
                Match match= matchRepository.findByName(name);
                if (match == null){
                    match = new Match();
                    match.setName(name);
                    match.setMentioned(0);
                }
                match.addMentioned();
                matchRepository.save(match);
                break;
            case "streamer":
                Streamer streamer = streamerRepository.findByName(name);
                if (streamer == null){
                    streamer = new Streamer();
                    streamer.setMentioned(0);
                    streamer.setName(name);
                }
                streamer.addMentioned();
                streamerRepository.save(streamer);
                break;
            default:
                break;
        }
    }

    @GetMapping("/actedAs")
    public void addBelongTo(){
        Iterable<Charater> all = charaterRepository.findAll();
        Game game=gameRepository.findByName("王者荣耀");
        List<Hero> heroes = game.getHeroes() == null ? new ArrayList<>():game.getHeroes();
        all.forEach(charater -> {
            Hero hero=new Hero();
            hero.setGame(game);
            hero.setCharater(charater);
            heroes.add(hero);
        });
        game.setHeroes(heroes);
        gameRepository.save(game);
    }
    @GetMapping("/streaming")
    public void streaming(){
        Iterable<Streamer> all = streamerRepository.findAll();
        Game game=gameRepository.findByName("王者荣耀");
        all.forEach(streamer -> {
            if(streamer.getStreaming()!=null){
                return;
            }
            Streaming streaming = new Streaming();
            streaming.setGame(game);
            streaming.setStreamer(streamer);
            streamer.setStreaming(streaming);
            streamerRepository.save(streamer);
        });
    }
    @PostMapping("/played")
    public void played(@RequestParam String streamerName,@RequestParam String names){
        Gson gson=new Gson();
        List<String> heroNames = gson.fromJson(names,List.class);
        Streamer streamer = streamerRepository.findByName(streamerName);
        heroNames.stream().forEach(heroName ->{
            Charater hero = charaterRepository.findByName(heroName);
            List<Played> playeds = streamer.getPlayeds() == null ? new ArrayList<>() : streamer.getPlayeds();
            Played played = new Played();
            played.setMentioned(0);
            for(Played p:playeds){
                if(p.getCharacter().getName().equals(heroName)){
                    played = p;
                }
            }
            played.addMentioned();
            played.setHuman(streamer);
            played.setCharacter(hero);
            playeds.add(played);
            streamer.setPlayeds(playeds);
            streamerRepository.save(streamer);
        });
    }
    @PostMapping("/game")
    public void saveGame(@RequestParam String name){
        Game game = new Game();
        game.setName(name);
        game.setMentioned(0);
        gameRepository.save(game);
    }
    @GetMapping("/analyze")
    public ResponseEntity analyzeEntityRelation2Game(@RequestParam String name,@RequestParam String type){

        switch (type){
            case "game":
                Game game = gameRepository.findByName(name);
                if(game != null){
                    return ResponseEntity.ok().body(game);
                }
                break;
            case "hero":
                Game g = gameRepository.findHeroInGame(name);
                if (g != null){
                    return ResponseEntity.ok(g);
                }
                break;
            case "human":

                break;
            case "team":

                break;
            case "equipment":

                break;
            case "match":

                break;
            case "streamer":
                Game g2= gameRepository.findStreamer4Game(name);
                if (g2 != null){
                    return ResponseEntity.ok().body(g2);
                }
                break;
            default:
                break;
        }
        return null;
    }



}
