export interface UserModel {
    userId: string;
    firstName: string;
    lastName: string;
    email: string;
    role: string;
    accountLocked: boolean;
    enabled: boolean;
    teams: Object[];
    issuesCreated: Object[];
    issuesAssigned: Object[];
    issuesClosed: Object[];
    commentsCreated: Object[];
}