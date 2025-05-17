import { Component, inject } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { MaterialModule } from '../../../shared/material.import';
import { ProjectService } from '../../../services/project.service';
import { Project } from '../../../models/project.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IssueService } from '../../../services/issue.service';
import { Issue } from '../../../models/issue.model';
import { MatDialog } from '@angular/material/dialog';
import { FilterDialogComponent } from '../../../shared/filter-dialog/filter-dialog.component';
import { SharedModule } from '../../../shared/shared.module';
import { EntityDialogComponent } from '../../../shared/entity-dialog/entity-dialog.component';
import { CdkModule } from '../../../shared/cdk.import';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { User } from '../../../models/user.model';
import { UserAssignmentDialogComponent } from '../user-assignment-dialog/user-assignment-dialog.component';

@Component({
  selector: 'app-project-details',
  imports: [
    CdkModule,
    MaterialModule,
    SharedModule,
    RouterModule,
    CommonModule,
    FormsModule
  ],
  host: {
    '[style.--mat-form-field-container-vertical-padding]': '"0px"',
    '[style.--mat-form-field-container-height]': '"40px"'
  },
  template: `
    <mat-card appearance="outlined">
      <mat-card-content class="top-card-content">
        <span>
          <button class="grid-element" mat-button routerLink="/projects" matTooltip="Back to Projects">
            <mat-icon>arrow_back</mat-icon>
            BACK TO PROJECTS
          </button>
        </span>
        <span class="grid-element">TODAY'S DATE</span>
        <span class="grid-element">PEOPLE ON PROJECT</span>
        <span class="grid-element" style="font-size: 2rem;">{{ project?.name }}</span>
        <span class="grid-element" style="font-size: 2rem;">{{ todayDate | date: "MMM dd, yyyy" }}</span>
        <span class="grid-element avatar-stack">
          <mat-icon class="avatar" *ngFor="let user of users; let i = index">person</mat-icon>
        </span>
        <span class="grid-element" style="justify-self: end;">
          <button mat-fab style="font-size: 1rem; width: fit-content; padding: 8px;" matTooltip="Assign People to Project" (click)="openUserAssignmentDialog()">
            <mat-icon>add</mat-icon>
            Add people
          </button>
        </span>
      </mat-card-content>
    </mat-card>

    <div class="content-area">
      <mat-card class="main-content" appearance="outlined">
        <mat-card-content class="main-card-content">
          <div class="main-top-content">
            <div class="search-bar">
              <mat-form-field class="search-input" appearance="outline">
                <mat-label>Search issues...</mat-label>
                <input matInput [(ngModel)]="searchQuery" (keyup)="onSearch(searchQuery)" placeholder="Search...">
                <mat-icon matSuffix>search</mat-icon>
              </mat-form-field>
            </div>
            <section style="align-self: center; justify-self: end;">
              <mat-checkbox>My tasks</mat-checkbox>
            </section>
            <section style="align-self: center; justify-self: end;">
              <button mat-fab extended (click)="openFilterDialog()">
                <mat-icon>tune</mat-icon>
                Filter
              </button>
            </section>
            <section style="justify-self: end;">
              <button mat-fab extended (click)="openIssueCreationOnProjectDialog()">
                <mat-icon>add</mat-icon>
                Create task
              </button>
            </section>
          </div>

          <div class="main-task-lists" cdkDropListGroup>
            <mat-card class="main-task-list">
              <mat-card-header>
                <mat-card-title>To Do</mat-card-title>
              </mat-card-header>
              <mat-card-content
                cdkDropList
                id="TO_DO"
                [cdkDropListData]="filteredToDoIssues"
                (cdkDropListDropped)="drop($event)">
                @for (issue of filteredToDoIssues; track issue) {
                  <mat-card class="task-card" appearance="outlined" (click)="selectIssue(issue)" cdkDrag>
                    <mat-card-header>
                    <mat-card-subtitle>{{issue.issueId}}</mat-card-subtitle>
                    <mat-card-title>{{issue.title}}</mat-card-title>
                    </mat-card-header>
                    <mat-card-content>
                      <p>Task details go here...</p>
                    </mat-card-content>
                  </mat-card>
                }
              </mat-card-content>
            </mat-card>

            <mat-card class="main-task-list">
              <mat-card-header>
                <mat-card-title>In Progress</mat-card-title>
              </mat-card-header>
              <mat-card-content
                cdkDropList
                id="IN_PROGRESS"
                [cdkDropListData]="filteredInProgressIssues"
                (cdkDropListDropped)="drop($event)">
                @for (issue of filteredInProgressIssues; track issue) {
                  <mat-card class="task-card" appearance="outlined" (click)="selectIssue(issue)" cdkDrag>
                    <mat-card-header>
                      <mat-card-subtitle>{{issue.issueId}}</mat-card-subtitle>
                      <mat-card-title>{{issue.title}}</mat-card-title>
                    </mat-card-header>
                    <mat-card-content>
                      <p>Task details go here...</p>
                    </mat-card-content>
                  </mat-card>
                }
              </mat-card-content>
            </mat-card>

            <mat-card class="main-task-list">
              <mat-card-header>
                <mat-card-title>Review</mat-card-title>
              </mat-card-header>
              <mat-card-content
                cdkDropList
                id="REVIEW"
                [cdkDropListData]="filteredReviewIssues"
                (cdkDropListDropped)="drop($event)">
                @for (issue of filteredReviewIssues; track issue) {
                  <mat-card class="task-card" appearance="outlined" (click)="selectIssue(issue)" cdkDrag>
                    <mat-card-header>
                      <mat-card-subtitle>{{issue.issueId}}</mat-card-subtitle>
                      <mat-card-title>{{issue.title}}</mat-card-title>
                    </mat-card-header>
                    <mat-card-content>
                      <p>Task details go here...</p>
                    </mat-card-content>
                  </mat-card>
                }
              </mat-card-content>
            </mat-card>

            <mat-card class="main-task-list">
              <mat-card-header>
                <mat-card-title>Done</mat-card-title>
              </mat-card-header>
              <mat-card-content
                cdkDropList
                id="DONE"
                [cdkDropListData]="filteredDoneIssues"
                (cdkDropListDropped)="drop($event)">
                @for (issue of filteredDoneIssues; track issue) {
                  <mat-card class="task-card" appearance="outlined" (click)="selectIssue(issue)" cdkDrag>
                    <mat-card-header>
                      <mat-card-subtitle>{{issue.issueId}}</mat-card-subtitle>
                      <mat-card-title>{{issue.title}}</mat-card-title>
                    </mat-card-header>
                    <mat-card-content>
                      <p>Task details go here...</p>
                    </mat-card-content>
                  </mat-card>
                }
              </mat-card-content>
            </mat-card>
          </div>
        </mat-card-content>
      </mat-card>

      @if (selectedIssue !== undefined) {
        <mat-card class="details-content" appearance="outlined">
          <mat-card-header>
            <mat-card-title>
              <div>{{selectedIssue.title}}</div>
              <button mat-icon-button (click)="selectedIssue = undefined">
                <mat-icon>close</mat-icon>
              </button>
            </mat-card-title>
          </mat-card-header>
          <mat-card-content>
            <div class="details-section">
              <h4>DETAILS</h4>
              <div class="details-section-content">
                <div class="details-section-content-tag">Status:</div>
                <div class="details-section-content-value">
                  <div style="width: fit-content;">{{selectedIssue.status}}</div>
                </div>
              </div>

              <div class="details-section-content">
                <div class="details-section-content-tag">Priority:</div>
                <div class="details-section-content-value">
                  <div style="width: fit-content;">{{selectedIssue.priority}}</div>
                </div>
              </div>

              <div class="details-section-content">
                <div class="details-section-content-tag">Assignee:</div>
                <div class="details-section-content-value">
                  <div style="width: fit-content;">{{selectedIssue.assignedUserId}}</div>
                </div>
              </div>
            </div>

            <mat-divider></mat-divider>

            <div class="dates-section">
              <h4>DATES</h4>
              <div class="dates-section-content">
                <div class="dates-section-content-tag">Created:</div>
                <div class="dates-section-content-value">
                  <div style="width: fit-content;">{{selectedIssue.createdAt | date: "MMM dd, yyyy - hh:mm a"}}</div>
                </div>
              </div>

              <div class="dates-section-content">
                <div class="dates-section-content-tag">Updated:</div>
                <div class="dates-section-content-value">
                  <div style="width: fit-content;">{{selectedIssue.updatedAt | date: "MMM dd, yyyy - hh:mm a"}}</div>
                </div>
              </div>

              <div class="dates-section-content">
                <div class="dates-section-content-tag">Deadline:</div>
                <div class="dates-section-content-value">
                  <div style="width: fit-content;">
                    @if (selectedIssue.deadline !== undefined) {
                      {{selectedIssue.deadline | date: "MMM dd, yyyy - hh:mm a"}}
                    } @else {
                      -
                    }
                  </div>
                </div>
              </div>
            </div>

            <mat-divider></mat-divider>

            <div class="description-section">
              <h4>DESCRIPTION</h4>
              <p>{{selectedIssue.description}}</p>
            </div>

            <mat-divider></mat-divider>

            <div class="comments-section">
              <h4>COMMENTS</h4>
              <div>
                <div>
                  <mat-form-field appearance="outline" style="width: 100%; margin-bottom: 1rem;">
                    <mat-label>Comment</mat-label>
                    <textarea matInput></textarea>
                  </mat-form-field>
                </div>
                @for (comment of comments; track comment) {
                  <mat-card class="task-card" appearance="outlined" style="border: none;">
                    <mat-card-header>
                      <div class="comment-header">
                        <div class="comment-header-user">
                          <mat-icon class="comment-header-user-avatar">person</mat-icon>
                          <div>{{comment.userId}}</div>
                        </div>
                        <div class="comment-header-date" style="font-size: 0.8rem;">{{comment.date | date: "MMM dd, yyyy - hh:mm a"}}</div>
                      </div>
                    </mat-card-header>
                    <mat-card-content>
                      <p>{{comment.comment}}</p>
                    </mat-card-content>
                  </mat-card>

                  @if (comment !== comments[comments.length - 1]) {
                    <mat-divider></mat-divider>
                  }
                }
              </div>
            </div>
          </mat-card-content>
        </mat-card>
      }
    </div>
  `,
  styleUrl: './project-details.component.css',
  styles: [`
    mat-card {
      margin: 0.5rem;
    }

    mat-form-field {
      margin: 0;
      height: 40px;
    }

    mat-card-header {
      display: block;
    }

    mat-card-title {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }

    input {
      padding: 0.5rem;
    }

    .top-card-content {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      grid-template-rows: repeat(2, 1fr);
      grid-template-areas:
        "a b c g"
        "d e f g";
    }

    .grid-element {
      width: fit-content;
      padding: 8px;
    }

    .grid-element:nth-child(1) {
      grid-area: a;
    }

    .grid-element:nth-child(2) {
      grid-area: b;
    }

    .grid-element:nth-child(3) {
      grid-area: c;
    }

    .grid-element:nth-child(4) {
      grid-area: d;
    }

    .grid-element:nth-child(5) {
      grid-area: e;
    }

    .grid-element:nth-child(6) {
      grid-area: f;
    }

    .grid-element:nth-child(7) {
      grid-area: g;
      align-self: center;
    }

    .avatar-stack {
      display: flex;
      position: relative;
      align-items: center;
    }

    .avatar {
      border-radius: 50%;
      background-color: #fff;
      color: black;
      border: 2px solid black;
      font-size: 2.5rem;
      margin: 0 0 0 -15px;
      height: auto;
      width: auto;
    }

    .avatar-stack .avatar:nth-child(1) {
      margin-left: 0;
    }

    .content-area {
      display: flex;
    }

    .main-content {
      width: 100%;
      height: fit-content;
    }

    .main-card-content {
    }

    .main-top-content {
      display: grid;
      grid-template-columns: repeat(5, 1fr);
      grid-template-rows: repeat(1, 1fr);
      grid-template-areas: "a a b c d";
    }

    .main-task-lists {
      margin-top: 0.5rem;
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      grid-template-rows: repeat(1, 1fr);
      gap: 0.5rem;
    }

    .main-task-list {
      margin-left: 0;
      margin-right: 0;
      width: 100%;
      border-width: 0;
    }

    .task-card {
      margin-left: 0;
      margin-right: 0;
    }

    .search-bar {
      width: 100%;
      grid-area: a;
      align-self: center;
    }

    .search-input {
      width: 100%;
    }

    .details-content {
      width: 40%;
    }

    .details-section {
      display: flex;
      flex-direction: column;
      margin-bottom: 1rem;
    }

    .details-section-content {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      grid-template-rows: repeat(1, 1fr);
      grid-template-areas: "a b";
    }

    .details-section-content-value {
      grid-area: b;
      text-align: right;
    }

    .dates-section {
      display: flex;
      flex-direction: column;
      margin-bottom: 1rem;
    }

    .dates-section-content {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      grid-template-rows: repeat(1, 1fr);
      grid-template-areas: "a b";
    }

    .dates-section-content-value {
      grid-area: b;
      text-align: right;
    }

    .description-section {
      display: flex;
      flex-direction: column;
      margin-bottom: 1rem;
    }

    .comments-section {
      display: flex;
      flex-direction: column;
      padding-bottom: 1rem;
    }

    .comment-header {
      width: 100%;
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0.5rem;
    }

    .comment-header-user {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 0.5rem;
    }

    .comment-header-date {}

    .comment-header-user-avatar {
      border-radius: 50%;
      background-color: #fff;
      color: black;
      border: 2px solid black;
      font-size: 1.5rem;
      margin: 0 0 0 -15px;
      height: auto;
      width: auto;
    }
  `]
})
export class ProjectDetailsComponent {
  searchQuery = '';

