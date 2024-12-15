import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import { ProjectService } from '../../services/project.service';
import { IssueService } from '../../services/issue.service';

@Component({
  selector: 'app-button',
  imports: [],
  templateUrl: './button.component.html',
  styleUrl: './button.component.css'
})
export class ButtonComponent {

  userCreation = {
    "userId": "UI-001",
    "firstName": "User",
    "lastName": "Name",
    "email": "user@company.com",
    "password": "User123Pass",
    "globalRole": 1
  };

  userUpdate = {
    "email": "user@company.uk",
    "password": "User123Pass",
    "globalRole": 1
  };

  projectCreation = {
    "projectKey": "SiPC",
    "name": "Sixth Project Created",
    "description": "This is the sixth project created."
  };

  projectUpdate = {
    "prokectKey": "SIPC",
    "name": "Sixth Project Created",
    "description": "This is the updated description of sixth project created"
  };

  issueCreation = {
    "issueId": "SiPC-001",
    "title": "Third SPC Issue",
    "description": "Third SPC issue description"
  };

  issueUpdate = {
    "issueId": "SiPC-001",
    "title": "Third SPC Issue",
    "description": "Third SPC issue updated description"
  };

  constructor(
    readonly userService: UserService,
    readonly projectService: ProjectService,
    readonly issueService: IssueService
  ) { }

  public createUser(): void {
    this.userService.createUser(this.userCreation)
      .subscribe();
  }

  public getAllUsers(): void {
    this.userService.getAllUsers()
      .subscribe({
        next: data => console.log(data)
      });
  }

  public getUser(): void {
    this.userService.getUser('UI-001')
      .subscribe({
        next: data => console.log(data)
      });
  }

  public updateUser(): void {
    this.userService.updateUser('UI-001', this.userUpdate)
      .subscribe();
  }

  public deleteUser(): void {
    this.userService.deleteUser('UI-001')
      .subscribe();
  }

  public createProject(): void {
    this.projectService.createProject(this.projectCreation)
      .subscribe();
  }

  public getAllProjects(): void {
    this.projectService.getAllProjects()
      .subscribe({
        next: data => console.log(data)
      });
  }

  public getProject(): void {
    this.projectService.getProject('SiPC')
      .subscribe({
        next: data => console.log(data)
      });
  }

  public updateProject(): void {
    this.projectService.updateProject('SiPC', this.projectUpdate)
      .subscribe();
  }

  public deleteProject(): void {
    this.projectService.deleteProject('SiPC')
      .subscribe();
  }

  public createIssue(): void {
    this.issueService.createIssue('SiPC', this.issueCreation)
      .subscribe();
  }

  public getAllIssues(): void {
    this.issueService.getAllIssues('SiPC')
      .subscribe({
        next: data => console.log(data)
      });
  }

  public getIssue(): void {
    this.issueService.getIssue('SiPC', 'SiPC-001')
      .subscribe({
        next: data => console.log(data)
      });
  }

  public updateIssue(): void {
    this.issueService.updateIssue('SiPC', 'SiPC-001', this.issueUpdate)
      .subscribe();
  }

  public deleteIssue(): void {
    this.issueService.deleteIssue('SiPC', 'SiPC-001')
      .subscribe();
  }
}
