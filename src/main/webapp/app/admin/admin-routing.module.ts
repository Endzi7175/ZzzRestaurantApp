import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {Authority} from "../config/authority.constants";

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'user-management',
        loadChildren: () => import('./user-management/user-management.module').then(m => m.UserManagementModule),
        data: {
          pageTitle: 'userManagement.home.title',
          authorities: [Authority.ADMIN, Authority.MANAGER],
        },
      },
      {
        path: 'docs',
        data: {
          authorities: [Authority.ADMIN],
        },
        loadChildren: () => import('./docs/docs.module').then(m => m.DocsModule),
      },
      {
        path: 'configuration',
        data: {
          authorities: [Authority.ADMIN],
        },
        loadChildren: () => import('./configuration/configuration.module').then(m => m.ConfigurationModule),
      },
      {
        path: 'health',
        data: {
          authorities: [Authority.ADMIN],
        },
        loadChildren: () => import('./health/health.module').then(m => m.HealthModule),
      },
      {
        path: 'logs',
        data: {
          authorities: [Authority.ADMIN],
        },
        loadChildren: () => import('./logs/logs.module').then(m => m.LogsModule),
      },
      {
        path: 'metrics',
        data: {
          authorities: [Authority.ADMIN],
        },
        loadChildren: () => import('./metrics/metrics.module').then(m => m.MetricsModule),
      },
      {
        path: 'tracker',
        data: {
          authorities: [Authority.ADMIN],
        },
        loadChildren: () => import('./tracker/tracker.module').then(m => m.TrackerModule),
      },
    ]),
  ],
})
export class AdminRoutingModule {}
