import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { UserModel } from 'src/app/features/feature-auth/models/UserModel';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-users-table',
  templateUrl: './users-table.component.html',
  styleUrls: ['./users-table.component.css']
})
export class UsersTableComponent implements OnInit {
  
  @Input() users!: UserModel[];
  error!: HttpErrorResponse;
  selectedUser!: UserModel;

  constructor(
    private userService: UserService
  ) { }

  ngOnInit(): void {
  }

  convertUserRole(userRole: string): string {
    let role = '';

    switch (userRole) {
      case 'ROLE_ADMIN':
        role = 'Admin';
        break;

      case 'ROLE_DEVELOPER':
        role = 'Developer';
        break;

      case 'ROLE_TESTER':
        role = 'Tester';
        break;

      case 'ROLE_SCRUM_MASTER':
        role = 'Scrum Master';
        break;
        case 'ROLE_PROJECT_MANAGER':
      role = 'Project Manager';
      break;

      case 'ROLE_VISITOR':
        role = 'Visitor';
        break;
    
      default:
        break;
    }

    return role;
  }

  selectUserToUpdate(user: UserModel): void {
    this.selectedUser = user;
  }

  disableEnableUserAccount(user: UserModel): void {
    if(user.enabled) {
      this.userService.disableAccount(user.userId)
      .subscribe({
        next: () => {
          window.location.reload()
        },
        error: error => this.error = error
      });
    } else {
      this.userService.enableAccount(user.userId)
      .subscribe({
        next: () => {
          window.location.reload()
        },
        error: error => this.error = error
      });
    }
  }

  unlockUserAccount(user: UserModel): void {
    this.userService.unlockAccount(user.userId)
      .subscribe({
        next: () => {
          window.location.reload()
        },
        error: error => this.error = error
      });
  }

  deleteUser(user: UserModel): void {
    this.userService.deleteUser(user.userId)
      .subscribe({
        next: () => {
          window.location.reload()
        },
        error: error => this.error = error
      });
  }

  sortFn(userA: UserModel, userB: UserModel): number {
    if(userA.userId < userB.userId) return -1;
    if(userA.userId === userB.userId) return 0;
    return 1;
  }
}
