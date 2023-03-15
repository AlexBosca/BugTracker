import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from 'src/app/features/feature-auth/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor (private router: Router, private authService: AuthService) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const isUserLoggedIn = this.authService.isUserLoggedIn();

    if(!isUserLoggedIn) {
      this.router.navigate(['/auth/login'], { queryParams: { returnUrl: state.url } });
    }

    return isUserLoggedIn;
  }
  
}
