import { Status } from "./status.enum";

export class StatusCategory {
    static readonly TO_DO = [Status.NEW, Status.ASSIGNED];
    static readonly IN_PROGRESS = [Status.OPEN, Status.REOPENED, Status.PENDING_RETEST, Status.RETEST, Status.FIXED];
    static readonly DONE = [Status.VERIFIED, Status.CLOSED, Status.DEFERRED, Status.DUPLICATE, Status.REJECTED, Status.NOT_A_BUG];
}