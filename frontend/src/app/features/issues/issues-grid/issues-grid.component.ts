import { Component, inject } from '@angular/core';
import { MaterialModule } from '../../../shared/material.import';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IssueService } from '../../../services/issue.service';
import { Issue } from '../../../models/issue.model';
import { MatDialog } from '@angular/material/dialog';
import { SharedModule } from '../../../shared/shared.module';
import { FilterDialogComponent } from '../../../shared/filter-dialog/filter-dialog.component';
import { EntityDialogComponent } from '../../../shared/entity-dialog/entity-dialog.component';
import { ProjectService } from '../../../services/project.service';

@Component({
  selector: 'app-issues-grid',
  imports: [MaterialModule, SharedModule, CommonModule, FormsModule],
  host: {
    '[style.--mat-form-field-container-vertical-padding]': '"0px"',
    '[style.--mat-form-field-container-height]': '"40px"'
  },
  template: `
    <div class="top-bar">
      <h2>Issues</h2>
      <div class="spacer"></div>
      <div class="search-bar">
        <mat-form-field appearance="outline">
          <mat-label>Search issues...</mat-label>
          <input matInput [(ngModel)]="searchQuery" (keyup)="onSearch(searchQuery)" placeholder="Search...">
          <mat-icon matSuffix>search</mat-icon>
        </mat-form-field>
      </div>
      <button mat-fab (click)="openFilterDialog()">
        <mat-icon>tune</mat-icon>
      </button>
      <!-- <div class="spacer"></div> -->
      <div class="spacer"></div>
      <button mat-fab extended (click)="openIssueCreationDialog()">
        <mat-icon>add</mat-icon>
        Create Issue
      </button>
    </div>
    @for (issue of filteredIssues; track issue.issueId) {
      <div class="margins">
        <mat-card appearance="outlined" (click)="openIssueEditDialog(issue)">
          <mat-card-header>
            <div mat-card-avatar>
              <mat-icon class="avatar">account_circle</mat-icon>
            </div>
            <mat-card-title  matBadge="2" matBadgeSize="large">{{issue.title}}</mat-card-title>
            <mat-card-subtitle>
              <span>{{issue.issueId}}</span>
              <span>{{issue.projectName}}</span>
              <span>
                <mat-icon class="footer-item-icon">account_circle</mat-icon>
                <span class="footer-item-text">{{issue.updatedBy}}</span>
              </span>
              <span>
                <mat-icon class="footer-item-icon">update</mat-icon>
                <span class="footer-item-text">Updated at: {{issue.updatedAt | date:"MMM dd, yyyy 'at' hh:mm a"}}</span>
              </span>
            </mat-card-subtitle>
          </mat-card-header>
          <mat-divider></mat-divider>
          <mat-card-footer>
            <div class="footer-item">
              <mat-icon class="footer-item-icon">account_circle</mat-icon>
              <span class="footer-item-text">{{issue.assignee}}</span>
            </div>

            <div class="footer-item">
              <mat-icon class="footer-item-icon">density_medium</mat-icon>
              <span class="footer-item-text">{{issue.priority}}</span>
            </div>

            <div class="footer-item">
              <mat-chip>{{issue.status}}</mat-chip>
            </div>

            <div class="footer-item">
              <mat-icon class="footer-item-icon">more_time</mat-icon>
              <span class="footer-item-text">Created at: {{issue.createdAt | date:"MMM dd, yyyy 'at' hh:mm a"}}</span>
            </div>
          </mat-card-footer>
        </mat-card>
      </div>
    }
  `,
  styleUrl: './issues-grid.component.css',
  styles: [`
    ::ng-deep .mat-mdc-card-header-text{
      width: 100%;
    }
    mat-form-field {
      width: 100%;
      margin: 0;
      height: 40px;
    }
    mat-icon.avatar {
      font-size: 3rem;
      height: auto;
      width: auto;
    }
    mat-card {
      cursor: pointer;
    }
    mat-card-subtitle {
      display: flex;
      justify-content: space-between;
    }
    mat-card-footer {
      display: flex;
      justify-content: space-between;
    }
    input {
      padding: 0.5rem;
    }
    .footer-item {
      padding: 1.5rem;
    }
    .footer-item-icon {
      vertical-align: middle;
    }
    .footer-item-text {
      vertical-align: middle;
      padding: 0.5rem;
    }
    h2 {
      margin: 1rem;
    }
    .margins {
      margin: 0.5rem;
    }
    .top-bar {
      margin: 1rem 0;
      display: flex;
      justify-content: start;
      align-items: center;
      padding: 0 1rem;
      gap: 3rem;
    }
    .spacer {
      flex-grow: 1;
    }
  `]
})
export class IssuesGridComponent {
  issues: Issue[] = [];

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

  filteredIssues: Issue[] = [];

  searchQuery = '';

  projectNamesAndKeys: {
    projectKey: string;
    projectName: string;
  } [] = []

  priorities: string[] = [];

  assineeList: string[] = [];

  readonly filterDialog = inject(MatDialog);

  readonly entityDialog = inject(MatDialog);

