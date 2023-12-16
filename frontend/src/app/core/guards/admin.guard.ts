import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from 'src/app/features/feature-auth/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {
  constructor(private router: Router, private authService: AuthService) { }

  canActivate(route: ActivatedRouteSnapshot,state: RouterStateSnapshot) {
    const expectedRole = route.data['role'];

    if(!this.authService.hasRole(expectedRole)) {
      this.router.navigate(['/access-denied']);
      return false;
    }

    return true;
  }
}
