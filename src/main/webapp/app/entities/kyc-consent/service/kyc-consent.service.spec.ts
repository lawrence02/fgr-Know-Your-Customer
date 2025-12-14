import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IKycConsent } from '../kyc-consent.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../kyc-consent.test-samples';

import { KycConsentService, RestKycConsent } from './kyc-consent.service';

const requireRestSample: RestKycConsent = {
  ...sampleWithRequiredData,
  consentedAt: sampleWithRequiredData.consentedAt?.toJSON(),
};

describe('KycConsent Service', () => {
  let service: KycConsentService;
  let httpMock: HttpTestingController;
  let expectedResult: IKycConsent | IKycConsent[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(KycConsentService);
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

    it('should create a KycConsent', () => {
      const kycConsent = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(kycConsent).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a KycConsent', () => {
      const kycConsent = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(kycConsent).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a KycConsent', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of KycConsent', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a KycConsent', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addKycConsentToCollectionIfMissing', () => {
      it('should add a KycConsent to an empty array', () => {
        const kycConsent: IKycConsent = sampleWithRequiredData;
        expectedResult = service.addKycConsentToCollectionIfMissing([], kycConsent);
        expect(expectedResult).toEqual([kycConsent]);
      });

      it('should not add a KycConsent to an array that contains it', () => {
        const kycConsent: IKycConsent = sampleWithRequiredData;
        const kycConsentCollection: IKycConsent[] = [
          {
            ...kycConsent,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addKycConsentToCollectionIfMissing(kycConsentCollection, kycConsent);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a KycConsent to an array that doesn't contain it", () => {
        const kycConsent: IKycConsent = sampleWithRequiredData;
        const kycConsentCollection: IKycConsent[] = [sampleWithPartialData];
        expectedResult = service.addKycConsentToCollectionIfMissing(kycConsentCollection, kycConsent);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(kycConsent);
      });

      it('should add only unique KycConsent to an array', () => {
        const kycConsentArray: IKycConsent[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const kycConsentCollection: IKycConsent[] = [sampleWithRequiredData];
        expectedResult = service.addKycConsentToCollectionIfMissing(kycConsentCollection, ...kycConsentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const kycConsent: IKycConsent = sampleWithRequiredData;
        const kycConsent2: IKycConsent = sampleWithPartialData;
        expectedResult = service.addKycConsentToCollectionIfMissing([], kycConsent, kycConsent2);
        expect(expectedResult).toEqual([kycConsent, kycConsent2]);
      });

      it('should accept null and undefined values', () => {
        const kycConsent: IKycConsent = sampleWithRequiredData;
        expectedResult = service.addKycConsentToCollectionIfMissing([], null, kycConsent, undefined);
        expect(expectedResult).toEqual([kycConsent]);
      });

      it('should return initial array if no KycConsent is added', () => {
        const kycConsentCollection: IKycConsent[] = [sampleWithRequiredData];
        expectedResult = service.addKycConsentToCollectionIfMissing(kycConsentCollection, undefined, null);
        expect(expectedResult).toEqual(kycConsentCollection);
      });
    });

    describe('compareKycConsent', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareKycConsent(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 12952 };
        const entity2 = null;

        const compareResult1 = service.compareKycConsent(entity1, entity2);
        const compareResult2 = service.compareKycConsent(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 12952 };
        const entity2 = { id: 2003 };

        const compareResult1 = service.compareKycConsent(entity1, entity2);
        const compareResult2 = service.compareKycConsent(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 12952 };
        const entity2 = { id: 12952 };

        const compareResult1 = service.compareKycConsent(entity1, entity2);
        const compareResult2 = service.compareKycConsent(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
