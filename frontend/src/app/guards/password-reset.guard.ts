import { inject } from '@angular/core';
import { Router, type CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { firstValueFrom } from 'rxjs';

export const passwordResetGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = route.queryParamMap.get('token');
  if(token) {
    return firstValueFrom(authService.validateResetToken(token))
      .then(() => true)
      .catch(() => {
        return router.createUrlTree(['/auth'], {
          queryParams: { returnUrl: state.url }
        });
      });
  }

  return router.createUrlTree(['/auth'], {
    queryParams: { returnUrl: state.url }
  });
};
