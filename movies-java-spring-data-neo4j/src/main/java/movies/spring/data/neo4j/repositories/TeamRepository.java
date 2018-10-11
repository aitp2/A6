package movies.spring.data.neo4j.repositories;

import movies.spring.data.neo4j.domain.Movie;
import movies.spring.data.neo4j.domain.Team;
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
@RepositoryRestResource(collectionResourceRel = "teams", path = "teams")
public interface TeamRepository extends Neo4jRepository<Team, Long> {
    Team findByName(String name);

}