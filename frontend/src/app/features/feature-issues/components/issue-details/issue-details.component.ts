import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { IssueModel } from '../../models/IssueModel';
import { IssueStatusRequest } from '../../models/IssueStatusRequest';
import { Status } from '../../models/status.enum';
import { IssueService } from '../../services/issue.service';

@Component({
  selector: 'app-issue-details',
  templateUrl: './issue-details.component.html',
  styleUrls: ['./issue-details.component.css']
})
export class IssueDetailsComponent implements OnInit {
  readonly IssueStatus = Status;
  readonly possibleActions: Map<Status, Array<string>> = new Map([
    [Status.NEW,['Assign']],
    [Status.ASSIGNED,['Open']],
    [Status.OPEN,['Fix', 'Duplicate', 'Reject', 'Defer', 'Not a Bug']],
    [Status.FIXED,['Send to Retest']],
    [Status.PENDING_RETEST,['Retest']],
    [Status.RETEST,['Verify', 'Reopen']],
    [Status.VERIFIED,['Close']],
    [Status.REOPENED,['Open']],
    [Status.CLOSED,[]],
    [Status.DUPLICATE,[]],
    [Status.REJECTED,[]],
    [Status.DEFERRED,[]],
    [Status.NOT_A_BUG,[]]
  ]);
  readonly possibleStates: Map<string, IssueStatusRequest> = new Map([
    ['Assign', IssueStatusRequest.assignToDeveloper],
    ['Open', IssueStatusRequest.open],
    ['Fix', IssueStatusRequest.fix],
    ['Duplicate', IssueStatusRequest.duplicate],
    ['Reject', IssueStatusRequest.reject],
    ['Defer', IssueStatusRequest.defer],
    ['Not a Bug', IssueStatusRequest.notABug],
    ['Send to Retest', IssueStatusRequest.sendToRetest],
    ['Retest', IssueStatusRequest.retest],
    ['Verify', IssueStatusRequest.verify],
    ['Reopen', IssueStatusRequest.reopen],
    ['Close', IssueStatusRequest.closeByDeveloper]
  ]);
  readonly SECONDS_OFFSET = 60;
  readonly MINUTES_OFFSET = this.SECONDS_OFFSET * 60;
  readonly HOURS_OFFSET = this.MINUTES_OFFSET * 60;
  readonly DAYS_OFFSET = this.HOURS_OFFSET * 60;
  private issueId!: string | null;
  issue!: IssueModel;
  error!: HttpErrorResponse;
  createdOn!: string;
  modifiedOn!: string | null;
  assignedOn!: string | null;
  closedOn!: string | null;

  constructor(
    private route: ActivatedRoute,
    private issueService: IssueService
    ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(
      (params: ParamMap) => {
        this.issueId = params.get('id');
      }
    );

    this.fetchIssue();
    console.log(this.issue.closedByUser);
  }

  fetchIssue(): void {
    if(this.issueId) {
      this.issueService.getIssue(this.issueId)
        .subscribe({
          next: data => {
            this.issue = data;
            this.initializeDates(data);
          },
          error: error => this.error = error
        });
    }
  }

  initializeDates(issue: IssueModel): void {
    this.createdOn = this.getElapsedTime(issue.createdOn);
    this.modifiedOn = issue.modifiedOn ? this.getElapsedTime(issue.modifiedOn) : null;
    this.assignedOn = issue.assignedOn ? this.getElapsedTime(issue.assignedOn) : null;
    this.closedOn = issue.closedOn ? this.getElapsedTime(issue.closedOn) : null;
  }

  getElapsedTime(date: Date): string {
    let now = new Date(Date.now());
    let givenDate = new Date(date);

    let timeDifferneceInSeconds = Math.abs(now.getTime() - givenDate.getTime()) / 1000;

    if(timeDifferneceInSeconds < this.SECONDS_OFFSET) {
      return `${Math.round(timeDifferneceInSeconds)} seconds ago`;
    }

    if(timeDifferneceInSeconds < this.MINUTES_OFFSET) {
      return `${Math.round(timeDifferneceInSeconds / this.SECONDS_OFFSET)} minutes ago`;
    }

    if(timeDifferneceInSeconds < this.HOURS_OFFSET) {
      return `${Math.round(timeDifferneceInSeconds / this.MINUTES_OFFSET)} hours ago`;
    }

    if(timeDifferneceInSeconds < this.DAYS_OFFSET) {
      return `${Math.round(timeDifferneceInSeconds / this.HOURS_OFFSET)} days ago`;
    }

    return givenDate.toDateString();
  }

  changeIssueStatus(action: string): void {
    if(this.issueId && action) {
      let possibleState = this.getPossibleStateByAction(action);
      if(possibleState)
        this.issueService.changeIssueStatus(this.issueId, possibleState)
          .subscribe({
            next: () => {
              console.log(possibleState);
              window.location.reload();
            },
            error: error => {
              this.error = error;
            }
          });
    }
  }

  getPossibleStateByAction(action: string): IssueStatusRequest {
    let possibleState = this.possibleStates.get(action);
    if(!possibleState) {
      throw new Error(`No possible status for ${action}`);
    }

    return possibleState;
  }
}
