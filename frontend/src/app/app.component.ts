import { Component } from '@angular/core';
import { LayoutModule } from './layout/layout.module';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [LayoutModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
}
