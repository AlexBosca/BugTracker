import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { AuthService } from 'src/app/features/feature-auth/services/auth.service';
import { UserModel } from 'src/app/features/feature-auth/models/UserModel';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-page-user-profile',
  templateUrl: './page-user-profile.component.html',
  styleUrls: ['./page-user-profile.component.css']
})
export class PageUserProfileComponent implements OnInit {

  userDetails!: UserModel;
  selectedFile!: File;
  error!: HttpErrorResponse;

  constructor(
    private authService: AuthService,
    private userSErvice: UserService
  ) { }

  ngOnInit(): void {
    this.userDetails = this.authService.getCurrentUser();
  }

  uploadAvatar(): void {
    const payload: FormData = new FormData();

    payload.append('avatar', this.selectedFile);

    this.userSErvice.uploadAvatar(payload).subscribe({
      next: () => {
        window.location.reload();
      },
      error: error => {
        this.error = error;
      }
    });
  }

  onSelectFile(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.selectedFile = input.files[0];
      var reader = new FileReader();

      reader.readAsDataURL(this.selectedFile);

      reader.onload = (event) => {
        this.userDetails.avatarUrl = event.target?.result as string;
      }
    }

    this.uploadAvatar();
  }
}
