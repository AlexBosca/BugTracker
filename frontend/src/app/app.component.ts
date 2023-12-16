import { Component } from '@angular/core';
import { AuthService } from './features/feature-auth/services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Bugtracker App';

  constructor(public authService: AuthService) { }
}