  projectKey!: string;
  project: Project | null = null;
  todayDate: string = new Date().toLocaleDateString();
  toDoIssues: Issue[] = [];
  filteredToDoIssues: Issue[] = [];
  inProgressIssues: Issue[] = [];
  filteredInProgressIssues: Issue[] = [];
  reviewIssues: Issue[] = [];
  filteredReviewIssues: Issue[] = [];
  doneIssues: Issue[] = [];
  filteredDoneIssues: Issue[] = [];
  users: User[] = [];
  availableUsers: User[] = [];
  availableRoles: string[] = [];
  projectNamesAndKeys: {
    projectKey: string;
    projectName: string;
  } [] = [];
  priorities: string[] = [];
  activeFilters: {
    projectNamesAndKeys: {
      projectKey: string;
      projectName: string;
    } [];
    priorities: string[];
    assignees: string[];
  } = {
    projectNamesAndKeys: [],
    priorities: [],
    assignees: []
  };
  selectedIssue: Issue | undefined;
  comments: {
    userId: string;
    comment: string;
    date: Date;
  }[] = [
    {
      userId: 'user-a-id',
      comment: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua ut enim ad minim veniam quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat',
      date: new Date('2024-07-01')
    },
    {
      userId: 'user-b-id',
      comment: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit sed do eiusmod tempor incididunt ut labore laboris nisi ut aliquip ex ea commodo consequat',
      date: new Date('2024-07-02')
    }
  ];

