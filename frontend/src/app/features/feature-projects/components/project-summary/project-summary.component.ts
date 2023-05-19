import { Component, OnInit } from '@angular/core';
import { ProjectModel } from '../../models/ProjectModel';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { ProjectService } from '../../services/project.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ChartData, ChartEvent, ChartType, ChartConfiguration, Chart } from 'chart.js';
import { IssueModel } from 'src/app/features/feature-issues/models/IssueModel';
import { Status } from 'src/app/features/feature-issues/models/status.enum';
import { IssueStatusRequest } from 'src/app/features/feature-issues/models/IssueStatusRequest';

@Component({
  selector: 'app-project-summary',
  templateUrl: './project-summary.component.html',
  styleUrls: ['./project-summary.component.css']
})
export class ProjectSummaryComponent implements OnInit {
  private projectId!: string| null;
  project!: ProjectModel;
  issues: IssueModel[] = [];
  doneIssues: IssueModel[] = [];
  updatedIssues: IssueModel[] = [];
  createdIssues: IssueModel[] = [];
  dueIssues: IssueModel[] = [];
  todoIssues: IssueModel[] = [];
  inProgressIssues: IssueModel[] =[];
  error!: HttpErrorResponse;
  issuesStatusOverviewChart!: Chart<'doughnut'>;
  issuesPriorityBreakdownChart!: Chart<'bar'>;


  // Bar Chart
  // public barChartLegend = false;
  // public barChartPlugins = [];

  // public barChartData: ChartConfiguration<'bar'>['data'] = {
  //   labels: ['None', 'Lowest', 'Low', 'Medium', 'High', 'Highest', 'Others'],
  //   datasets: [
  //     { 
  //       data: [65, 59, 80, 81, 56, 55, 40],
  //       backgroundColor: [
  //         '#9FA6B2',
  //         '#14A44D',
  //         '#3B71CA',
  //         '#DC4C64',
  //         '#E4A11B',
  //         '#54B4D3',
  //         '#332D2D'
  //       ]
  //     }
  //   ]
  // };

  // public barChartOptions: ChartConfiguration<'bar'>['options'] = {
  //   responsive: false,
  // };

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService
  ) { }

  ngOnInit(): void {
    this.route.parent!.paramMap.subscribe(
      (params: ParamMap) => {
        this.projectId = params.get('id');
      }
    );

    this.fetchProject();
    this.fetchIssuesOnProject();
  }

  fetchProject(): void {
    if(this.projectId) {
      this.projectService.getProject(this.projectId)
        .subscribe({
          next: data => this.project = data,
          error: error => this.error = error
        });
    }
  }

  fetchIssuesOnProject(): void {
    if(this.projectId) {
      this.projectService.getIssuesOnProject(this.projectId)
        .subscribe({
          next: data => {
            this.issues = data;
            this.doneIssues = data.filter(issue => [Status.VERIFIED, Status.CLOSED, Status.DEFERRED, Status.DUPLICATE, Status.REJECTED, Status.NOT_A_BUG].includes(issue.status));    // verify DONE states
            this.todoIssues = data.filter(issue => [Status.NEW, Status.ASSIGNED, Status.OPEN, Status.REOPENED].includes(issue.status));    // verify TODO states
            this.inProgressIssues = data.filter(issue => [Status.PENDING_RETEST, Status.RETEST, Status.FIXED].includes(issue.status));    // verify IN PROGRESS states

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
          labels: ['Done', 'To do', 'In progress'],
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
          labels: ['None', 'Lowest', 'Low', 'Medium', 'High', 'Highest', 'Others'],
          datasets: [
            { 
              data: [65, 59, 80, 81, 56, 55, 40],
              backgroundColor: [
                '#9FA6B2',
                '#14A44D',
                '#3B71CA',
                '#DC4C64',
                '#E4A11B',
                '#54B4D3',
                '#332D2D'
              ]
            }
          ]
        },
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
