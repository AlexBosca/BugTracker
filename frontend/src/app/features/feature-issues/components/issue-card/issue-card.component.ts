import { Component, Input, OnInit } from '@angular/core';
import { IssueModel } from '../../models/IssueModel';

@Component({
  selector: 'app-issue-card',
  templateUrl: './issue-card.component.html',
  styleUrls: ['./issue-card.component.css']
})
export class IssueCardComponent implements OnInit {

  @Input() issue!: IssueModel;

  constructor() { }

  ngOnInit(): void {
  }

}
