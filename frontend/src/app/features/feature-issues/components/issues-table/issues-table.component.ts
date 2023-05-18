import { formatDate } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/features/feature-auth/services/auth.service';
import { IssueModel } from '../../models/IssueModel';
import { Status } from '../../models/status.enum';
import { IssueService } from '../../services/issue.service';

@Component({
  selector: 'app-issues-table',
  templateUrl: './issues-table.component.html',
  styleUrls: ['./issues-table.component.css']
})
export class IssuesTableComponent implements OnInit {
  readonly IssueStatus = Status;
  @Input() issues!: IssueModel[];
  error!: HttpErrorResponse;

  constructor() { }

  ngOnInit(): void {
  }
}
