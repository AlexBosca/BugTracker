package ro.alexportfolio.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import ro.alexportfolio.backend.dao.IssueRepository;
import ro.alexportfolio.backend.dao.ProjectRepository;
import ro.alexportfolio.backend.dao.UserProjectRoleRepository;
import ro.alexportfolio.backend.dao.UserRepository;

@Profile("test")
@ActiveProfiles("test")
@SpringBootTest
class BackendApplicationTests {

	@MockBean
    private IssueRepository issueRepository;

	@MockBean
    private ProjectRepository projectRepository;

	@MockBean
    private UserRepository userRepository;

	@MockBean
	private UserProjectRoleRepository userProjectRoleRepository;

	@Test
	void contextLoads() {
		
	}

}
