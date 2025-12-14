import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import KycCaseResolve from './route/kyc-case-routing-resolve.service';

const kycCaseRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/kyc-case').then(m => m.KycCase),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/kyc-case-detail').then(m => m.KycCaseDetail),
    resolve: {
      kycCase: KycCaseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/kyc-case-update').then(m => m.KycCaseUpdate),
    resolve: {
      kycCase: KycCaseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/kyc-case-update').then(m => m.KycCaseUpdate),
    resolve: {
      kycCase: KycCaseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default kycCaseRoute;
