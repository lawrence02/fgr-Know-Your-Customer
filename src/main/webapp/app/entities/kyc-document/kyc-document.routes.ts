import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import KycDocumentResolve from './route/kyc-document-routing-resolve.service';

const kycDocumentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/kyc-document').then(m => m.KycDocument),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/kyc-document-detail').then(m => m.KycDocumentDetail),
    resolve: {
      kycDocument: KycDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/kyc-document-update').then(m => m.KycDocumentUpdate),
    resolve: {
      kycDocument: KycDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/kyc-document-update').then(m => m.KycDocumentUpdate),
    resolve: {
      kycDocument: KycDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default kycDocumentRoute;
