import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICdmsSubmission } from '../cdms-submission.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../cdms-submission.test-samples';

import { CdmsSubmissionService, RestCdmsSubmission } from './cdms-submission.service';

const requireRestSample: RestCdmsSubmission = {
  ...sampleWithRequiredData,
  submittedAt: sampleWithRequiredData.submittedAt?.toJSON(),
  lastAttemptAt: sampleWithRequiredData.lastAttemptAt?.toJSON(),
  nextRetryAt: sampleWithRequiredData.nextRetryAt?.toJSON(),
};

describe('CdmsSubmission Service', () => {
  let service: CdmsSubmissionService;
  let httpMock: HttpTestingController;
  let expectedResult: ICdmsSubmission | ICdmsSubmission[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CdmsSubmissionService);
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

    it('should create a CdmsSubmission', () => {
      const cdmsSubmission = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cdmsSubmission).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CdmsSubmission', () => {
      const cdmsSubmission = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cdmsSubmission).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CdmsSubmission', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CdmsSubmission', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CdmsSubmission', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCdmsSubmissionToCollectionIfMissing', () => {
      it('should add a CdmsSubmission to an empty array', () => {
        const cdmsSubmission: ICdmsSubmission = sampleWithRequiredData;
        expectedResult = service.addCdmsSubmissionToCollectionIfMissing([], cdmsSubmission);
        expect(expectedResult).toEqual([cdmsSubmission]);
      });

      it('should not add a CdmsSubmission to an array that contains it', () => {
        const cdmsSubmission: ICdmsSubmission = sampleWithRequiredData;
        const cdmsSubmissionCollection: ICdmsSubmission[] = [
          {
            ...cdmsSubmission,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCdmsSubmissionToCollectionIfMissing(cdmsSubmissionCollection, cdmsSubmission);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CdmsSubmission to an array that doesn't contain it", () => {
        const cdmsSubmission: ICdmsSubmission = sampleWithRequiredData;
        const cdmsSubmissionCollection: ICdmsSubmission[] = [sampleWithPartialData];
        expectedResult = service.addCdmsSubmissionToCollectionIfMissing(cdmsSubmissionCollection, cdmsSubmission);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cdmsSubmission);
      });

      it('should add only unique CdmsSubmission to an array', () => {
        const cdmsSubmissionArray: ICdmsSubmission[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cdmsSubmissionCollection: ICdmsSubmission[] = [sampleWithRequiredData];
        expectedResult = service.addCdmsSubmissionToCollectionIfMissing(cdmsSubmissionCollection, ...cdmsSubmissionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cdmsSubmission: ICdmsSubmission = sampleWithRequiredData;
        const cdmsSubmission2: ICdmsSubmission = sampleWithPartialData;
        expectedResult = service.addCdmsSubmissionToCollectionIfMissing([], cdmsSubmission, cdmsSubmission2);
        expect(expectedResult).toEqual([cdmsSubmission, cdmsSubmission2]);
      });

      it('should accept null and undefined values', () => {
        const cdmsSubmission: ICdmsSubmission = sampleWithRequiredData;
        expectedResult = service.addCdmsSubmissionToCollectionIfMissing([], null, cdmsSubmission, undefined);
        expect(expectedResult).toEqual([cdmsSubmission]);
      });

      it('should return initial array if no CdmsSubmission is added', () => {
        const cdmsSubmissionCollection: ICdmsSubmission[] = [sampleWithRequiredData];
        expectedResult = service.addCdmsSubmissionToCollectionIfMissing(cdmsSubmissionCollection, undefined, null);
        expect(expectedResult).toEqual(cdmsSubmissionCollection);
      });
    });

    describe('compareCdmsSubmission', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCdmsSubmission(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7713 };
        const entity2 = null;

        const compareResult1 = service.compareCdmsSubmission(entity1, entity2);
        const compareResult2 = service.compareCdmsSubmission(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7713 };
        const entity2 = { id: 21337 };

        const compareResult1 = service.compareCdmsSubmission(entity1, entity2);
        const compareResult2 = service.compareCdmsSubmission(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 7713 };
        const entity2 = { id: 7713 };

        const compareResult1 = service.compareCdmsSubmission(entity1, entity2);
        const compareResult2 = service.compareCdmsSubmission(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
