import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { Chart } from 'chart.js';
import { IssueModel } from 'src/app/features/feature-issues/models/IssueModel';

@Component({
  selector: 'app-issues-status-by-assignee',
  templateUrl: './issues-status-by-assignee.component.html',
  styleUrls: ['./issues-status-by-assignee.component.css']
})
export class IssuesStatusByAssigneeComponent implements OnInit, OnChanges {

  @Input() issues!: IssueModel[];
  issuesStatusByAssigneeBarchart!: Chart<'bar'>;
  usersSet: Set<string> = new Set();
  assignedIssuesByUser!: Map<string, IssueModel[]>;
  closedIssuesByUser!: Map<string, IssueModel[]>;

  ngOnInit(): void {
    Chart.defaults.font.family = "'Bootstrap-icons'";
    Chart.defaults.font.size = 13;
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.extractUsersFromIssues();
    const assignedUsersKey = (issue: IssueModel) => issue.assignedUser?.firstName + " " + issue.assignedUser?.lastName;
    this.assignedIssuesByUser = this.splitArrayIntoChunksWithKeys(this.issues, assignedUsersKey);
    const closedByUsersKey = (issue: IssueModel) => issue.closedByUser?.firstName + " " + issue.closedByUser?.lastName;
    this.closedIssuesByUser = this.splitArrayIntoChunksWithKeys(this.issues, closedByUsersKey);

    this.issuesPriorityPiechartUpdate();
  }

  public issuesStatusByAssigneeInit() {
    this.issuesStatusByAssigneeBarchart = new Chart(
      'issuesStatusByAssignee',
      {
        type: 'bar',
        data: {
          labels: Array.from(this.usersSet),
          datasets: [
            {
              label: 'Assigned Issues',
              data: this.getAssignedIssuesNumberByUser(),
              backgroundColor: '#0D47A1'
            },
            {
              label: 'Closed Issues',
              data: this.getClosedIssuesNumberByUser(),
              backgroundColor: '#DD2C00'
            }
          ]
        }
      }
    );
  }

  public issuesPriorityPiechartUpdate(): void {
    if(!this.issuesStatusByAssigneeBarchart) {
      this.issuesStatusByAssigneeInit();
    }

    this.issuesStatusByAssigneeBarchart.data.datasets[0].data = this.getAssignedIssuesNumberByUser();
    this.issuesStatusByAssigneeBarchart.data.datasets[1].data = this.getClosedIssuesNumberByUser();

    this.issuesStatusByAssigneeBarchart.update();
  }

  public getAssignedIssuesNumberByUser(): number[] {
    let assignedIssuesByUserLengths: number[] = [];

    this.usersSet.forEach((userName) => {
      assignedIssuesByUserLengths.push(
        this.getLengthOfIssuesByUserName(this.assignedIssuesByUser.get(userName))
      );
    });

    return assignedIssuesByUserLengths;
  }

  public getClosedIssuesNumberByUser(): number[] {
    let closedIssuesByUserLengths: number[] = [];

    this.usersSet.forEach((userName) => {
      closedIssuesByUserLengths.push(
        this.getLengthOfIssuesByUserName(this.closedIssuesByUser.get(userName))
      );
    });

    return closedIssuesByUserLengths;
  }

  public extractUsersFromIssues() {
    this.issues.forEach((issue) => {
      if(issue.assignedUser) {
        this.usersSet.add(issue.assignedUser.firstName + " " + issue.assignedUser.lastName);
      }

      if(issue.closedByUser) {
        this.usersSet.add(issue.closedByUser.firstName + " " + issue.closedByUser.lastName);
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
