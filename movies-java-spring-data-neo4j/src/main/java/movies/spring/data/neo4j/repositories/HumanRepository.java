package movies.spring.data.neo4j.repositories;

import movies.spring.data.neo4j.domain.Human;
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
@RepositoryRestResource(collectionResourceRel = "human", path = "human")
public interface HumanRepository extends Neo4jRepository<Human, Long> {
    Human findByName(String name);

}