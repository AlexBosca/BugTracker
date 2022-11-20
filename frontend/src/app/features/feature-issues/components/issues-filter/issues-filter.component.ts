import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-issues-filter',
  templateUrl: './issues-filter.component.html',
  styleUrls: ['./issues-filter.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class IssuesFilterComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
