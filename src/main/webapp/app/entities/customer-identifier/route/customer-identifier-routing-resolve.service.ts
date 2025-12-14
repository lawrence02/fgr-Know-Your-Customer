import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICustomerIdentifier } from '../customer-identifier.model';
import { CustomerIdentifierService } from '../service/customer-identifier.service';

const customerIdentifierResolve = (route: ActivatedRouteSnapshot): Observable<null | ICustomerIdentifier> => {
  const id = route.params.id;
  if (id) {
    return inject(CustomerIdentifierService)
      .find(id)
      .pipe(
        mergeMap((customerIdentifier: HttpResponse<ICustomerIdentifier>) => {
          if (customerIdentifier.body) {
            return of(customerIdentifier.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default customerIdentifierResolve;
