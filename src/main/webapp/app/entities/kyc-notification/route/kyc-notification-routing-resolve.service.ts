import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IKycNotification } from '../kyc-notification.model';
import { KycNotificationService } from '../service/kyc-notification.service';

const kycNotificationResolve = (route: ActivatedRouteSnapshot): Observable<null | IKycNotification> => {
  const id = route.params.id;
  if (id) {
    return inject(KycNotificationService)
      .find(id)
      .pipe(
        mergeMap((kycNotification: HttpResponse<IKycNotification>) => {
          if (kycNotification.body) {
            return of(kycNotification.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default kycNotificationResolve;
