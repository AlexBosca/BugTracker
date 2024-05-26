import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProjectService } from '../../services/project.service';

@Component({
  selector: 'app-project-modal',
  templateUrl: './project-modal.component.html',
  styleUrls: ['./project-modal.component.css']
})
export class ProjectModalComponent implements OnInit {

  projectCreationForm!: FormGroup;
  submitted: boolean = false;
  loading: boolean = false;
  error!: HttpErrorResponse;

  constructor(
    private projectService: ProjectService,
    private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.projectCreationForm = this.formBuilder.group({
      projectKey: ['', Validators.required],
      name: ['', Validators.required],
      description: ['', Validators.required],
      startDate: ['', Validators.required],
      targetEndDate: ['', Validators.required]
    });
  }

  get form() {
    return this.projectCreationForm.controls;
  }

  createProject(): void {
    this.submitted = true;

    if(!this.projectCreationForm.valid) {
      return;
    }

    this.loading = true;

    this.projectService.createProject({
      projectKey: this.form['projectKey'].value,
      name: this.form['name'].value,
      description: this.form['description'].value,
      startDate: this.formatDateTimeForBackend(this.form['startDate'].value),
      targetEndDate: this.formatDateTimeForBackend(this.form['targetEndDate'].value)
    }).subscribe({
      next: () => {
        document.getElementById('createProjectForm')?.click();
        window.location.reload();
      },
      error: error => {
        this.error = error;
        this.loading = true;
      }
    });
  }

  formatDateTimeForBackend(dateTimeLocal: string): string {
    const date = new Date(dateTimeLocal);
    return date.toISOString().slice(0, 19).replace('T', ' ');
  }
}
