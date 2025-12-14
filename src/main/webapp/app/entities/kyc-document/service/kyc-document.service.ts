import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IKycDocument, NewKycDocument } from '../kyc-document.model';

export type PartialUpdateKycDocument = Partial<IKycDocument> & Pick<IKycDocument, 'id'>;

type RestOf<T extends IKycDocument | NewKycDocument> = Omit<T, 'uploadedAt' | 'expiresAt' | 'deletedAt'> & {
  uploadedAt?: string | null;
  expiresAt?: string | null;
  deletedAt?: string | null;
};

export type RestKycDocument = RestOf<IKycDocument>;

export type NewRestKycDocument = RestOf<NewKycDocument>;

export type PartialUpdateRestKycDocument = RestOf<PartialUpdateKycDocument>;

export type EntityResponseType = HttpResponse<IKycDocument>;
export type EntityArrayResponseType = HttpResponse<IKycDocument[]>;

@Injectable({ providedIn: 'root' })
export class KycDocumentService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/kyc-documents');

  create(kycDocument: NewKycDocument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(kycDocument);
    return this.http
      .post<RestKycDocument>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(kycDocument: IKycDocument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(kycDocument);
    return this.http
      .put<RestKycDocument>(`${this.resourceUrl}/${encodeURIComponent(this.getKycDocumentIdentifier(kycDocument))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(kycDocument: PartialUpdateKycDocument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(kycDocument);
    return this.http
      .patch<RestKycDocument>(`${this.resourceUrl}/${encodeURIComponent(this.getKycDocumentIdentifier(kycDocument))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestKycDocument>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestKycDocument[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getKycDocumentIdentifier(kycDocument: Pick<IKycDocument, 'id'>): number {
    return kycDocument.id;
  }

  compareKycDocument(o1: Pick<IKycDocument, 'id'> | null, o2: Pick<IKycDocument, 'id'> | null): boolean {
    return o1 && o2 ? this.getKycDocumentIdentifier(o1) === this.getKycDocumentIdentifier(o2) : o1 === o2;
  }

  addKycDocumentToCollectionIfMissing<Type extends Pick<IKycDocument, 'id'>>(
    kycDocumentCollection: Type[],
    ...kycDocumentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const kycDocuments: Type[] = kycDocumentsToCheck.filter(isPresent);
    if (kycDocuments.length > 0) {
      const kycDocumentCollectionIdentifiers = kycDocumentCollection.map(kycDocumentItem => this.getKycDocumentIdentifier(kycDocumentItem));
      const kycDocumentsToAdd = kycDocuments.filter(kycDocumentItem => {
        const kycDocumentIdentifier = this.getKycDocumentIdentifier(kycDocumentItem);
        if (kycDocumentCollectionIdentifiers.includes(kycDocumentIdentifier)) {
          return false;
        }
        kycDocumentCollectionIdentifiers.push(kycDocumentIdentifier);
        return true;
      });
      return [...kycDocumentsToAdd, ...kycDocumentCollection];
    }
    return kycDocumentCollection;
  }

  protected convertDateFromClient<T extends IKycDocument | NewKycDocument | PartialUpdateKycDocument>(kycDocument: T): RestOf<T> {
    return {
      ...kycDocument,
      uploadedAt: kycDocument.uploadedAt?.toJSON() ?? null,
      expiresAt: kycDocument.expiresAt?.toJSON() ?? null,
      deletedAt: kycDocument.deletedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restKycDocument: RestKycDocument): IKycDocument {
    return {
      ...restKycDocument,
      uploadedAt: restKycDocument.uploadedAt ? dayjs(restKycDocument.uploadedAt) : undefined,
      expiresAt: restKycDocument.expiresAt ? dayjs(restKycDocument.expiresAt) : undefined,
      deletedAt: restKycDocument.deletedAt ? dayjs(restKycDocument.deletedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestKycDocument>): HttpResponse<IKycDocument> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestKycDocument[]>): HttpResponse<IKycDocument[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
