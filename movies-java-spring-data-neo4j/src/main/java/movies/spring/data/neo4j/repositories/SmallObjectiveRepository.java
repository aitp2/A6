package movies.spring.data.neo4j.repositories;

import movies.spring.data.neo4j.digital.domain.SmallInvitation;
import movies.spring.data.neo4j.digital.domain.SmallObjective;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RepositoryRestResource(collectionResourceRel = "SmallObjective", path = "SmallObjective")
public interface SmallObjectiveRepository extends Neo4jRepository<SmallObjective, Long> {
    SmallObjective findByObjId(String objId);
}