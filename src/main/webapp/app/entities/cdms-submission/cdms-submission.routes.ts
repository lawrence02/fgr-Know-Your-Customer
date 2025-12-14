import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import CdmsSubmissionResolve from './route/cdms-submission-routing-resolve.service';

const cdmsSubmissionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cdms-submission').then(m => m.CdmsSubmission),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cdms-submission-detail').then(m => m.CdmsSubmissionDetail),
    resolve: {
      cdmsSubmission: CdmsSubmissionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cdms-submission-update').then(m => m.CdmsSubmissionUpdate),
    resolve: {
      cdmsSubmission: CdmsSubmissionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cdms-submission-update').then(m => m.CdmsSubmissionUpdate),
    resolve: {
      cdmsSubmission: CdmsSubmissionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default cdmsSubmissionRoute;
