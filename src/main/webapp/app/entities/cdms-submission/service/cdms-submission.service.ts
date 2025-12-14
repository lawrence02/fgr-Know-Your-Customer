import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ICdmsSubmission, NewCdmsSubmission } from '../cdms-submission.model';

export type PartialUpdateCdmsSubmission = Partial<ICdmsSubmission> & Pick<ICdmsSubmission, 'id'>;

type RestOf<T extends ICdmsSubmission | NewCdmsSubmission> = Omit<T, 'submittedAt' | 'lastAttemptAt' | 'nextRetryAt'> & {
  submittedAt?: string | null;
  lastAttemptAt?: string | null;
  nextRetryAt?: string | null;
};

export type RestCdmsSubmission = RestOf<ICdmsSubmission>;

export type NewRestCdmsSubmission = RestOf<NewCdmsSubmission>;

export type PartialUpdateRestCdmsSubmission = RestOf<PartialUpdateCdmsSubmission>;

export type EntityResponseType = HttpResponse<ICdmsSubmission>;
export type EntityArrayResponseType = HttpResponse<ICdmsSubmission[]>;

@Injectable({ providedIn: 'root' })
export class CdmsSubmissionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cdms-submissions');

  create(cdmsSubmission: NewCdmsSubmission): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cdmsSubmission);
    return this.http
      .post<RestCdmsSubmission>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(cdmsSubmission: ICdmsSubmission): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cdmsSubmission);
    return this.http
      .put<RestCdmsSubmission>(`${this.resourceUrl}/${encodeURIComponent(this.getCdmsSubmissionIdentifier(cdmsSubmission))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(cdmsSubmission: PartialUpdateCdmsSubmission): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cdmsSubmission);
    return this.http
      .patch<RestCdmsSubmission>(`${this.resourceUrl}/${encodeURIComponent(this.getCdmsSubmissionIdentifier(cdmsSubmission))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCdmsSubmission>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCdmsSubmission[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getCdmsSubmissionIdentifier(cdmsSubmission: Pick<ICdmsSubmission, 'id'>): number {
    return cdmsSubmission.id;
  }

  compareCdmsSubmission(o1: Pick<ICdmsSubmission, 'id'> | null, o2: Pick<ICdmsSubmission, 'id'> | null): boolean {
    return o1 && o2 ? this.getCdmsSubmissionIdentifier(o1) === this.getCdmsSubmissionIdentifier(o2) : o1 === o2;
  }

  addCdmsSubmissionToCollectionIfMissing<Type extends Pick<ICdmsSubmission, 'id'>>(
    cdmsSubmissionCollection: Type[],
    ...cdmsSubmissionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cdmsSubmissions: Type[] = cdmsSubmissionsToCheck.filter(isPresent);
    if (cdmsSubmissions.length > 0) {
      const cdmsSubmissionCollectionIdentifiers = cdmsSubmissionCollection.map(cdmsSubmissionItem =>
        this.getCdmsSubmissionIdentifier(cdmsSubmissionItem),
      );
      const cdmsSubmissionsToAdd = cdmsSubmissions.filter(cdmsSubmissionItem => {
        const cdmsSubmissionIdentifier = this.getCdmsSubmissionIdentifier(cdmsSubmissionItem);
        if (cdmsSubmissionCollectionIdentifiers.includes(cdmsSubmissionIdentifier)) {
          return false;
        }
        cdmsSubmissionCollectionIdentifiers.push(cdmsSubmissionIdentifier);
        return true;
      });
      return [...cdmsSubmissionsToAdd, ...cdmsSubmissionCollection];
    }
    return cdmsSubmissionCollection;
  }

  protected convertDateFromClient<T extends ICdmsSubmission | NewCdmsSubmission | PartialUpdateCdmsSubmission>(
    cdmsSubmission: T,
  ): RestOf<T> {
    return {
      ...cdmsSubmission,
      submittedAt: cdmsSubmission.submittedAt?.toJSON() ?? null,
      lastAttemptAt: cdmsSubmission.lastAttemptAt?.toJSON() ?? null,
      nextRetryAt: cdmsSubmission.nextRetryAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCdmsSubmission: RestCdmsSubmission): ICdmsSubmission {
    return {
      ...restCdmsSubmission,
      submittedAt: restCdmsSubmission.submittedAt ? dayjs(restCdmsSubmission.submittedAt) : undefined,
      lastAttemptAt: restCdmsSubmission.lastAttemptAt ? dayjs(restCdmsSubmission.lastAttemptAt) : undefined,
      nextRetryAt: restCdmsSubmission.nextRetryAt ? dayjs(restCdmsSubmission.nextRetryAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCdmsSubmission>): HttpResponse<ICdmsSubmission> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCdmsSubmission[]>): HttpResponse<ICdmsSubmission[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
