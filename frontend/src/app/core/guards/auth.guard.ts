import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from 'src/app/features/feature-auth/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private router: Router, private authService: AuthService) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const isUserLoggedIn = this.authService.isUserLoggedIn();

    if(!isUserLoggedIn) {
      this.router.navigate(['/auth'], { queryParams: { returnUrl: state.url } });
    }

    return isUserLoggedIn;
  }
  
}
