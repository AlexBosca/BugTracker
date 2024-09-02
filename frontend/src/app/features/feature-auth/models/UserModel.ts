export interface UserModel {
    userId: string;
    firstName: string;
    lastName: string;
    email: string;
    role: string;
    avatarUrl: string;
    accountLocked: boolean;
    enabled: boolean;
    issuesCreated: Object[];
    issuesAssigned: Object[];
    issuesClosed: Object[];
    commentsCreated: Object[];
}
