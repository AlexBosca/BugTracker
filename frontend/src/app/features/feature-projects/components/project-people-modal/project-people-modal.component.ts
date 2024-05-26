import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProjectService } from '../../services/project.service';
import { UserService } from 'src/app/features/feature-users/services/user.service';
import { UserModel } from 'src/app/features/feature-auth/models/UserModel';
import { iif } from 'rxjs';

@Component({
  selector: 'app-project-people-modal',
  templateUrl: './project-people-modal.component.html',
  styleUrls: ['./project-people-modal.component.css']
})
export class ProjectPeopleModalComponent implements OnInit {

  @Input() projectKey!: string;
  projectPeopleForm!: FormGroup;
  submitted: boolean = false;
  loading: boolean = false;
  showSuggestions: boolean = false;
  suggestions: UserModel[] = [];
  peopleToAdd: UserModel[] = [];
  error!: HttpErrorResponse;

  constructor(
    private projectService: ProjectService,
    private userService: UserService,
    private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.projectPeopleForm = this.formBuilder.group({
      username: ['', Validators.required]
    });

    this.projectPeopleForm.get('username')?.valueChanges
    .subscribe({
      next: username => {
        this.suggestions = [];

        if(username.length >= 3) {
          this.userService.getUsers()
          .subscribe({
            next: data => {
              for(let user of data) {
                if(this.peopleToAdd.indexOf(user) === -1){
                  this.suggestions.push(user);
                  this.showSuggestions = true;
                }
              }
            },
            error: error => this.error = error
          });
        } else {
          this.suggestions = [];
          this.showSuggestions = false;
        }
      }
    });
  }

  get form() {
    return this.projectPeopleForm.controls;
  }

  addSuggestion(suggestion: UserModel): void {
    this.peopleToAdd.push(suggestion);
    this.suggestions = this.suggestions.filter(user => user.userId !== suggestion.userId);
  }

  removeUser(user: UserModel): void {
    this.peopleToAdd = this.peopleToAdd.filter(addedUser => addedUser.userId !== user.userId);
  }

  addPeople(): void {
    this.submitted = true;

    if(!this.peopleToAdd.length) {
      return;
    }

    this.loading = true;

    iif(
      () => this.peopleToAdd.length === 1,
      this.projectService.addUserOnProject(
        this.projectKey,
        this.peopleToAdd[0].userId
      ),
      this.projectService.addUsersOnProject(
        this.projectKey,
        this.peopleToAdd.map(user => user.userId)
      )
    )
    .subscribe({
      next: () => {
        document.getElementById('createProjectForm')?.click();
        window.location.reload();
      },
      error: error => {
        this.error = error;
        this.loading = false;
      }
    });
  }

  onClose(): void {
    this.peopleToAdd = [];
    this.suggestions = [];
    this.projectPeopleForm.reset({username: ''});
  }
}
