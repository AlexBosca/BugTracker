export interface UserResetPasswordModel {
    email: string;
    currentPassword: string;
    newPassword: string;
    newPasswordRepeated: string;
}