export interface UserModel {
    userId: string;
    firstName: string;
    lastName: string;
    email: string;
    role: string;
    accountLocked: boolean;
    enabled: boolean;
    avatarUrl: string;
    phoneNumber: string;
    jobTitle: string;
    department: string;
    timezone: string;
    issuesCreated: Object[];
    issuesAssigned: Object[];
    issuesClosed: Object[];
    commentsCreated: Object[];
}