  readonly filterDialog = inject(MatDialog);
  readonly entityDialog = inject(MatDialog);
  readonly userAssignmentDialog = inject(MatDialog);

  constructor(
    readonly router: ActivatedRoute,
    readonly projectService: ProjectService,
    readonly issueService: IssueService
  ) {
    this.getProjectKey();
    this.getProjectDetails();
    this.getIssuesOnProject();
    this.getUsersOnProject();
    this.getAvailableUsers();
    this.getProjectAvailableRoles();
  }

  getProjectKey(): void {
    this.router.queryParams.subscribe(params => {
      this.projectKey = params['projectKey'];
    });
  }

  getProjectDetails(): void {
    this.projectService.getProject(this.projectKey).subscribe((project) => {
      this.project = project;
    });
  }

  getIssuesOnProject(): void {
    this.issueService.getIssuesOnProject(this.projectKey).subscribe((issues) => {
      this.toDoIssues = issues.filter(issue => issue.status === 'TO_DO');
      this.filteredToDoIssues = this.toDoIssues;
      this.inProgressIssues = issues.filter(issue => issue.status === 'IN_PROGRESS');
      this.filteredInProgressIssues = this.inProgressIssues
      this.reviewIssues = issues.filter(issue => issue.status === 'REVIEW');
      this.filteredReviewIssues = this.reviewIssues;
      this.doneIssues = issues.filter(issue => issue.status === 'DONE');
      this.filteredDoneIssues = this.doneIssues;
    });
  }

