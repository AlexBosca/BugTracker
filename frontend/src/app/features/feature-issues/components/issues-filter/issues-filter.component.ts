import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { IssueService } from '../../services/issue.service';

@Component({
  selector: 'app-issues-filter',
  templateUrl: './issues-filter.component.html',
  styleUrls: ['./issues-filter.component.css']
})
export class IssuesFilterComponent implements OnInit {

  issueCreationForm!: FormGroup;
  submitted: boolean = false;
  loading: boolean = false;
  error!: HttpErrorResponse;

  constructor(
    private router: Router,
    private issueService: IssueService,
    private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    // this.issueCreationForm = this.formBuilder.group({
    //   issueId: ['', Validators.required],
    //   title: ['', Validators.required],
    //   description: ['', Validators.required],
    //   reproducingSteps: ['', Validators.required],
    //   environment: ['', Validators.required],
    //   version: ['', Validators.required],
    //   priority: ['', Validators.required],
    //   project: ['', Validators.required]
    // });
  }

  // get form() {
  //   return this.issueCreationForm.controls;
  // }

  // createIssue(): void {
  //   this.submitted = true;

  //   if(!this.issueCreationForm.valid) {
  //     return;
  //   }

  //   this.loading = true;

  //   this.issueService.createIssue({
  //     issueId: this.form['issueId'].value,
  //     title: this.form['title'].value,
  //     description: this.form['description'].value,
  //     reproducingSteps: this.form['reproducingSteps'].value,
  //     environment: this.form['environment'].value,
  //     version: this.form['version'].value,
  //     priority: this.form['priority'].value,
  //   }, this.form['project'].value).subscribe({
  //     next: () => {
  //       document.getElementById('createIssueForm')?.click();
  //       window.location.reload();
  //     },
  //     error: error => {
  //       this.error = error;
  //       this.loading = false;
  //     }
  //   });
  // }

}
