import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { Chart } from 'chart.js';
import { IssueModel } from 'src/app/features/feature-issues/models/IssueModel';
import { Priority } from 'src/app/features/feature-issues/models/priority.enum';

@Component({
  selector: 'app-issues-priority-piechart',
  templateUrl: './issues-priority-piechart.component.html',
  styleUrls: ['./issues-priority-piechart.component.css']
})
export class IssuesPriorityPiechartComponent implements OnInit, OnChanges {

  @Input() issues!: IssueModel[];
  issuesPriorityPiechart!: Chart<'doughnut'>;
  splitedIssues!: Map<string, IssueModel[]>;
  colors = [
    '#0D47A1',
    '#1565C0',
    '#E4A11B',
    '#EF6C00',
    '#DD2C00'
  ];

  ngOnInit(): void {
    Chart.defaults.font.family = "'Bootstrap-icons'";
    Chart.defaults.font.size = 13;
  }

  ngOnChanges(changes: SimpleChanges): void {
    const getKey = (issue: IssueModel) => issue.priority;
    this.splitedIssues = this.splitArrayIntoChunksWithKeys(this.issues, getKey);
    this.issuesPriorityPiechartUpdate();
  }

  public issuesPriorityPiechartInit(): void {
    this.issuesPriorityPiechart = new Chart(
      'issuesPriorityPiechart',
      {
        type: 'doughnut',
        data: {
          labels: Object.values(Priority),
          datasets: [
            {
              data: [
                this.getLengthOfIssuesByPriority(this.splitedIssues.get(Priority.LOWEST)),
                this.getLengthOfIssuesByPriority(this.splitedIssues.get(Priority.LOW)),
                this.getLengthOfIssuesByPriority(this.splitedIssues.get(Priority.MEDIUM)),
                this.getLengthOfIssuesByPriority(this.splitedIssues.get(Priority.HIGH)),
                this.getLengthOfIssuesByPriority(this.splitedIssues.get(Priority.HIGHEST))
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

  public issuesPriorityPiechartUpdate(): void {
    if(!this.issuesPriorityPiechart) {
      this.issuesPriorityPiechartInit();
    }

    this.issuesPriorityPiechart.data.datasets[0].data = [
      this.getLengthOfIssuesByPriority(this.splitedIssues.get(Priority.LOWEST)),
      this.getLengthOfIssuesByPriority(this.splitedIssues.get(Priority.LOW)),
      this.getLengthOfIssuesByPriority(this.splitedIssues.get(Priority.MEDIUM)),
      this.getLengthOfIssuesByPriority(this.splitedIssues.get(Priority.HIGH)),
      this.getLengthOfIssuesByPriority(this.splitedIssues.get(Priority.HIGHEST))
    ];

    this.issuesPriorityPiechart.update();
  }

  public splitArrayIntoChunksWithKeys<T, K>(arr: T[], condition: (item: T) => K): Map<K, T[]> {
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

  public getLengthOfIssuesByPriority(issues: IssueModel[] | undefined): number {
    if(issues === undefined) {
      return 0;
    }

    return issues.length;
  }
}
