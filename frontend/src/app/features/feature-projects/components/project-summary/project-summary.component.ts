import { Component, OnInit } from '@angular/core';
import { ProjectModel } from '../../models/ProjectModel';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { ProjectService } from '../../services/project.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ChartEvent, Chart } from 'chart.js';
import { IssueModel } from 'src/app/features/feature-issues/models/IssueModel';
import { Status } from 'src/app/features/feature-issues/models/status.enum';
import { StatusCategory } from 'src/app/features/feature-issues/models/StatusCategory';

@Component({
  selector: 'app-project-summary',
  templateUrl: './project-summary.component.html',
  styleUrls: ['./project-summary.component.css']
})
export class ProjectSummaryComponent implements OnInit {
  private projectKey!: string | null;
  project!: ProjectModel;
  issues: IssueModel[] = [];
  doneIssues: IssueModel[] = [];
  updatedIssues: IssueModel[] = [];
  dueIssues: IssueModel[] = [];
  todoIssues: IssueModel[] = [];
  inProgressIssues: IssueModel[] =[];
  error!: HttpErrorResponse;
  issuesStatusOverviewChart!: Chart<'doughnut'>;
  issuesPriorityBreakdownChart!: Chart<'bar'>;
  createdIssues: number = 0;
  colors = [
    '#0D47A1',
    '#1565C0',
    '#E4A11B',
    '#EF6C00',
    '#DD2C00'
  ];
  labels = [
    '\uF27E Lowest',
    '\uF282 Low',
    '\uF63B Medium',
    '\uF286 High',
    '\uF281 Highest'
  ];
  // issueStatusCategories= {
  //     'ToDo': [Status.NEW, Status.ASSIGNED],
  //     'InProgress': [Status.OPEN, Status.REOPENED, Status.PENDING_RETEST, Status.RETEST, Status.FIXED],
  //     'Done': [Status.VERIFIED, Status.CLOSED, Status.DEFERRED, Status.DUPLICATE, Status.REJECTED, Status.NOT_A_BUG]
  // };

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService
  ) { }

  ngOnInit(): void {
    this.route.parent!.paramMap.subscribe(
      (params: ParamMap) => {
        this.projectKey = params.get('id');
      }
    );

    Chart.defaults.font.family = "'Bootstrap-icons'";
    Chart.defaults.font.size = 13;

    this.fetchProject();
    this.fetchIssuesOnProject();
  }

  fetchProject(): void {
    if(this.projectKey) {
      this.projectService.getProject(this.projectKey)
        .subscribe({
          next: data => this.project = data,
          error: error => this.error = error
        });
    }
  }

  fetchIssuesOnProject(): void {
    if(this.projectKey) {
      this.projectService.getIssuesOnProject(this.projectKey)
        .subscribe({
          next: data => {
            this.issues = data;
            this.doneIssues = data.filter(issue => StatusCategory.DONE.includes(issue.status));    // verify DONE states
            this.todoIssues = data.filter(issue => StatusCategory.TO_DO.includes(issue.status));    // verify TODO states
            this.inProgressIssues = data.filter(issue => StatusCategory.IN_PROGRESS.includes(issue.status));    // verify IN PROGRESS states

            this.createdIssues = data.filter(issue => {
              let currentTime = new Date().getTime();
              let issueCreationTime = new Date(issue.createdOn).getTime();
              let diff = Math.abs(currentTime - issueCreationTime) / (1000 * 60 * 60 * 24);
              return diff <= 7;
            }).length;

            this.issuesStatusOverviewChartInit();
            this.issuesPriorityBreakdownChartInit();
          },
          error: error => this.error = error
        });
    }
  }

  public issuesStatusOverviewChartInit(): void {
    this.issuesStatusOverviewChart = new Chart(
      'issuesStatusOverviewChart',
      {
        type: 'doughnut',
        data: {
          labels: [' Done', ' To do', ' In progress'],
          datasets: [
            {
              label: '',
              data: [this.doneIssues.length, this.todoIssues.length, this.inProgressIssues.length],
              backgroundColor: ['#14A44D','#9FA6B2','#3B71CA']
            }
          ],
        },
        options: {
          plugins: {
            legend: {
              position: 'right'
            }
          }
        }
      }
    );
  }

  public issuesPriorityBreakdownChartInit(): void {
    this.issuesPriorityBreakdownChart = new Chart(
      'issuesPriorityBreakdownChart',
      {
        type: 'bar',
        data: {
          labels: ['\uF27E Lowest', '\uF282 Low', '\uF63B Medium', '\uF286 High', '\uF281 Highest'],
          datasets: [
            { 
              data: [65, 59, 80, 81, 56, 55, 40],
              backgroundColor: this.colors
            }
          ]
        },
        options: {
          plugins: {
            legend: {
              display: false
            }
          },
          scales: {
            x: {
              ticks: {
                color: (c) => {
                  return this.colors[c.index % this.colors.length];
                } 
              }
            }
          }
        }
      }
    );
  }

  // events
  public chartClicked({ event, active }: { event: ChartEvent, active: {}[] }): void {
    console.log(event, active);
  }

  public chartHovered({ event, active }: { event: ChartEvent, active: {}[] }): void {
    console.log(event, active);
  }
}
