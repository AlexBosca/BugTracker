import { IssueModel } from "../../feature-issues/models/IssueModel";
import { TeamModel } from "../../feature-teams/models/TeamModel";

export interface ProjectModel {
    projectId: string;
    name: string;
    description: string;
    teams: TeamModel[];
    issues: IssueModel[];
}