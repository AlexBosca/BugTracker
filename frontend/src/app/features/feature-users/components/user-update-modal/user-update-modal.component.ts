import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { UserModel } from 'src/app/features/feature-auth/models/UserModel';
import { UserService } from '../../services/user.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-user-update-modal',
  templateUrl: './user-update-modal.component.html',
  styleUrls: ['./user-update-modal.component.css']
})
export class UserUpdateModalComponent implements OnChanges {

  @Input() user!: UserModel;

  userUpdateForm!: FormGroup;
  submitted: boolean = false;
  loading: boolean = false;
  error!: HttpErrorResponse;

  constructor(
    private userService: UserService,
    private formBuilder: FormBuilder
  ) {
    this.userUpdateForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.required],
      password: ['', Validators.required],
      role: ['', Validators.required],
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if(changes['user']?.currentValue) {
      this.userUpdateForm.patchValue({
        firstName: this.user.firstName,
        lastName: this.user.lastName,
        email: this.user.email,
        password: '',
        role: this.convertUserRoleToCode(this.user.role),
      });
    }
  }

  private convertUserRoleToCode(userRole: string): string {
    let role = '';

    switch (userRole) {
      case 'ROLE_ADMIN':
        role = 'A';
        break;

      case 'ROLE_DEVELOPER':
        role = 'D';
        break;

      case 'ROLE_TESTER':
        role = 'T';
        break;

      case 'ROLE_SCRUM_MASTER':
        role = 'SM';
        break;
        case 'ROLE_PROJECT_MANAGER':
      role = 'PM';
      break;

      case 'ROLE_VISITOR':
        role = 'V';
        break;
    
      default:
        break;
    }

    return role;
  }

  get form() {
    return this.userUpdateForm.controls;
  }

  updateUser(): void {
    this.submitted = true;

    if(!this.userUpdateForm.valid) {
      return;
    }

    this.userService.updateUser(this.user.userId, {
      firstName: this.form['firstName'].value,
      lastName: this.form['lastName'].value,
      email: this.form['email'].value,
      password: this.form['password'].value,
      role: this.form['role'].value
    }).subscribe({
      next: () => {
        document.getElementById('updateUserForm')?.click();
        window.location.reload();
      },
      error: error => {
        this.error = error;
        this.loading = false;
      }
    });
  }
}
