import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ICustomerIdentifier, NewCustomerIdentifier } from '../customer-identifier.model';

export type PartialUpdateCustomerIdentifier = Partial<ICustomerIdentifier> & Pick<ICustomerIdentifier, 'id'>;

type RestOf<T extends ICustomerIdentifier | NewCustomerIdentifier> = Omit<T, 'createdAt' | 'verifiedAt'> & {
  createdAt?: string | null;
  verifiedAt?: string | null;
};

export type RestCustomerIdentifier = RestOf<ICustomerIdentifier>;

export type NewRestCustomerIdentifier = RestOf<NewCustomerIdentifier>;

export type PartialUpdateRestCustomerIdentifier = RestOf<PartialUpdateCustomerIdentifier>;

export type EntityResponseType = HttpResponse<ICustomerIdentifier>;
export type EntityArrayResponseType = HttpResponse<ICustomerIdentifier[]>;

@Injectable({ providedIn: 'root' })
export class CustomerIdentifierService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/customer-identifiers');

  create(customerIdentifier: NewCustomerIdentifier): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(customerIdentifier);
    return this.http
      .post<RestCustomerIdentifier>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(customerIdentifier: ICustomerIdentifier): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(customerIdentifier);
    return this.http
      .put<RestCustomerIdentifier>(
        `${this.resourceUrl}/${encodeURIComponent(this.getCustomerIdentifierIdentifier(customerIdentifier))}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(customerIdentifier: PartialUpdateCustomerIdentifier): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(customerIdentifier);
    return this.http
      .patch<RestCustomerIdentifier>(
        `${this.resourceUrl}/${encodeURIComponent(this.getCustomerIdentifierIdentifier(customerIdentifier))}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCustomerIdentifier>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCustomerIdentifier[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getCustomerIdentifierIdentifier(customerIdentifier: Pick<ICustomerIdentifier, 'id'>): number {
    return customerIdentifier.id;
  }

  compareCustomerIdentifier(o1: Pick<ICustomerIdentifier, 'id'> | null, o2: Pick<ICustomerIdentifier, 'id'> | null): boolean {
    return o1 && o2 ? this.getCustomerIdentifierIdentifier(o1) === this.getCustomerIdentifierIdentifier(o2) : o1 === o2;
  }

  addCustomerIdentifierToCollectionIfMissing<Type extends Pick<ICustomerIdentifier, 'id'>>(
    customerIdentifierCollection: Type[],
    ...customerIdentifiersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const customerIdentifiers: Type[] = customerIdentifiersToCheck.filter(isPresent);
    if (customerIdentifiers.length > 0) {
      const customerIdentifierCollectionIdentifiers = customerIdentifierCollection.map(customerIdentifierItem =>
        this.getCustomerIdentifierIdentifier(customerIdentifierItem),
      );
      const customerIdentifiersToAdd = customerIdentifiers.filter(customerIdentifierItem => {
        const customerIdentifierIdentifier = this.getCustomerIdentifierIdentifier(customerIdentifierItem);
        if (customerIdentifierCollectionIdentifiers.includes(customerIdentifierIdentifier)) {
          return false;
        }
        customerIdentifierCollectionIdentifiers.push(customerIdentifierIdentifier);
        return true;
      });
      return [...customerIdentifiersToAdd, ...customerIdentifierCollection];
    }
    return customerIdentifierCollection;
  }

  protected convertDateFromClient<T extends ICustomerIdentifier | NewCustomerIdentifier | PartialUpdateCustomerIdentifier>(
    customerIdentifier: T,
  ): RestOf<T> {
    return {
      ...customerIdentifier,
      createdAt: customerIdentifier.createdAt?.toJSON() ?? null,
      verifiedAt: customerIdentifier.verifiedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCustomerIdentifier: RestCustomerIdentifier): ICustomerIdentifier {
    return {
      ...restCustomerIdentifier,
      createdAt: restCustomerIdentifier.createdAt ? dayjs(restCustomerIdentifier.createdAt) : undefined,
      verifiedAt: restCustomerIdentifier.verifiedAt ? dayjs(restCustomerIdentifier.verifiedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCustomerIdentifier>): HttpResponse<ICustomerIdentifier> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCustomerIdentifier[]>): HttpResponse<ICustomerIdentifier[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
