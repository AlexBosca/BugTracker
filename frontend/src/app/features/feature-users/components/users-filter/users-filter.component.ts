import { Component, OnInit } from '@angular/core';
import { ModalType } from 'src/app/core/enums/modal-type.enum';

@Component({
  selector: 'app-users-filter',
  templateUrl: './users-filter.component.html',
  styleUrls: ['./users-filter.component.css']
})
export class UsersFilterComponent implements OnInit {

  modalType = ModalType;

  constructor() { }

  ngOnInit(): void {
  }

}
