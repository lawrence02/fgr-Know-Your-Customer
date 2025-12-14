import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IKycDocument } from '../kyc-document.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../kyc-document.test-samples';

import { KycDocumentService, RestKycDocument } from './kyc-document.service';

const requireRestSample: RestKycDocument = {
  ...sampleWithRequiredData,
  uploadedAt: sampleWithRequiredData.uploadedAt?.toJSON(),
  expiresAt: sampleWithRequiredData.expiresAt?.toJSON(),
  deletedAt: sampleWithRequiredData.deletedAt?.toJSON(),
};

describe('KycDocument Service', () => {
  let service: KycDocumentService;
  let httpMock: HttpTestingController;
  let expectedResult: IKycDocument | IKycDocument[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(KycDocumentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a KycDocument', () => {
      const kycDocument = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(kycDocument).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a KycDocument', () => {
      const kycDocument = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(kycDocument).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a KycDocument', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of KycDocument', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a KycDocument', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addKycDocumentToCollectionIfMissing', () => {
      it('should add a KycDocument to an empty array', () => {
        const kycDocument: IKycDocument = sampleWithRequiredData;
        expectedResult = service.addKycDocumentToCollectionIfMissing([], kycDocument);
        expect(expectedResult).toEqual([kycDocument]);
      });

      it('should not add a KycDocument to an array that contains it', () => {
        const kycDocument: IKycDocument = sampleWithRequiredData;
        const kycDocumentCollection: IKycDocument[] = [
          {
            ...kycDocument,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addKycDocumentToCollectionIfMissing(kycDocumentCollection, kycDocument);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a KycDocument to an array that doesn't contain it", () => {
        const kycDocument: IKycDocument = sampleWithRequiredData;
        const kycDocumentCollection: IKycDocument[] = [sampleWithPartialData];
        expectedResult = service.addKycDocumentToCollectionIfMissing(kycDocumentCollection, kycDocument);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(kycDocument);
      });

      it('should add only unique KycDocument to an array', () => {
        const kycDocumentArray: IKycDocument[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const kycDocumentCollection: IKycDocument[] = [sampleWithRequiredData];
        expectedResult = service.addKycDocumentToCollectionIfMissing(kycDocumentCollection, ...kycDocumentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const kycDocument: IKycDocument = sampleWithRequiredData;
        const kycDocument2: IKycDocument = sampleWithPartialData;
        expectedResult = service.addKycDocumentToCollectionIfMissing([], kycDocument, kycDocument2);
        expect(expectedResult).toEqual([kycDocument, kycDocument2]);
      });

      it('should accept null and undefined values', () => {
        const kycDocument: IKycDocument = sampleWithRequiredData;
        expectedResult = service.addKycDocumentToCollectionIfMissing([], null, kycDocument, undefined);
        expect(expectedResult).toEqual([kycDocument]);
      });

      it('should return initial array if no KycDocument is added', () => {
        const kycDocumentCollection: IKycDocument[] = [sampleWithRequiredData];
        expectedResult = service.addKycDocumentToCollectionIfMissing(kycDocumentCollection, undefined, null);
        expect(expectedResult).toEqual(kycDocumentCollection);
      });
    });

    describe('compareKycDocument', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareKycDocument(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 12821 };
        const entity2 = null;

        const compareResult1 = service.compareKycDocument(entity1, entity2);
        const compareResult2 = service.compareKycDocument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 12821 };
        const entity2 = { id: 13608 };

        const compareResult1 = service.compareKycDocument(entity1, entity2);
        const compareResult2 = service.compareKycDocument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 12821 };
        const entity2 = { id: 12821 };

        const compareResult1 = service.compareKycDocument(entity1, entity2);
        const compareResult2 = service.compareKycDocument(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
