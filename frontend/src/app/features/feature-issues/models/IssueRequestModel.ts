export interface IssueRequestModel {
    issueId: string;
    title: string;
    description: string;
    reproducingSteps: string;
    environment: string;
    version: string;
    priority: string;
}