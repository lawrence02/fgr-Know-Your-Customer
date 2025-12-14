import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IKycCase } from '../kyc-case.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../kyc-case.test-samples';

import { KycCaseService, RestKycCase } from './kyc-case.service';

const requireRestSample: RestKycCase = {
  ...sampleWithRequiredData,
  startedAt: sampleWithRequiredData.startedAt?.toJSON(),
  lastActivityAt: sampleWithRequiredData.lastActivityAt?.toJSON(),
  lastUpdatedAt: sampleWithRequiredData.lastUpdatedAt?.toJSON(),
  completedAt: sampleWithRequiredData.completedAt?.toJSON(),
  expiresAt: sampleWithRequiredData.expiresAt?.toJSON(),
};

describe('KycCase Service', () => {
  let service: KycCaseService;
  let httpMock: HttpTestingController;
  let expectedResult: IKycCase | IKycCase[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(KycCaseService);
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

    it('should create a KycCase', () => {
      const kycCase = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(kycCase).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a KycCase', () => {
      const kycCase = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(kycCase).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a KycCase', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of KycCase', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a KycCase', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addKycCaseToCollectionIfMissing', () => {
      it('should add a KycCase to an empty array', () => {
        const kycCase: IKycCase = sampleWithRequiredData;
        expectedResult = service.addKycCaseToCollectionIfMissing([], kycCase);
        expect(expectedResult).toEqual([kycCase]);
      });

      it('should not add a KycCase to an array that contains it', () => {
        const kycCase: IKycCase = sampleWithRequiredData;
        const kycCaseCollection: IKycCase[] = [
          {
            ...kycCase,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addKycCaseToCollectionIfMissing(kycCaseCollection, kycCase);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a KycCase to an array that doesn't contain it", () => {
        const kycCase: IKycCase = sampleWithRequiredData;
        const kycCaseCollection: IKycCase[] = [sampleWithPartialData];
        expectedResult = service.addKycCaseToCollectionIfMissing(kycCaseCollection, kycCase);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(kycCase);
      });

      it('should add only unique KycCase to an array', () => {
        const kycCaseArray: IKycCase[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const kycCaseCollection: IKycCase[] = [sampleWithRequiredData];
        expectedResult = service.addKycCaseToCollectionIfMissing(kycCaseCollection, ...kycCaseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const kycCase: IKycCase = sampleWithRequiredData;
        const kycCase2: IKycCase = sampleWithPartialData;
        expectedResult = service.addKycCaseToCollectionIfMissing([], kycCase, kycCase2);
        expect(expectedResult).toEqual([kycCase, kycCase2]);
      });

      it('should accept null and undefined values', () => {
        const kycCase: IKycCase = sampleWithRequiredData;
        expectedResult = service.addKycCaseToCollectionIfMissing([], null, kycCase, undefined);
        expect(expectedResult).toEqual([kycCase]);
      });

      it('should return initial array if no KycCase is added', () => {
        const kycCaseCollection: IKycCase[] = [sampleWithRequiredData];
        expectedResult = service.addKycCaseToCollectionIfMissing(kycCaseCollection, undefined, null);
        expect(expectedResult).toEqual(kycCaseCollection);
      });
    });

    describe('compareKycCase', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareKycCase(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 1342 };
        const entity2 = null;

        const compareResult1 = service.compareKycCase(entity1, entity2);
        const compareResult2 = service.compareKycCase(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 1342 };
        const entity2 = { id: 2504 };

        const compareResult1 = service.compareKycCase(entity1, entity2);
        const compareResult2 = service.compareKycCase(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 1342 };
        const entity2 = { id: 1342 };

        const compareResult1 = service.compareKycCase(entity1, entity2);
        const compareResult2 = service.compareKycCase(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
