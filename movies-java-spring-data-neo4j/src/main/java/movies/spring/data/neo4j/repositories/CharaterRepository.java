package movies.spring.data.neo4j.repositories;

import movies.spring.data.neo4j.domain.Charater;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RepositoryRestResource(collectionResourceRel = "gameRoles", path = "gameRoles")
public interface CharaterRepository extends Neo4jRepository<Charater, Long> {
    Charater findByName(String name);

}