  constructor(
    readonly issueService: IssueService,
    readonly projectService: ProjectService
  ) {
    this.fetchIssues();
    this.fetchProjectKeys();
  }

  fetchIssues() {
    this.issueService.getAllIssues().subscribe((issues) => {
      this.issues = issues;
      this.filteredIssues = issues;

      this.priorities = issues.map(issue => issue.priority).filter((value, index, self) => {
        return self.indexOf(value) === index;
      });

      this.assineeList = issues.map(issue => issue.assignee).filter((value, index, self) => {
        return self.indexOf(value) === index;
      });
    });
  }

  fetchProjectKeys() {
    this.projectService.getAllProjects().subscribe((projects) => {
      this.projectNamesAndKeys = projects.map(project => ({
        projectKey: project.projectKey,
        projectName: project.name
      })).filter((value, index, self) =>
        self.findIndex(v => v.projectKey === value.projectKey) === index
      );
    });
  }

  onSearch(query: string): void {
    if(!query) {
      this.filteredIssues = this.issues;
    } else {
      this.filteredIssues = this.issues.filter(issue =>
        issue.issueId.toLowerCase().includes(query.toLowerCase()) ||
        issue.title.toLowerCase().includes(query.toLowerCase()) ||
        issue.description.toLowerCase().includes(query.toLowerCase()) ||
        // issue.assignee.toLowerCase().includes(query.toLowerCase()) ||
        // issue.projectName.toLowerCase().includes(query.toLowerCase()) ||
        // issue.projectKey.toLowerCase().includes(query.toLowerCase()) ||
        issue.projectKey.toLowerCase().includes(query.toLowerCase())
        // issue.status.toLowerCase().includes(query.toLowerCase()) ||
        // issue.priority.toLowerCase().includes(query.toLowerCase()) ||
        // issue.updatedBy.toLowerCase().includes(query.toLowerCase())
      );
    }
  }

  openFilterDialog(): void {
    this.filterDialog.open(FilterDialogComponent,{
      data: {
        projectNamesAndKeys: this.projectNamesAndKeys,
        priorityList: this.priorities,
        assigneeList: this.assineeList,
        activeFilters: this.activeFilters
      }
    }).afterClosed().subscribe(result => {
      if(result !== undefined) {
        const selectedProjectKeys = result.projectNamesAndKeys?.map(
          (item: { projectKey: string; projectName: string }) => item.projectKey
        ) ?? [];

        this.filteredIssues = this.issues.filter(issue => {
          return (selectedProjectKeys.length === 0 || selectedProjectKeys.includes(issue.projectKey)) &&
            (result.priorities.length === 0 || result.priorities.includes(issue.priority)) &&
            (result.assignees.length === 0 || result.assignees.includes(issue.assignee));
        });
      }
    });
  }

  openIssueCreationDialog(): void {
    // Implement the logic to open the issue creation dialog
    this.entityDialog.open(EntityDialogComponent, {
      data: {
        title: 'Create Issue',
        fields: [
          { name: 'issueId', label: 'Issue ID', type: 'text' },
          { name: 'title', label: 'Title', type: 'text' },
          { name: 'assignee', label: 'Assignee', type: 'text' },
          { name: 'priority', label: 'Priority', type: 'select', options: this.priorities },
          { name: 'status', label: 'Status', type: 'select', options: ['Open', 'In Progress', 'Closed'] },
          { name: 'updatedBy', label: 'Updated By', type: 'text' },
          { name: 'createdAt', label: 'Created At', type: 'date' },
          { name: 'updatedAt', label: 'Updated At', type: 'date' },
          { name: 'projectName', label: 'Project', type: 'select', options: this.projectNamesAndKeys },
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
        this.issueService.createIssue(issue).subscribe(() => {
          console.log('Issue created successfully');
        });
      }
    });
  }

  openIssueEditDialog(issue: Issue): void {
    // Implement the logic to open the issue creation dialog
    this.entityDialog.open(EntityDialogComponent, {
      data: {
        title: 'Edit Issue',
        fields: [
          { name: 'issueId', label: 'Issue ID', type: 'text' },
          { name: 'title', label: 'Title', type: 'text' },
          { name: 'assignee', label: 'Assignee', type: 'text' },
          { name: 'priority', label: 'Priority', type: 'select', options: this.priorities },
          { name: 'status', label: 'Status', type: 'select', options: ['Open', 'In Progress', 'Closed'] },
          { name: 'updatedBy', label: 'Updated By', type: 'text' },
          { name: 'createdAt', label: 'Created At', type: 'date' },
          { name: 'updatedAt', label: 'Updated At', type: 'date' },
          { name: 'projectName', label: 'Project', type: 'select', options: this.projectNamesAndKeys },
          { name: 'projectKey', label: 'Project Key', type: 'text' },
          { name: 'description', label: 'Description', type: 'text-area' }
        ],
        entityData: issue,
        type: 'Edit'
      },
      disableClose: true,
      height: '800px',
      width: '500px'
    }).afterClosed().subscribe(issue => {
      if (issue) {
        console.log('Issue created:', issue);
        this.issueService.updateIssue(issue.issueId, issue).subscribe(() => {
          console.log('Issue created successfully');
        });
      }
    });
  }
}
