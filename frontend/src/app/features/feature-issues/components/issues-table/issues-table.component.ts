import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input } from '@angular/core';
import { IssueModel } from '../../models/IssueModel';
import { Status } from '../../models/status.enum';

@Component({
  selector: 'app-issues-table',
  templateUrl: './issues-table.component.html',
  styleUrls: ['./issues-table.component.css']
})
export class IssuesTableComponent {
  readonly IssueStatus = Status;
  @Input() issues!: IssueModel[];
  error!: HttpErrorResponse;

  constructor() { }
}
