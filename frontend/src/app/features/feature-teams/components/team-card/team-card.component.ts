import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { TeamModel } from '../../models/TeamModel';

@Component({
  selector: 'app-team-card',
  templateUrl: './team-card.component.html',
  styleUrls: ['./team-card.component.css']
})
export class TeamCardComponent implements OnInit {

  @Input() team!: TeamModel;

  constructor() { }

  ngOnInit(): void {
  }

}
