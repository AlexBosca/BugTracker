import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth.service';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

describe('AuthService', () => {
  let authService: AuthService;
  let httpTesting: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    authService = TestBed.inject(AuthService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTesting.verify();
  });

  it('should be created', () => {
    const service = TestBed.inject(AuthService);
    expect(service).toBeTruthy();
  });

  it('should login a user', () => {
    const service = TestBed.inject(AuthService);
    let called = false;
    service.login({username: 'testuser', password: 'password123'}).subscribe(() => {
      called = true;
    });

    const req = httpTesting.expectOne('https://localhost:8081/api/v1/bug-tracker/auth/login');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({
      username: 'testuser',
      password: 'password123'
    });

    req.flush(null);

    expect(called).toBe(true);
  });

  it('should register a user', () => {
    const service = TestBed.inject(AuthService);
    let called = false;
    const registerRequest = {
      username: 'newuser',
      email: 'new.user@mail.com',
      password: 'newpassword'
    };

    service.register(registerRequest).subscribe(() => {
      called = true;
    });

    const req = httpTesting.expectOne('https://localhost:8081/api/v1/bug-tracker/auth/register');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(registerRequest);

    req.flush(null);

    expect(called).toBe(true);
  });

  it('should logout a user', () => {
    spyOn(authService['router'], 'navigate');

    authService.logout();

    const req = httpTesting.expectOne('https://localhost:8081/api/v1/bug-tracker/auth/logout');
    expect(req.request.method).toBe('POST');

    req.flush(null, { status: 200, statusText: 'OK' });

    expect(authService['router'].navigate).toHaveBeenCalledWith(['/auth']);
  });

  it('should handle logout error', () => {
    spyOn(console, 'error');

    authService.logout();

    const req = httpTesting.expectOne('https://localhost:8081/api/v1/bug-tracker/auth/logout');
    expect(req.request.method).toBe('POST');

    req.flush('Logout failed', { status: 500, statusText: 'Server Error' });

    expect(console.error).toHaveBeenCalledWith('Logout failed', jasmine.anything());
  });

  it('should request password reset', () => {
    let called = false;

    authService.passwordReset('user.name@email.com').subscribe(() => {
      called = true;
    });

    const req = httpTesting.expectOne('https://localhost:8081/api/v1/bug-tracker/auth/password-reset-request');

    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ email: 'user.name@email.com' });

    req.flush(null);

    expect(called).toBe(true);
  });

  it('should validate reset token', () => {
    let called = false;
    const token = 'reset-token-123';

    authService.validateResetToken(token).subscribe(() => {
      called = true;
    });

    const req = httpTesting.expectOne(`https://localhost:8081/api/v1/bug-tracker/auth/password-reset?token=${token}`);

    expect(req.request.method).toBe('GET');

    req.flush(null);

    expect(called).toBe(true);
  });

  it('should update password', () => {
    let called = false;
    const token = 'reset-token-123';
    const newPassword = 'newSecurePassword!';

    authService.passwordUpdate(token, newPassword).subscribe(() => {
      called = true;
    });

    const req = httpTesting.expectOne('https://localhost:8081/api/v1/bug-tracker/auth/password-reset');

    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ token, newPassword });

    req.flush(null);

    expect(called).toBe(true);
  });

  it('should return authentication status', () => {
    sessionStorage.setItem('isAuthenticated', 'true');

    expect(authService.isAuthenticated()).toBeTrue();

    sessionStorage.setItem('isAuthenticated', 'false');

    expect(authService.isAuthenticated()).toBeFalse();
  });

  it('should return false if isAuthenticated is not set', () => {
    sessionStorage.removeItem('isAuthenticated');

    expect(authService.isAuthenticated()).toBeFalse();
  });
});
