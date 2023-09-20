import { Component, OnInit } from '@angular/core';
import { UserModel } from 'src/app/features/feature-auth/models/UserModel';
import { UserService } from '../../services/user.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  templateUrl: './page-users.component.html',
  styleUrls: ['./page-users.component.css']
})
export class PageUsersComponent implements OnInit {
  users: UserModel[] = [];
  error!: HttpErrorResponse;

  constructor(
    private usersService: UserService
  ) { }

  ngOnInit(): void {
    this.fetchUsers();
  }

  fetchUsers(): void {
    this.usersService.getUsers()
        .subscribe({
          next: data => {
            this.users = data;
          },
          error: error => this.error = error
        });
  }
}
