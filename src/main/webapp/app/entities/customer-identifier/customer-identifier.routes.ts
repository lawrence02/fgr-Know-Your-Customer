import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import CustomerIdentifierResolve from './route/customer-identifier-routing-resolve.service';

const customerIdentifierRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/customer-identifier').then(m => m.CustomerIdentifier),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/customer-identifier-detail').then(m => m.CustomerIdentifierDetail),
    resolve: {
      customerIdentifier: CustomerIdentifierResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/customer-identifier-update').then(m => m.CustomerIdentifierUpdate),
    resolve: {
      customerIdentifier: CustomerIdentifierResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/customer-identifier-update').then(m => m.CustomerIdentifierUpdate),
    resolve: {
      customerIdentifier: CustomerIdentifierResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default customerIdentifierRoute;
