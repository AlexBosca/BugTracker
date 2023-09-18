import { HttpErrorResponse } from '@angular/common/http';
import { Component, ElementRef, Input, OnChanges, OnInit, SimpleChanges, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { ModalType } from 'src/app/core/enums/modal-type.enum';
import { UserRegistrationModel } from 'src/app/features/feature-auth/models/UserRegistrationModel';
import { UserModel } from 'src/app/features/feature-auth/models/UserModel';

@Component({
  selector: 'app-user-modal',
  templateUrl: './user-modal.component.html',
  styleUrls: ['./user-modal.component.css']
})
export class UserModalComponent implements OnInit {

  @Input() userModalType!: ModalType ;

  userCreationForm!: FormGroup;
  userUpdateForm!: FormGroup;
  submitted: boolean = false;
  loading: boolean = false;
  error!: HttpErrorResponse;

  constructor(
    private userService: UserService,
    private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.userCreationForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.required],
      password: ['', Validators.required],
      role: ['', Validators.required],
    });
  }

  get form() {
    return this.userCreationForm.controls;
  }

  createUser(): void {
    this.submitted = true;

    if(!this.userCreationForm.valid) {
      return;
    }

    this.userService.createUser({
      firstName: this.form['firstName'].value,
      lastName: this.form['lastName'].value,
      email: this.form['email'].value,
      password: this.form['password'].value,
      role: this.form['role'].value
    }).subscribe({
      next: () => {
        document.getElementById('createUserForm')?.click();
        window.location.reload();
      },
      error: error => {
        this.error = error;
        this.loading = false;
      }
    });
  }
}
