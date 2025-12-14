import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IKycNotification, NewKycNotification } from '../kyc-notification.model';

export type PartialUpdateKycNotification = Partial<IKycNotification> & Pick<IKycNotification, 'id'>;

type RestOf<T extends IKycNotification | NewKycNotification> = Omit<T, 'sentAt' | 'deliveredAt'> & {
  sentAt?: string | null;
  deliveredAt?: string | null;
};

export type RestKycNotification = RestOf<IKycNotification>;

export type NewRestKycNotification = RestOf<NewKycNotification>;

export type PartialUpdateRestKycNotification = RestOf<PartialUpdateKycNotification>;

export type EntityResponseType = HttpResponse<IKycNotification>;
export type EntityArrayResponseType = HttpResponse<IKycNotification[]>;

@Injectable({ providedIn: 'root' })
export class KycNotificationService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/kyc-notifications');

  create(kycNotification: NewKycNotification): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(kycNotification);
    return this.http
      .post<RestKycNotification>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(kycNotification: IKycNotification): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(kycNotification);
    return this.http
      .put<RestKycNotification>(`${this.resourceUrl}/${encodeURIComponent(this.getKycNotificationIdentifier(kycNotification))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(kycNotification: PartialUpdateKycNotification): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(kycNotification);
    return this.http
      .patch<RestKycNotification>(`${this.resourceUrl}/${encodeURIComponent(this.getKycNotificationIdentifier(kycNotification))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestKycNotification>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestKycNotification[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getKycNotificationIdentifier(kycNotification: Pick<IKycNotification, 'id'>): number {
    return kycNotification.id;
  }

  compareKycNotification(o1: Pick<IKycNotification, 'id'> | null, o2: Pick<IKycNotification, 'id'> | null): boolean {
    return o1 && o2 ? this.getKycNotificationIdentifier(o1) === this.getKycNotificationIdentifier(o2) : o1 === o2;
  }

  addKycNotificationToCollectionIfMissing<Type extends Pick<IKycNotification, 'id'>>(
    kycNotificationCollection: Type[],
    ...kycNotificationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const kycNotifications: Type[] = kycNotificationsToCheck.filter(isPresent);
    if (kycNotifications.length > 0) {
      const kycNotificationCollectionIdentifiers = kycNotificationCollection.map(kycNotificationItem =>
        this.getKycNotificationIdentifier(kycNotificationItem),
      );
      const kycNotificationsToAdd = kycNotifications.filter(kycNotificationItem => {
        const kycNotificationIdentifier = this.getKycNotificationIdentifier(kycNotificationItem);
        if (kycNotificationCollectionIdentifiers.includes(kycNotificationIdentifier)) {
          return false;
        }
        kycNotificationCollectionIdentifiers.push(kycNotificationIdentifier);
        return true;
      });
      return [...kycNotificationsToAdd, ...kycNotificationCollection];
    }
    return kycNotificationCollection;
  }

  protected convertDateFromClient<T extends IKycNotification | NewKycNotification | PartialUpdateKycNotification>(
    kycNotification: T,
  ): RestOf<T> {
    return {
      ...kycNotification,
      sentAt: kycNotification.sentAt?.toJSON() ?? null,
      deliveredAt: kycNotification.deliveredAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restKycNotification: RestKycNotification): IKycNotification {
    return {
      ...restKycNotification,
      sentAt: restKycNotification.sentAt ? dayjs(restKycNotification.sentAt) : undefined,
      deliveredAt: restKycNotification.deliveredAt ? dayjs(restKycNotification.deliveredAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestKycNotification>): HttpResponse<IKycNotification> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestKycNotification[]>): HttpResponse<IKycNotification[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
