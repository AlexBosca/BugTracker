import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { SidebarComponent } from './sidebar/sidebar.component';
import { TopNavComponent } from './top-nav/top-nav.component';
import { MaterialModule } from '../shared/material.import';
import { FooterComponent } from "./footer/footer.component";

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterModule, MaterialModule, TopNavComponent, SidebarComponent, FooterComponent],

  template: `
    <div class="layout">
      <app-top-nav></app-top-nav>
      <div class="content">
        <app-sidebar></app-sidebar>
        <main>
          <router-outlet></router-outlet>
          <!-- <div class="margins">
            <mat-card appearance="outlined">
              <mat-card-header>
                <mat-card-title>Bug Tracker Overview</mat-card-title>
              </mat-card-header>
              <mat-card-content>
                <p>{{longText}}</p>
              </mat-card-content>
            </mat-card>
          </div>

          <div class="margins">
            <mat-card appearance="outlined">
              <mat-card-header>
                <mat-card-title>Key Metrics</mat-card-title>
              </mat-card-header>
              <mat-card-content>
                <p>{{longText}}</p>
              </mat-card-content>
            </mat-card>
          </div>

          <div class="margins">
            <mat-card appearance="outlined">
              <mat-card-header>
                <mat-card-title>Recent Activity</mat-card-title>
              </mat-card-header>
              <mat-card-content>
                <p>{{longText}}</p>
              </mat-card-content>
            </mat-card>
          </div>

          <div class="margins">
            <mat-card appearance="outlined">
              <mat-card-header>
                <mat-card-title>Bugs by Severity</mat-card-title>
              </mat-card-header>
              <mat-card-content>
                <p>{{longText}}</p>
              </mat-card-content>
              <mat-card-footer class="example-card-footer">
                <mat-chip-set aria-label="Bugs by Severity">
                  <mat-chip color="warn">High</mat-chip>
                  <mat-chip color="accent">Medium</mat-chip>
                  <mat-chip color="primary">Low</mat-chip>
                </mat-chip-set>
              </mat-card-footer>
            </mat-card>
          </div>

          <div class="margins">
            <mat-card appearance="outlined">
              <mat-card-header>
                <mat-card-title>Open Bugs</mat-card-title>
              </mat-card-header>
              <mat-card-content>
                <p>{{longText}}</p>
              </mat-card-content>
            </mat-card>
          </div> -->

          <!-- <div class="margins">
            <mat-card appearance="outlined">
              <mat-card-header>
                <mat-card-title>Chihuahua</mat-card-title>
              </mat-card-header>
              <mat-card-content>
                <p>{{longText}}</p>
              </mat-card-content>
              <mat-card-footer class="example-card-footer">
                <mat-chip-set aria-label="Chihuahua traits">
                  <mat-chip>charming</mat-chip>
                  <mat-chip>graceful</mat-chip>
                  <mat-chip>sassy</mat-chip>
                </mat-chip-set>
              </mat-card-footer>
            </mat-card>
          </div>

          <div class="margins">
            <mat-card appearance="outlined">
              <mat-card-header>
                <mat-card-title>Chihuahua</mat-card-title>
              </mat-card-header>
              <mat-card-content>
                <p>{{longText}}</p>
              </mat-card-content>
              <mat-card-footer class="example-card-footer">
                <mat-chip-set aria-label="Chihuahua traits">
                  <mat-chip>charming</mat-chip>
                  <mat-chip>graceful</mat-chip>
                  <mat-chip>sassy</mat-chip>
                </mat-chip-set>
              </mat-card-footer>
            </mat-card>
          </div>

          <div class="margins">
            <mat-card appearance="outlined">
              <mat-card-header>
                <mat-card-title>Chihuahua</mat-card-title>
              </mat-card-header>
              <mat-card-content>
                <p>{{longText}}</p>
              </mat-card-content>
              <mat-card-footer class="example-card-footer">
                <mat-chip-set aria-label="Chihuahua traits">
                  <mat-chip>charming</mat-chip>
                  <mat-chip>graceful</mat-chip>
                  <mat-chip>sassy</mat-chip>
                </mat-chip-set>
              </mat-card-footer>
            </mat-card>
          </div>

          <div class="margins">
            <mat-card appearance="outlined">
              <mat-card-header>
                <mat-card-title>Chihuahua</mat-card-title>
              </mat-card-header>
              <mat-card-content>
                <p>{{longText}}</p>
              </mat-card-content>
              <mat-card-footer class="example-card-footer">
                <mat-chip-set aria-label="Chihuahua traits">
                  <mat-chip>charming</mat-chip>
                  <mat-chip>graceful</mat-chip>
                  <mat-chip>sassy</mat-chip>
                </mat-chip-set>
              </mat-card-footer>
            </mat-card>
          </div>

          <div class="margins">
            <mat-card appearance="outlined">
              <mat-card-header>
                <mat-card-title>Chihuahua</mat-card-title>
              </mat-card-header>
              <mat-card-content>
                <p>{{longText}}</p>
              </mat-card-content>
              <mat-card-footer class="example-card-footer">
                <mat-chip-set aria-label="Chihuahua traits">
                  <mat-chip>charming</mat-chip>
                  <mat-chip>graceful</mat-chip>
                  <mat-chip>sassy</mat-chip>
                </mat-chip-set>
              </mat-card-footer>
            </mat-card>
          </div>

          <div class="margins">
            <mat-card appearance="outlined">
              <mat-card-header>
                <mat-card-title>Chihuahua</mat-card-title>
              </mat-card-header>
              <mat-card-content>
                <p>{{longText}}</p>
              </mat-card-content>
              <mat-card-footer class="example-card-footer">
                <mat-chip-set aria-label="Chihuahua traits">
                  <mat-chip>charming</mat-chip>
                  <mat-chip>graceful</mat-chip>
                  <mat-chip>sassy</mat-chip>
                </mat-chip-set>
              </mat-card-footer>
            </mat-card>
          </div>

          <div class="margins">
            <mat-card appearance="outlined">
              <mat-card-header>
                <mat-card-title>Chihuahua</mat-card-title>
              </mat-card-header>
              <mat-card-content>
                <p>{{longText}}</p>
              </mat-card-content>
              <mat-card-footer class="example-card-footer">
                <mat-chip-set aria-label="Chihuahua traits">
                  <mat-chip>charming</mat-chip>
                  <mat-chip>graceful</mat-chip>
                  <mat-chip>sassy</mat-chip>
                </mat-chip-set>
              </mat-card-footer>
            </mat-card>
          </div>

          <div class="margins">
            <mat-card appearance="outlined">
              <mat-card-header>
                <mat-card-title>Chihuahua</mat-card-title>
              </mat-card-header>
              <mat-card-content>
                <p>{{longText}}</p>
              </mat-card-content>
              <mat-card-footer class="example-card-footer">
                <mat-chip-set aria-label="Chihuahua traits">
                  <mat-chip>charming</mat-chip>
                  <mat-chip>graceful</mat-chip>
                  <mat-chip>sassy</mat-chip>
                </mat-chip-set>
              </mat-card-footer>
            </mat-card>
          </div>

          <div class="margins">
            <mat-card appearance="outlined">
              <mat-card-header>
                <mat-card-title>Chihuahua</mat-card-title>
              </mat-card-header>
              <mat-card-content>
                <p>{{longText}}</p>
              </mat-card-content>
              <mat-card-footer class="example-card-footer">
                <mat-chip-set aria-label="Chihuahua traits">
                  <mat-chip>charming</mat-chip>
                  <mat-chip>graceful</mat-chip>
                  <mat-chip>sassy</mat-chip>
                </mat-chip-set>
              </mat-card-footer>
            </mat-card>
          </div>

          <div class="margins">
            <mat-card appearance="outlined">
              <mat-card-header>
                <mat-card-title>Chihuahua</mat-card-title>
              </mat-card-header>
              <mat-card-content>
                <p>{{longText}}</p>
              </mat-card-content>
              <mat-card-footer class="example-card-footer">
                <mat-chip-set aria-label="Chihuahua traits">
                  <mat-chip>charming</mat-chip>
                  <mat-chip>graceful</mat-chip>
                  <mat-chip>sassy</mat-chip>
                </mat-chip-set>
              </mat-card-footer>
            </mat-card>
          </div> -->
          <app-footer></app-footer>
        </main>
      </div>
    </div>
  `,
  styles: [`
    app-sidebar {
      padding: 5px;
      border-right: 1px solid var(--mat-sys-surface-variant);
    }
    app-top-nav {
      border-bottom: 1px solid var(--mat-sys-surface-variant);
    }
    app-footer {
      text-align: center;
      // position: fixed;
      padding: 1rem;
      color: #333;
      bottom: 0;
    }
    mat-card-title, mat-card-content {
      color: var(--mat-sys-on-surface-variant);
    }
    .margins {
      margin: 0.5rem;
    }
    .layout {
      display: flex;
      flex-direction: column;
      height: 100vh;
    }
    .content {
      display: flex;
      flex: 1;
    }
    main {
      flex: 1;
    }
    .example-card {
      // max-width: 400px;
      // margin: 20px;
    }
    .example-card-footer {
      padding: 16px;
    }
  `]
})
export class LayoutComponent {
  longText = `Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam nec purus ac libero ultrices aliquam.
    Nullam nec purus ac libero ultrices aliquam. Nullam nec purus ac libero ultrices aliquam. Nullam nec purus ac libero ultrices aliquam.
    Nullam nec purus ac libero ultrices aliquam. Nullam nec purus ac libero ultrices aliquam. Nullam nec purus ac libero ultrices aliquam.
    Nullam nec purus ac libero ultrices aliquam. Nullam nec purus ac libero ultrices aliquam. Nullam nec purus ac libero ultrices aliquam.`;
}
