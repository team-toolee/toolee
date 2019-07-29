package toolee.tools.Repositories;

import org.springframework.data.repository.CrudRepository;
import toolee.tools.Models.Tool;

public interface ToolRepository extends CrudRepository<Tool, Long> {

}
