import { Component, OnInit } from '@angular/core';
import { ProjectModel } from '../../models/ProjectModel';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { ProjectService } from '../../services/project.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ChartData, ChartEvent, ChartType, ChartConfiguration } from 'chart.js';

@Component({
  selector: 'app-project-summary',
  templateUrl: './project-summary.component.html',
  styleUrls: ['./project-summary.component.css']
})
export class ProjectSummaryComponent implements OnInit {
  private projectId!: string| null;
  project!: ProjectModel;
  error!: HttpErrorResponse;
  // Doughnut Chart
  public doughnutChartLabels: string[] = ['Done', 'To do', 'In progress'];
  public doughnutChartData: ChartData<'doughnut'> = {
    labels: this.doughnutChartLabels,
    datasets: [
      {
        label: 'Dataset 1',
        data: [1, 2, 3],
        backgroundColor: [
          '#14A44D',
          '#9FA6B2',
          '#3B71CA'
        ]
      }
    ]
  };
  
  public doughnutChartOptions: ChartConfiguration<'doughnut'>['options'] = {
    plugins: {
      legend: {
        position: 'right'
      }
    }
  }
  public doughnutChartType: ChartType = 'doughnut';

  // Bar Chart
  public barChartLegend = false;
  public barChartPlugins = [];

  public barChartData: ChartConfiguration<'bar'>['data'] = {
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
  };

  public barChartOptions: ChartConfiguration<'bar'>['options'] = {
    responsive: false,
  };

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

  // events
  public chartClicked({ event, active }: { event: ChartEvent, active: {}[] }): void {
    console.log(event, active);
  }

  public chartHovered({ event, active }: { event: ChartEvent, active: {}[] }): void {
    console.log(event, active);
  }
}
