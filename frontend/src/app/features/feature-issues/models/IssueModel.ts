export interface IssueModel {
    issueId: string;
    title: string;
    description: string;
    reproducingSteps: string;
    environment: string;
    version: string;
    status: string;   
    priority: string; 
    createdBy: string;
    createdOn: string;
    asignee: string;
    asignedOn: string;
    closedBy: string;
    closedOn: string;
    tester: string;
    discussion: string[];
    project: string;
}