import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IKycCase, NewKycCase } from '../kyc-case.model';

export type PartialUpdateKycCase = Partial<IKycCase> & Pick<IKycCase, 'id'>;

type RestOf<T extends IKycCase | NewKycCase> = Omit<T, 'startedAt' | 'lastActivityAt' | 'lastUpdatedAt' | 'completedAt' | 'expiresAt'> & {
  startedAt?: string | null;
  lastActivityAt?: string | null;
  lastUpdatedAt?: string | null;
  completedAt?: string | null;
  expiresAt?: string | null;
};

export type RestKycCase = RestOf<IKycCase>;

export type NewRestKycCase = RestOf<NewKycCase>;

export type PartialUpdateRestKycCase = RestOf<PartialUpdateKycCase>;

export type EntityResponseType = HttpResponse<IKycCase>;
export type EntityArrayResponseType = HttpResponse<IKycCase[]>;

@Injectable({ providedIn: 'root' })
export class KycCaseService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/kyc-cases');

  create(kycCase: NewKycCase): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(kycCase);
    return this.http
      .post<RestKycCase>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(kycCase: IKycCase): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(kycCase);
    return this.http
      .put<RestKycCase>(`${this.resourceUrl}/${encodeURIComponent(this.getKycCaseIdentifier(kycCase))}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(kycCase: PartialUpdateKycCase): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(kycCase);
    return this.http
      .patch<RestKycCase>(`${this.resourceUrl}/${encodeURIComponent(this.getKycCaseIdentifier(kycCase))}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestKycCase>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestKycCase[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getKycCaseIdentifier(kycCase: Pick<IKycCase, 'id'>): number {
    return kycCase.id;
  }

  compareKycCase(o1: Pick<IKycCase, 'id'> | null, o2: Pick<IKycCase, 'id'> | null): boolean {
    return o1 && o2 ? this.getKycCaseIdentifier(o1) === this.getKycCaseIdentifier(o2) : o1 === o2;
  }

  addKycCaseToCollectionIfMissing<Type extends Pick<IKycCase, 'id'>>(
    kycCaseCollection: Type[],
    ...kycCasesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const kycCases: Type[] = kycCasesToCheck.filter(isPresent);
    if (kycCases.length > 0) {
      const kycCaseCollectionIdentifiers = kycCaseCollection.map(kycCaseItem => this.getKycCaseIdentifier(kycCaseItem));
      const kycCasesToAdd = kycCases.filter(kycCaseItem => {
        const kycCaseIdentifier = this.getKycCaseIdentifier(kycCaseItem);
        if (kycCaseCollectionIdentifiers.includes(kycCaseIdentifier)) {
          return false;
        }
        kycCaseCollectionIdentifiers.push(kycCaseIdentifier);
        return true;
      });
      return [...kycCasesToAdd, ...kycCaseCollection];
    }
    return kycCaseCollection;
  }

  protected convertDateFromClient<T extends IKycCase | NewKycCase | PartialUpdateKycCase>(kycCase: T): RestOf<T> {
    return {
      ...kycCase,
      startedAt: kycCase.startedAt?.toJSON() ?? null,
      lastActivityAt: kycCase.lastActivityAt?.toJSON() ?? null,
      lastUpdatedAt: kycCase.lastUpdatedAt?.toJSON() ?? null,
      completedAt: kycCase.completedAt?.toJSON() ?? null,
      expiresAt: kycCase.expiresAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restKycCase: RestKycCase): IKycCase {
    return {
      ...restKycCase,
      startedAt: restKycCase.startedAt ? dayjs(restKycCase.startedAt) : undefined,
      lastActivityAt: restKycCase.lastActivityAt ? dayjs(restKycCase.lastActivityAt) : undefined,
      lastUpdatedAt: restKycCase.lastUpdatedAt ? dayjs(restKycCase.lastUpdatedAt) : undefined,
      completedAt: restKycCase.completedAt ? dayjs(restKycCase.completedAt) : undefined,
      expiresAt: restKycCase.expiresAt ? dayjs(restKycCase.expiresAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestKycCase>): HttpResponse<IKycCase> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestKycCase[]>): HttpResponse<IKycCase[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
