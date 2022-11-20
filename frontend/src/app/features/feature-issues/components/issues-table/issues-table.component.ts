import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { IssueModel } from '../../models/IssueModel';
import { IssueService } from '../../services/issue.service';

@Component({
  selector: 'app-issues-table',
  templateUrl: './issues-table.component.html',
  styleUrls: ['./issues-table.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class IssuesTableComponent implements OnInit {

  issues: IssueModel[] = [];

  constructor(private issueService: IssueService) { }

  ngOnInit(): void {
    this.getIssues();
  }

  getIssues(): void {
    this.issueService.getIssues().subscribe(issues => this.issues = issues)
  }

}
