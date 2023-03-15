import { ProjectSlimModel } from "../../feature-projects/models/ProjectSlimModel";
import { UserSlimModel } from "../../feature-auth/models/UserSlimModel";
import { Status } from "./status.enum";
import { Priority } from "./priority.enum";
import { IssueComment } from "./IssueComment";

export interface IssueModel {
    issueId: string;
    title: string;
    description: string;
    reproducingSteps: string;
    environment: string;
    version: string;
    status: Status;   
    priority: Priority; 
    createdByUser: UserSlimModel;
    createdOn: Date;
    assignedUser: UserSlimModel | null;
    assignedOn: Date | null;
    closedByUser: UserSlimModel | null;
    closedOn: Date | null;
    tester: UserSlimModel | null;
    discussion: IssueComment[];
    project: ProjectSlimModel;
}