import { UserModel } from "../../feature-auth/models/UserModel";
import { IssueModel } from "./IssueModel";

export interface IssueComment {
    comment: string;
    createdOn: Date;
    createdByUser: UserModel;
    createdOnIssue: IssueModel;
}