  getUsersOnProject(): void {
    this.projectService.getUsersOnProject(this.projectKey).subscribe((users) => {
      this.users = users;
    });
  }

  getAvailableUsers(): void {
    this.projectService.getUnassignedUsers(this.projectKey).subscribe((users) => {
      this.availableUsers = users;
    });
  }

  getProjectAvailableRoles(): void {
    this.projectService.getProjectAvailableRoles(this.projectKey).subscribe((roles) => {
      this.availableRoles = roles;
    });
  }

  openUserAssignmentDialog(): void {
    this.userAssignmentDialog.open(UserAssignmentDialogComponent,{
      data: {
        projectKey: this.projectKey,
        availableUsers: this.availableUsers,
        availableRoles: this.availableRoles
      },
      width: '800px'
    }).afterClosed().subscribe((result) => {
      if ((result !== undefined) && (result.length > 0)) {
        console.log('User assignments:', result);
        this.projectService.assignUsersToProject(this.projectKey, {userProjectAssignments: result}).subscribe(() => {
          console.log('Users assigned successfully');
          this.getUsersOnProject();
          this.getAvailableUsers();
        });
      }
    });
  }

  onSearch(query: string): void {
    if(!query) {
      this.filteredToDoIssues = this.toDoIssues;
      this.filteredInProgressIssues = this.inProgressIssues;
      this.filteredReviewIssues = this.reviewIssues;
      this.filteredDoneIssues = this.doneIssues;
    } else {
      this.filteredToDoIssues = this.toDoIssues.filter(issue =>
        issue.issueId.toLowerCase().includes(query.toLowerCase()) ||
        issue.title.toLowerCase().includes(query.toLowerCase()) ||
        issue.description.toLowerCase().includes(query.toLowerCase()) ||
        issue.assignedUserId.toLowerCase().includes(query.toLowerCase())
      );
    }
  }

