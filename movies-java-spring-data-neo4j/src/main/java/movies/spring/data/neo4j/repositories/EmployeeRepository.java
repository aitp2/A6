package movies.spring.data.neo4j.repositories;

import movies.spring.data.neo4j.digital.domain.ClockIn;
import movies.spring.data.neo4j.digital.domain.Employee;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RepositoryRestResource(collectionResourceRel = "Employee", path = "Employee")
public interface EmployeeRepository extends Neo4jRepository<Employee, Long> {
    Employee findByObjId(String objId);
    Employee findByName(String name);
    
    @Query("MATCH (n:Employee) -[r:SIMILAR_TO]- (d:Employee) where n.objId={objId} RETURN n,r,d limit 10")
    List<Employee> findSimilar(@Param("objId")String objId);
}