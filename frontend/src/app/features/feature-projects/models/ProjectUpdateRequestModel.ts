export interface ProjectUpdateRequestModel {
  projectKey: string;
  name: string;
  description: string;
  startDate: string;
  targetEndDate: string;
  actualEndDate: string;
  projectManagerId: string;
}