  openFilterDialog(): void {
    this.filterDialog.open(FilterDialogComponent, {
      data: {
        projectNamesAndKeys: this.projectNamesAndKeys,
        priorityList: this.priorities,
        assigneeList: this.users,
        activeFilters: this.activeFilters
      }
    }).afterClosed().subscribe((result) => {
      if (result !== undefined) {
        this.filteredToDoIssues = this.toDoIssues.filter(issue => {
          return (result.assignees.length === 0) || (result.assignees.includes(issue.assignedUserId))
        });

        this.filteredInProgressIssues = this.inProgressIssues.filter(issue => {
          return (result.assignees.length === 0) || (result.assignees.includes(issue.assignedUserId))
        });

        this.filteredReviewIssues = this.reviewIssues.filter(issue => {
          return (result.assignees.length === 0) || (result.assignees.includes(issue.assignedUserId))
        });

        this.filteredDoneIssues = this.doneIssues.filter(issue => {
          return (result.assignees.length === 0) || (result.assignees.includes(issue.assignedUserId))
        });

      }
    });
  }

  openIssueCreationOnProjectDialog(): void {
    this.entityDialog.open(EntityDialogComponent, {
      data: {
        title: 'Create Issue',
        fields: [
          { name: 'issueId', label: 'Issue ID', type: 'text' },
          { name: 'title', label: 'Title', type: 'text' },
          { name: 'priority', label: 'Priority', type: 'select', options: ['LOW', 'MEDIUM', 'HIGH'] },
          { name: 'status', label: 'Status', type: 'select', options: ['TO_DO', 'IN_PROGRESS', 'REVIEW', 'DONE'] },
          { name: 'projectKey', label: 'Project Key', type: 'text' },
          { name: 'description', label: 'Description', type: 'text-area' }
        ],
        type: 'Create'
      },
      disableClose: true,
      height: '800px',
      width: '500px'
    }).afterClosed().subscribe(issue => {
      if (issue) {
        console.log('Issue created:', issue);
        this.issueService.createIssueOnProject(this.projectKey, issue).subscribe(() => {
          console.log('Issue created successfully');
        });
      }
    });
  }

  // openIssueEditOnProjectDialog(): void {
  //   this
  // }

  selectIssue(issue: Issue): void {
    this.selectedIssue = issue;
  }

  drop(event: CdkDragDrop<Issue[]>) {
    if(event.previousContainer === event.container) {
      moveItemInArray(
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );
    } else {
      console.log(event);
      this.issueService.partiallyUpdateIssueOnProject(
        this.projectKey,
        event.previousContainer.data[event.previousIndex].issueId,
        { status: event.container.id }
      ).subscribe(() => {
        console.log('Issue status updated successfully');
        this.getIssuesOnProject();
      });
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );
    }
  }
}
