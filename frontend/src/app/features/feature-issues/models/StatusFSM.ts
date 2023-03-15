import { Status } from "./status.enum";

export class StatusFSM {

    private status: Status;
    // private possibleTransitions: StatusFSM[];

    constructor(status: Status) {
        this.status = status;
    }

    // public transitionTo(newState: Status) {
    //     if(!this.canTransitionTo(newState)) {
    //         return null;
    //     }

    //     reutrn newState;
    // }

    // public canTransitionTo(anotherState: Status) {
    //     return this.possibleTransitions.find(state => state.status == anotherState);
    // }

    // private allowTransitionTo(allowableStates: StatusFSM[]) {
    //     this.possibleTransitions = allowableStates;
    // }
}