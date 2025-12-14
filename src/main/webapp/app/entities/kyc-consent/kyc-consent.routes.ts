import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import KycConsentResolve from './route/kyc-consent-routing-resolve.service';

const kycConsentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/kyc-consent').then(m => m.KycConsent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/kyc-consent-detail').then(m => m.KycConsentDetail),
    resolve: {
      kycConsent: KycConsentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/kyc-consent-update').then(m => m.KycConsentUpdate),
    resolve: {
      kycConsent: KycConsentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/kyc-consent-update').then(m => m.KycConsentUpdate),
    resolve: {
      kycConsent: KycConsentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default kycConsentRoute;
