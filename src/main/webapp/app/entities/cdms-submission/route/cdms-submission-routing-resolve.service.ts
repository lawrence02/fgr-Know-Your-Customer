import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICdmsSubmission } from '../cdms-submission.model';
import { CdmsSubmissionService } from '../service/cdms-submission.service';

const cdmsSubmissionResolve = (route: ActivatedRouteSnapshot): Observable<null | ICdmsSubmission> => {
  const id = route.params.id;
  if (id) {
    return inject(CdmsSubmissionService)
      .find(id)
      .pipe(
        mergeMap((cdmsSubmission: HttpResponse<ICdmsSubmission>) => {
          if (cdmsSubmission.body) {
            return of(cdmsSubmission.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default cdmsSubmissionResolve;
