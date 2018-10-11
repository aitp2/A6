package movies.spring.data.neo4j.repositories;

import movies.spring.data.neo4j.domain.Streamer;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RepositoryRestResource(collectionResourceRel = "webcasters", path = "webcasters")
public interface StreamerRepository extends Neo4jRepository<Streamer, Long> {
    Streamer findByName(String name);

}