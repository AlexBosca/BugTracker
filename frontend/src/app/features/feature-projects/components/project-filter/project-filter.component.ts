import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProjectService } from '../../services/project.service';
import { FilterCriteria } from 'src/app/features/feature-issues/models/FilterCriteria';
import { DatePipe, formatDate } from '@angular/common';

@Component({
  selector: 'app-project-filter',
  templateUrl: './project-filter.component.html',
  styleUrls: ['./project-filter.component.css']
})
export class ProjectFilterComponent implements OnInit {

  @Output() onFilter: EventEmitter<any> = new EventEmitter<any>();

  projectCreationForm!: FormGroup;
  submitted: boolean = false;
  loading: boolean = false;
  error!: HttpErrorResponse;
  fieldsDatatypes: any = {
    "projectKey": "string",
    "name": "string",
    "description": "string"
  };
  dataTypeToInputType: any = {
    "string": "text",
    "integer": "number",
    "date": "date"
  };
  filterCriteria: FilterCriteria = {
    filters: { },
    operators: { },
    dataTypes: { }
  };
  filterSets: FormGroup[] = [];
  appliedFilters: number = 0;

  constructor(
    private projectService: ProjectService,
    private formBuilder: FormBuilder
  ) {
    this.addFilterSet();
  }

  addFilter() {
    this.addFilterSet();
  }

  addFilterSet() {
    this.filterSets.push(this.createFilterSetFormGroup());
  }

  createFilterSetFormGroup() {
    return this.formBuilder.group({
      field: [''],
      value: [],
      operator: [''],
      dataType: []
    });
  }

  getInputType(filterSet: FormGroup) {
    return this.dataTypeToInputType[this.fieldsDatatypes[filterSet.get('field')?.value]];
  }

  applyFilter(): void {
    for(let filterSet of this.filterSets) {
      //Verify the filterSet validity
      const field = filterSet.get('field')?.value;
      this.filterCriteria.filters[field] = filterSet.get('value')?.value;
      this.filterCriteria.operators[field] = filterSet.get('operator')?.value;
      this.filterCriteria.dataTypes[field] = this.fieldsDatatypes[field] as string;
    }

    this.projectService.getFilteredProjects(this.filterCriteria).subscribe({
      next: filteredProjects => {
        this.onFilter.emit(filteredProjects);
        document.getElementById('closeProjectFilterModal')?.click();
        this.appliedFilters = Object.keys(this.filterCriteria.filters).length;
      },
      error: error => this.error = error
    });
  }

  removeFilterSet(index: number) {
    let field = this.filterSets[index].value['field'];
    this.filterSets.splice(index, 1);

    if((!this.filterCriteria.filters[field]) ||
    (!this.filterCriteria.operators[field]) ||
    (!this.filterCriteria.dataTypes[field])) {
      return;
    }

    this.removeFilter(field);
  }

  removeFilter(field: string) {
    delete this.filterCriteria.filters[field];
    delete this.filterCriteria.operators[field];
    delete this.filterCriteria.dataTypes[field];
  }

  getFilters(): any {
    return Object.keys(this.filterCriteria.filters).map(key => ({
      field: key,
      value: this.filterCriteria.filters[key]
    }));
  }

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
