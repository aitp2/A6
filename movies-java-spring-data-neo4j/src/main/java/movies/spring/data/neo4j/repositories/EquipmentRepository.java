package movies.spring.data.neo4j.repositories;

import movies.spring.data.neo4j.domain.Equipment;
import movies.spring.data.neo4j.domain.Game;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RepositoryRestResource(collectionResourceRel = "equipment", path = "equipment")
public interface EquipmentRepository extends Neo4jRepository<Equipment, Long> {
    Equipment findByName(String name);

}