import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-page-not-found',
  imports: [],
  template: `<p>page-not-found works!</p>`,
  styleUrl: './page-not-found.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PageNotFoundComponent { }
