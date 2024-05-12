import { UserSlimModel } from "../../feature-auth/models/UserSlimModel";
import { IssueModel } from "../../feature-issues/models/IssueModel";

export interface ProjectModel {
    projectKey: string;
    name: string;
    description: string;
    issues: IssueModel[];
    projectManager: UserSlimModel;
    assignedUsers: Set<UserSlimModel>;
}
