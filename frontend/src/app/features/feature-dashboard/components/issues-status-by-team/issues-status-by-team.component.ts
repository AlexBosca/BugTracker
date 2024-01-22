import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { Chart } from 'chart.js';
import { IssueModel } from 'src/app/features/feature-issues/models/IssueModel';

@Component({
  selector: 'app-issues-status-by-team',
  templateUrl: './issues-status-by-team.component.html',
  styleUrls: ['./issues-status-by-team.component.css']
})
export class IssuesStatusByTeamComponent implements OnInit, OnChanges {

  @Input() issues!: IssueModel[];
  issuesStatusByTeamBarchart!: Chart<'bar'>;
  teamsSet: Set<string> = new Set();
  assignedIssuesByTeam!: Map<string, IssueModel[]>;
  closedIssuesByTeam!: Map<string, IssueModel[]>;

  ngOnInit(): void {
    Chart.defaults.font.family = "'Bootstrap-icons'";
    Chart.defaults.font.size = 13;
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.issuesStatusByTeamBarchart = new Chart(
      'issuesStatusByTeam',
      {
        type: 'bar',
        data: {
          labels: Array.from(this.teamsSet),
          datasets: [
            {
              label: 'Assigned Issues',
              data: this.getAssignedIssuesNumberByTeam(),
              backgroundColor: '#0D47A1'
            },
            {
              label: 'Closed Issues',
              data: this.getClosedIssuesNumberByTeam(),
              backgroundColor: '#DD2C00'
            }
          ]
        }
      }
    );
  }

  public getAssignedIssuesNumberByTeam(): number[] {
    return [];
  }

  public getClosedIssuesNumberByTeam(): number[] {
    return [];
  }

  public extractUsersFromIssues() {
    this.issues.forEach((issue) => {
      if(issue.assignedUser) {
        this.teamsSet.add(issue.assignedUser.firstName + " " + issue.assignedUser.lastName);
      }

      if(issue.closedByUser) {
        this.teamsSet.add(issue.closedByUser.firstName + " " + issue.closedByUser.lastName);
      }
    });
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

  public getLengthOfIssuesByUserName(issues: IssueModel[] | undefined): number {
    if(issues === undefined) {
      return 0;
    }

    return issues.length;
  }
}
