import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import KycNotificationResolve from './route/kyc-notification-routing-resolve.service';

const kycNotificationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/kyc-notification').then(m => m.KycNotification),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/kyc-notification-detail').then(m => m.KycNotificationDetail),
    resolve: {
      kycNotification: KycNotificationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/kyc-notification-update').then(m => m.KycNotificationUpdate),
    resolve: {
      kycNotification: KycNotificationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/kyc-notification-update').then(m => m.KycNotificationUpdate),
    resolve: {
      kycNotification: KycNotificationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default kycNotificationRoute;
