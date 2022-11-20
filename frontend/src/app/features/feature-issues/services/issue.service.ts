import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { IssueModel } from '../models/IssueModel';

@Injectable({
  providedIn: 'root'
})
export class IssueService {
  issues: IssueModel[] = [
    {
      'createdBy': 'Ionut Predeal',
      'issueId': 'PPDS0001',
      'title': 'Some Issue Title',
      'version': '1.200.2',
      'asignee': 'Dorel Bolovaneanu',
      'priority': 'HIGH',
      'status': 'ASSIGNED',
      'createdOn': '21 Feb 2022',
      'description': 'Dummy Description',
      'reproducingSteps': 'Steps to reproduce the issue',
      'asignedOn': '21 Feb 2022',
      'environment': 'Some Environment',
      'closedBy': 'Ionut Predeal',
      'closedOn': '12 Mar 2022',
      'tester': 'Bogdan Popescu',
      'discussion': ['comment', 'another comment'],
      'project': 'Project'
    },
    {
      'createdBy': 'Bogdan Popescu',
      'issueId': 'PPDS0002',
      'title': 'Other Issue Title',
      'version': '1.200.2',
      'asignee': 'Dorel Bolovaneanu',
      'priority': 'HIGHEST',
      'status': 'NEW',
      'createdOn': '21 Feb 2022',
      'description': 'Dummy Description',
      'reproducingSteps': 'Steps to reproduce the issue',
      'asignedOn': '21 Feb 2022',
      'environment': 'Some Environment',
      'closedBy': 'Ionut Predeal',
      'closedOn': '12 Mar 2022',
      'tester': 'Bogdan Popescu',
      'discussion': ['comment', 'another comment'],
      'project': 'Project'
    },
    {
      'createdBy': 'Dorel Bolovaneanu',
      'issueId': 'PPDS0003',
      'title': 'Some Issue Title',
      'version': '1.200.2',
      'asignee': 'Bogdan Popescu',
      'priority': 'REJECTED',
      'status': 'MEDIUM',
      'createdOn': '21 Feb 2022',
      'description': 'Dummy Description',
      'reproducingSteps': 'Steps to reproduce the issue',
      'asignedOn': '21 Feb 2022',
      'environment': 'Some Environment',
      'closedBy': 'Ionut Predeal',
      'closedOn': '12 Mar 2022',
      'tester': 'Bogdan Popescu',
      'discussion': ['comment', 'another comment'],
      'project': 'Project'
    },
    {
      'createdBy': 'Dorel Bolovaneanu',
      'issueId': 'PPDS0004',
      'title': 'Some Issue Title',
      'version': '1.200.2',
      'asignee': 'Ionut Predeal',
      'priority': 'HIGH',
      'status': 'OPEN',
      'createdOn': '21 Feb 2022',
      'description': 'Dummy Description',
      'reproducingSteps': 'Steps to reproduce the issue',
      'asignedOn': '21 Feb 2022',
      'environment': 'Some Environment',
      'closedBy': 'Ionut Predeal',
      'closedOn': '12 Mar 2022',
      'tester': 'Bogdan Popescu',
      'discussion': ['comment', 'another comment'],
      'project': 'Project'
    }
  ];

  constructor() { }

  getIssues(): Observable<any[]> {
    return of(this.issues);
  }
}
