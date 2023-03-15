import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { TeamModel } from '../../models/TeamModel';

@Component({
  selector: 'app-teams-grid',
  templateUrl: './teams-grid.component.html',
  styleUrls: ['./teams-grid.component.css']
})
export class TeamsGridComponent implements OnInit {

  @Input() teams!: TeamModel[];

  constructor() { }

  ngOnInit(): void {
  }

}
