import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'fgrKnowYourCustomerApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'customer',
    data: { pageTitle: 'fgrKnowYourCustomerApp.customer.home.title' },
    loadChildren: () => import('./customer/customer.routes'),
  },
  {
    path: 'customer-identifier',
    data: { pageTitle: 'fgrKnowYourCustomerApp.customerIdentifier.home.title' },
    loadChildren: () => import('./customer-identifier/customer-identifier.routes'),
  },
  {
    path: 'kyc-case',
    data: { pageTitle: 'fgrKnowYourCustomerApp.kycCase.home.title' },
    loadChildren: () => import('./kyc-case/kyc-case.routes'),
  },
  {
    path: 'kyc-consent',
    data: { pageTitle: 'fgrKnowYourCustomerApp.kycConsent.home.title' },
    loadChildren: () => import('./kyc-consent/kyc-consent.routes'),
  },
  {
    path: 'kyc-document',
    data: { pageTitle: 'fgrKnowYourCustomerApp.kycDocument.home.title' },
    loadChildren: () => import('./kyc-document/kyc-document.routes'),
  },
  {
    path: 'cdms-submission',
    data: { pageTitle: 'fgrKnowYourCustomerApp.cdmsSubmission.home.title' },
    loadChildren: () => import('./cdms-submission/cdms-submission.routes'),
  },
  {
    path: 'kyc-notification',
    data: { pageTitle: 'fgrKnowYourCustomerApp.kycNotification.home.title' },
    loadChildren: () => import('./kyc-notification/kyc-notification.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
