package ro.alexportfolio.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ro.alexportfolio.backend.dao.IssueRepository;
import ro.alexportfolio.backend.dao.ProjectRepository;
import ro.alexportfolio.backend.dao.UserRepository;

@SpringBootTest
class BackendApplicationTests {

	@MockBean
    private IssueRepository issueRepository;

	@MockBean
    private ProjectRepository projectRepository;

	@MockBean
    private UserRepository userRepository;

	@Test
	void contextLoads() {
		
	}

}
