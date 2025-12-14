import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IKycDocument } from '../kyc-document.model';
import { KycDocumentService } from '../service/kyc-document.service';

const kycDocumentResolve = (route: ActivatedRouteSnapshot): Observable<null | IKycDocument> => {
  const id = route.params.id;
  if (id) {
    return inject(KycDocumentService)
      .find(id)
      .pipe(
        mergeMap((kycDocument: HttpResponse<IKycDocument>) => {
          if (kycDocument.body) {
            return of(kycDocument.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default kycDocumentResolve;
