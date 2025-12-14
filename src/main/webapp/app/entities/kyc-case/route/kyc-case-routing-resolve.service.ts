import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IKycCase } from '../kyc-case.model';
import { KycCaseService } from '../service/kyc-case.service';

const kycCaseResolve = (route: ActivatedRouteSnapshot): Observable<null | IKycCase> => {
  const id = route.params.id;
  if (id) {
    return inject(KycCaseService)
      .find(id)
      .pipe(
        mergeMap((kycCase: HttpResponse<IKycCase>) => {
          if (kycCase.body) {
            return of(kycCase.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default kycCaseResolve;
