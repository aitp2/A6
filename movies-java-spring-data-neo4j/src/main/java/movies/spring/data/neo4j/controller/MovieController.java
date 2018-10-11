package movies.spring.data.neo4j.controller;

import java.util.Collection;
import java.util.Map;

import movies.spring.data.neo4j.domain.Movie;
import movies.spring.data.neo4j.domain.Person;
import movies.spring.data.neo4j.domain.Role;
import movies.spring.data.neo4j.repositories.MovieRepository;
import movies.spring.data.neo4j.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RestController
@RequestMapping("/")
public class MovieController {

	private final MovieService movieService;
	@Autowired
    MovieRepository repository;
	public MovieController(MovieService movieService) {
		this.movieService = movieService;
	}

    @GetMapping("/graph")
	public Map<String, Object> graph(@RequestParam(value = "limit",required = false) Integer limit) {
		return movieService.graph(limit == null ? 100 : limit);
	}
	@PostMapping("/graph")
	public void createGraph(){
        Movie movie=new Movie("jidushan",1,"bojue");
        Person person=new Person("jianfei",1989);
        Role role = new Role(movie,person);
        role.addRoleName("zhuyan");
        movie.addRole(role);
        repository.save(movie);
    }
    @DeleteMapping("/graph/{id}")
    public void delete(@PathVariable Long id){
	    Movie movie=new Movie();
	    movie.setId(id);
	    repository.delete(movie);
    }
}
