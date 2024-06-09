import { Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { ProjectService } from '../../services/project.service';
import { ProjectModel } from '../../models/ProjectModel';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-project-update-modal',
  templateUrl: './project-update.component.html',
  styleUrls: ['./project-update.component.css']
})
export class ProjectUpdateComponent implements OnInit {
  projectKey!: string;
  project!:ProjectModel;
  error!: HttpErrorResponse;
  projectUpdateForm!: FormGroup;
  submitted: boolean = false;
  loading: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService,
    private formBuilder: FormBuilder
  ) {

  }

  ngOnInit(): void {
    this.route.parent!.paramMap.subscribe(
      (params: ParamMap) => {
        this.projectKey = params.get('id')!;
      }
    )

    this.initializeForm();
    this.fetchProject();


  }

  initializeForm(): void {
    this.projectUpdateForm = this.formBuilder.group({
      projectKey: [this.project?.projectKey || ''],
      name: [this.project?.name || ''],
      description: [this.project?.description || ''],
      startDate: [this.project?.startDate || ''],
      targetEndDate: [this.project?.targetEndDate || ''],
      actualEndDate: [this.project?.actualEndDate || ''],
      projectManagerId: [this.project?.projectManager.userId || '']
    });
  }

  fetchProject(): void {
    if(this.projectKey) {
      this.projectService.getProject(this.projectKey)
          .subscribe({
            next: data => {
              this.project = data;
              this.setFormValues();
            },
            error: error => this.error = error
          });
    }
  }

  setFormValues(): void {
    if(this.project) {
      this.projectUpdateForm.patchValue({
        projectKey: this.project.projectKey,
        name: this.project.name,
        description: this.project.description,
        startDate: this.project.startDate,
        targetEndDate: this.project.targetEndDate,
        actualEndDate: this.project.actualEndDate,
        projectManagerId: this.project.projectManager.userId
      });
    }
  }

  get form() {
    return this.projectUpdateForm.controls;
  }

  updateProject(): void {
    this.submitted = true;

    if(!this.projectUpdateForm.valid) {
      return;
    }

    this.projectService.updateProject(this.projectKey, {
      projectKey: this.form['projectKey'].value,
      name: this.form['name'].value,
      description: this.form['description'].value,
      startDate: this.formatDateTimeForBackend(this.form['startDate'].value),
      targetEndDate: this.formatDateTimeForBackend(this.form['targetEndDate'].value),
      actualEndDate: this.formatDateTimeForBackend(this.form['actualEndDate'].value),
      projectManagerId: this.form['projectManagerId'].value
    }).subscribe({
      next: () => window.location.reload(),
      error: error => {
        this.error = error;
        this.loading = false;
      }
    });
  }

  formatDateTimeForBackend(dateTimeLocal: string): string {
    const date = new Date(dateTimeLocal);
    return date.toISOString().slice(0, 19).replace('T', ' ');
  }
}
