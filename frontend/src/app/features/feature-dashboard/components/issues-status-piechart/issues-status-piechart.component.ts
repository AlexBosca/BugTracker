import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { Chart } from 'chart.js';
import { IssueModel } from 'src/app/features/feature-issues/models/IssueModel';
import { Status } from 'src/app/features/feature-issues/models/status.enum';

@Component({
  selector: 'app-issues-status-piechart',
  templateUrl: './issues-status-piechart.component.html',
  styleUrls: ['./issues-status-piechart.component.css']
})
export class IssuesStatusPiechartComponent implements OnInit, OnChanges {

  @Input() issues!: IssueModel[];
  issuesStatusPiechart!: Chart<'doughnut'>;
  splitedIssues!: Map<string, IssueModel[]>;
  colors = [
    '#0D47A1',
    '#1565C0',
    '#E4A11B',
    '#EF6C00',
    '#DD2C00',
    '#14A44D',
    '#9FA6B2',
    '#3B71CA',
    '#0043ce',
    '#8a3ffc',
    '#8e6a00',
    '#ba4e00',
    '#6f6f6f',
  ];

  constructor() { }

  ngOnInit(): void {
    Chart.defaults.font.family = "'Bootstrap-icons'";
    Chart.defaults.font.size = 13;

    this.issuesStatusPiechartInit();
  }

  ngOnChanges(changes: SimpleChanges): void {
    const geKey = (issue: IssueModel) => issue.status;
    this.splitedIssues = this.splitArrayIntoChunksWithKeys(this.issues, geKey);
    this.issuesStatusPiechartUpdate();
  }

  public issuesStatusPiechartInit(): void {
    this.issuesStatusPiechart = new Chart(
      'issuesStatusPiechart',
      {
        type: 'doughnut',
        data: {
          labels: Object.values(Status),
          datasets: [
            {
              data: [
                this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.NEW)),
                this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.ASSIGNED)),
                this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.OPEN)),
                this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.FIXED)),
                this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.REOPENED)),
                this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.PENDING_RETEST)),
                this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.RETEST)),
                this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.VERIFIED)),
                this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.CLOSED)),
                this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.DUPLICATE)),
                this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.REJECTED)),
                this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.DEFERRED)),
                this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.NOT_A_BUG))
              ],
              backgroundColor: this.colors
            }
          ]
        },
        options: {
          plugins: {
              legend: {
                  display: true,
                  labels: {
                      color: 'FFFFFF'
                  }
              }
          }
      }
      }
    );
  }

  public issuesStatusPiechartUpdate(): void {
    if(!this.issuesStatusPiechart) {
      this.issuesStatusPiechartInit();
    }

    this.issuesStatusPiechart.data.datasets[0].data = [
      this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.NEW)),
      this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.ASSIGNED)),
      this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.OPEN)),
      this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.FIXED)),
      this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.REOPENED)),
      this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.PENDING_RETEST)),
      this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.RETEST)),
      this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.VERIFIED)),
      this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.CLOSED)),
      this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.DUPLICATE)),
      this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.REJECTED)),
      this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.DEFERRED)),
      this.getLengthOfIssuesByStatus(this.splitedIssues.get(Status.NOT_A_BUG))
    ];

    this.issuesStatusPiechart.update();
  }

  splitArrayIntoChunksWithKeys<T, K>(arr: T[], condition: (item: T) => K): Map<K, T[]> {
    const chunksMap = new Map<K, T[]>();
    
    for(const item of arr) {
      const key = condition(item);
  
      if(!chunksMap.has(key)) {
        chunksMap.set(key, []);
      }
  
      chunksMap.get(key)!.push(item);
    }
  
    return chunksMap;
  }

  getLengthOfIssuesByStatus(issues: IssueModel[] | undefined): number {
    if(issues === undefined) {
      return 0;
    }

    return issues.length;
  }
}
