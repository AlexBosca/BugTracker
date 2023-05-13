import { Component, Input, OnInit } from '@angular/core';
import { IssueModel } from '../../models/IssueModel';

@Component({
  selector: 'app-issues-board',
  templateUrl: './issues-board.component.html',
  styleUrls: ['./issues-board.component.css']
})
export class IssuesBoardComponent implements OnInit {

  @Input() issues!: IssueModel[];
  @Input() status!: string;

  constructor() { }

  ngOnInit(): void {
  }

}
