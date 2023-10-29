import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { IssueService } from '../../services/issue.service';
import { FilterCriteria } from '../../models/FilterCriteria';

@Component({
  selector: 'app-issues-filter',
  templateUrl: './issues-filter.component.html',
  styleUrls: ['./issues-filter.component.css']
})
export class IssuesFilterComponent {
  
  @Output() onFilter: EventEmitter<any> = new EventEmitter<any>();

  submitted: boolean = false;
  loading: boolean = false;
  error!: HttpErrorResponse;
  fieldsDatatypes: any = {
    "issueId": "string",
    "title": "string",
    "description": "string",
    "reproducingSteps": "string",
    "environment": "string",
    "version": "string",
    "status": "string",
    "priority": "string",
    "createdByUser.userId": "string",
    "assignedUser.userId": "string",
    "modifiedByUser.userId": "string",
    "closedByUser.userId": "string",
    "tester.userId": "string",
    "project.projectKey": "string",
    "createdOn": "date",
    "assignedOn": "date",
    "modifiedOn": "date",
    "closedOn": "date"
  };
  dataTypeToInputType: any = {
    "string": "text",
    "integer": "number",
    "date": "date"
  }
  filterCriteria: FilterCriteria = {
    filters: { },
    operators: { },
    dataTypes: { }
  };
  filterSets: FormGroup[] = [];
  appliedFilters: number = 0;

  constructor(
    private issueService: IssueService,
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

    this.issueService.getFilteredIssues(this.filterCriteria).subscribe({
      next: filteredIssues => {
        this.onFilter.emit(filteredIssues);
        document.getElementById('closeIssueFilterModal')?.click();
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
}
