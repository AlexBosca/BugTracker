export interface Issue {
  issueId: string;
  title: string;
  assignee: string;
  updatedBy: string;
  projectName: string;
  projectKey: string;
  status: string;
  priority: string;
  updatedAt: Date;
  deadline: Date;
  description: string;
  createdAt: Date;
  assignedUserId: string;
}
