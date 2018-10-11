package movies.spring.data.neo4j.repositories;

import movies.spring.data.neo4j.domain.Game;
import movies.spring.data.neo4j.domain.Movie;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RepositoryRestResource(collectionResourceRel = "games", path = "games")
public interface GameRepository extends Neo4jRepository<Game, Long> {
    Game findByName(String name);

    @Query("MATCH (m:Game)<-[r:ACTED_AS]-(a:Charater) where a.name={heroName} RETURN m,r,a")
    Game findHeroInGame(@Param("heroName")String heroName );

    @Query("MATCH (m:Game)-[r:STREAMING]->(a:Streamer) where a.name={streamerName} RETURN m,r,a")
    Game findStreamer4Game(@Param("streamerName")String streamerName );
}