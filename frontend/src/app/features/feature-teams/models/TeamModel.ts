import { UserModel } from "../../feature-auth/models/UserModel";
import { ProjectModel } from "../../feature-projects/models/ProjectModel";

export interface TeamModel {
    teamId: string;
    name: string;
    projects: ProjectModel[];
    colleagues: UserModel[];
}