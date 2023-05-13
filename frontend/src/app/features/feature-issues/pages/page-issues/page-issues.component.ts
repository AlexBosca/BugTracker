import { Component, OnInit } from '@angular/core';
import { Status } from '../../models/status.enum';
import { IssueModel } from '../../models/IssueModel';
import { HttpErrorResponse } from '@angular/common/http';
import { IssueService } from '../../services/issue.service';

@Component({
  templateUrl: './page-issues.component.html',
  styleUrls: ['./page-issues.component.css']
})
export class PageIssuesComponent implements OnInit {
  readonly IssueStatus = Status;
  issues: IssueModel[] = [];
  error!: HttpErrorResponse;

  constructor(
    private issueService: IssueService
  ) { }

  ngOnInit(): void {
    this.fetchIssues();
  }

  fetchIssues(): void {
    this.issueService.getIssues()
        .subscribe({
          next: data => this.issues = data,
          error: error => this.error = error
        });
  }
}