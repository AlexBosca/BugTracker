import { TestBed } from '@angular/core/testing';

import { ProjectService } from './project.service';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { Project } from '../models/project.model';

describe('ProjectService', () => {
  let service: ProjectService;
  let httpTesting: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(ProjectService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTesting.verify();
  });

  it('should create a new project', () => {
    const projectRequest = {
      projectKey: 'SWD',
      name: 'Software Development',
      description: 'Software Development Project'
    };

    let called = false;

    service.createProject(projectRequest).subscribe(() => {
      called = true;
    });

    const req = httpTesting.expectOne('http://localhost:8081/api/v1/bug-tracker/projects');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(projectRequest);

    req.flush(null);

    expect(called).toBe(true);
  });

  it('should fetch all projects', () => {
    let result: Project[] = [];

    service.getAllProjects().subscribe((projects) => {
      result = projects;
    });

    const req = httpTesting.expectOne('http://localhost:8081/api/v1/bug-tracker/projects');
    expect(req.request.method).toBe('GET');

    req.flush([
      {
        projectKey: 'SWD',
        name: 'Software Development',
        description: 'Software Development Project'
      },
      {
        projectKey: 'HRM',
        name: 'Human Resource Management',
        description: 'HRM Project'
      }
    ]);

    expect(result.length).toBe(2);
    expect(result[0].projectKey).toBe('SWD');
    expect(result[0].name).toBe('Software Development');
    expect(result[0].description).toBe('Software Development Project');
  });

  it('should fetch all projects', () => {
    let result: Project | null = null;
    let projectKey = 'SWD';

    service.getProject(projectKey).subscribe((project) => {
      result = project;
    });

    const req = httpTesting.expectOne(`http://localhost:8081/api/v1/bug-tracker/projects/${projectKey}`);
    expect(req.request.method).toBe('GET');

    req.flush({
        projectKey: 'SWD',
        name: 'Software Development',
        description: 'Software Development Project'
    });

    expect(result).not.toBeNull();
    expect(result!.projectKey).toBe('SWD');
    expect(result!.name).toBe('Software Development');
    expect(result!.description).toBe('Software Development Project');
  });

  it('should update a project', () => {
    const projectKey = 'SWD';
    const projectRequest = {
      name: 'Software Development',
      description: 'Updated Software Development Project'
    };

    let called = false;

    service.updateProject(projectKey, projectRequest).subscribe(() => {
      called = true;
    });

    const req = httpTesting.expectOne(`http://localhost:8081/api/v1/bug-tracker/projects/${projectKey}`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(projectRequest);

    req.flush(null);

    expect(called).toBe(true);
  });

  it('should delete a project', () => {
    const projectKey = 'SWD';

    let called = false;

    service.deleteProject(projectKey).subscribe(() => {
      called = true;
    });

    const req = httpTesting.expectOne(`http://localhost:8081/api/v1/bug-tracker/projects/${projectKey}`);
    expect(req.request.method).toBe('DELETE');

    req.flush(null);

    expect(called).toBe(true);
  });

  it('should get all project members', () => {
    const projectKey = 'SWD';
    let result: any[] = [];

    service.getUsersOnProject(projectKey).subscribe((members) => {
      result = members;
    });

    const req = httpTesting.expectOne(`http://localhost:8081/api/v1/bug-tracker/projects/${projectKey}/users`);
    expect(req.request.method).toBe('GET');

    req.flush([
      {
        id: 1,
        name: 'John Doe',
        email: 'john.doe@email.com'
      },
      {
        id: 2,
        name: 'Jane Smith',
        email: 'jane.smith@email.uk'
      }
    ]);
    expect(result.length).toBe(2);
    expect(result[0].id).toBe(1);
    expect(result[0].name).toBe('John Doe');
    expect(result[0].email).toBe('john.doe@email.com');
    expect(result[1].id).toBe(2);
    expect(result[1].name).toBe('Jane Smith');
    expect(result[1].email).toBe('jane.smith@email.uk');
  });

  it('should assign users to a project', () => {
    const projectKey = 'SWD';
    const request = {
      userIds: [1, 2]
    };
    let called = false;
    service.assignUsersToProject(projectKey, request).subscribe(() => {
      called = true;
    });
    const req = httpTesting.expectOne(`http://localhost:8081/api/v1/bug-tracker/projects/${projectKey}/users`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(request);
    req.flush(null);
    expect(called).toBe(true);
  });

  it('should get unassigned users', () => {
    const projectKey = 'SWD';
    let result: any[] = [];

    service.getUnassignedUsers(projectKey).subscribe((users) => {
      result = users;
    });

    const req = httpTesting.expectOne(`http://localhost:8081/api/v1/bug-tracker/projects/${projectKey}/users/unassigned`);
    expect(req.request.method).toBe('GET');

    req.flush([
      {
        id: 3,
        name: 'Alice Johnson',
        email: 'alice.johnson@email.com'
      },
      {
        id: 4,
        name: 'Bob Brown',
        email: 'bob.brown@mail.jp'
      }
    ]);

    expect(result.length).toBe(2);
    expect(result[0].id).toBe(3);
    expect(result[0].name).toBe('Alice Johnson');
    expect(result[0].email).toBe('alice.johnson@email.com');
    expect(result[1].id).toBe(4);
    expect(result[1].name).toBe('Bob Brown');
    expect(result[1].email).toBe('bob.brown@mail.jp');
  });

  it('should get project available roles', () => {
    const projectKey = 'SWD';
    let result: string[] = [];
    service.getProjectAvailableRoles(projectKey).subscribe((roles) => {
      result = roles;
    });
    const req = httpTesting.expectOne(`http://localhost:8081/api/v1/bug-tracker/projects/${projectKey}/roles`);
    expect(req.request.method).toBe('GET');
    req.flush(['Admin', 'Developer', 'Tester']);
    expect(result.length).toBe(3);
    expect(result[0]).toBe('Admin');
    expect(result[1]).toBe('Developer');
    expect(result[2]).toBe('Tester');
  });
});
