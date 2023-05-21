import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { IssueService } from '../../services/issue.service';

@Component({
  selector: 'app-issue-modal',
  templateUrl: './issue-modal.component.html',
  styleUrls: ['./issue-modal.component.css']
})
export class IssueModalComponent implements OnInit {

  issueCreationForm!: FormGroup;
  submitted: boolean = false;
  loading: boolean = false;
  error!: HttpErrorResponse;

  constructor(
    private issueService: IssueService,
    private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.issueCreationForm = this.formBuilder.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      reproducingSteps: ['', Validators.required],
      environment: ['', Validators.required],
      version: ['', Validators.required],
      priority: ['', Validators.required],
      project: ['', Validators.required]
    });
  }

  get form() {
    return this.issueCreationForm.controls;
  }

  createIssue(): void {
    this.submitted = true;

    if(!this.issueCreationForm.valid) {
      return;
    }

    this.loading = true;

    this.issueService.createIssue({
      title: this.form['title'].value,
      description: this.form['description'].value,
      reproducingSteps: this.form['reproducingSteps'].value,
      environment: this.form['environment'].value,
      version: this.form['version'].value,
      priority: this.form['priority'].value,
    }, this.form['project'].value).subscribe({
      next: () => {
        document.getElementById('createIssueForm')?.click();
        window.location.reload();
      },
      error: error => {
        this.error = error;
        this.loading = false;
      }
    });
  }
}
