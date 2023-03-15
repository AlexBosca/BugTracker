export interface UserModel {
    userId: string;
    firstName: string;
    lastName: string;
    email: string;
    teams: Object[];
    issuesCreated: Object[];
    issuesAssigned: Object[];
    issuesClosed: Object[];
    commentsCreated: Object[];
}