import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpHeaders
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from 'src/app/features/feature-auth/services/auth.service';

@Injectable()
export class BasicAuthInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const currentUser = this.authService.getCurrentUser();
    const token = this.authService.getCurrentUserAuthToken();

    if((currentUser || request.url === this.authService.resetPasswordUrl) && token) {
        request = request.clone({
          setHeaders: {
            Authorization: token
          }
        });
    }

    return next.handle(request);
  }
}
