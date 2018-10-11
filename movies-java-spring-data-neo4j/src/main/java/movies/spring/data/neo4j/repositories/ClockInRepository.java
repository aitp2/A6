package movies.spring.data.neo4j.repositories;

import movies.spring.data.neo4j.digital.domain.ClockIn;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RepositoryRestResource(collectionResourceRel = "ClockIn", path = "ClockIn")
public interface ClockInRepository extends Neo4jRepository<ClockIn, Long> {
    ClockIn findByObjId(String objId);

}