import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { UserModel } from 'src/app/features/feature-auth/models/UserModel';

@Component({
  selector: 'app-user-profile-modal',
  templateUrl: './user-profile-modal.component.html',
  styleUrls: ['./user-profile-modal.component.css']
})
export class UserProfileModalComponent implements OnChanges {

  @Input() userDetails!: UserModel;

  selectedFile!: File;
  editProfileForm!: FormGroup;
  submitted: boolean = false;
  loading: boolean = false;
  error!: HttpErrorResponse;

  constructor(
    private userService: UserService,
    private formBuilder: FormBuilder
  ) {
    this.editProfileForm = this.formBuilder.group({
      firstName: [''],
      lastName: [''],
      email: ['', Validators.email],
      password: [''],
      phoneNumber: [''],
      jobTitle: [''],
      department: [''],
      timezone: ['']
    });

    console.log(this.userDetails);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if(changes['userDetails']?.currentValue) {
      this.editProfileForm.patchValue({
        firstName: this.userDetails.firstName,
        lastName: this.userDetails.lastName,
        email: this.userDetails.email,
        password: '',
        phoneNumber: this.userDetails.phoneNumber,
        jobTitle: this.userDetails.jobTitle,
        department: this.userDetails.department,
        timezone: this.userDetails.timezone,
      });
    }
  }

  get form() {
    return this.editProfileForm.controls;
  }

  editProfile(): void {
    this.submitted = true;

    const payload: FormData = new FormData();
    const blob = new Blob([JSON.stringify({
      firstName: this.form['firstName'].value,
      lastName: this.form['lastName'].value,
      email: this.form['email'].value,
      // password: this.form['firstName'].value,
      phoneNumber: this.form['phoneNumber'].value,
      jobTitle: this.form['jobTitle'].value,
      department: this.form['department'].value,
      timezone: this.form['timezone'].value,
    })], {
      type: 'application/json'
    });
    payload.append('request', blob);
    payload.append('avatar', this.selectedFile);

    this.userService.updateUser(payload).subscribe({
      next: () => {
        document.getElementById('editProfileForm')?.click();
        window.location.reload();
      },
      error: error => {
        this.error = error;
        this.loading = false;
      }
    });
  }

  // onSelectFile(event: Event) {
  //   const input: HTMLInputElement = event.target as HTMLInputElement;

  //   if (input.files && input.files[0]) {
  //     this.selectedFile = input.files[0];
  //     console.log(this.selectedFile);
  //   }
  // }

  onSelectFile(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.selectedFile = input.files[0];
      var reader = new FileReader();

      reader.readAsDataURL(this.selectedFile); // read file as data url

      reader.onload = (event) => { // called once readAsDataURL is completed
        this.userDetails.avatarUrl = event.target?.result as string;
        console.log(this.userDetails);
      }
    }
  }
}
