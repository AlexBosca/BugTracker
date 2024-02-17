import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-account-confirmation',
  templateUrl: './account-confirmation.component.html',
  styleUrls: ['./account-confirmation.component.css']
})
export class AccountConfirmationComponent implements OnInit {

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const token = params['token'];

      if(token) {
        this.authService.conofirmAccount(token).subscribe(
          () => {
            this.router.navigate(
              ['/login'],
              {
                state: {
                  data: 'Email address successfully confirmed'
                }
              }
            );
          }
        );
      }
    });
  }

}
