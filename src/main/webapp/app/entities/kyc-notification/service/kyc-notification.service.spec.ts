import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IKycNotification } from '../kyc-notification.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../kyc-notification.test-samples';

import { KycNotificationService, RestKycNotification } from './kyc-notification.service';

const requireRestSample: RestKycNotification = {
  ...sampleWithRequiredData,
  sentAt: sampleWithRequiredData.sentAt?.toJSON(),
  deliveredAt: sampleWithRequiredData.deliveredAt?.toJSON(),
};

describe('KycNotification Service', () => {
  let service: KycNotificationService;
  let httpMock: HttpTestingController;
  let expectedResult: IKycNotification | IKycNotification[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(KycNotificationService);
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

    it('should create a KycNotification', () => {
      const kycNotification = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(kycNotification).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a KycNotification', () => {
      const kycNotification = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(kycNotification).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a KycNotification', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of KycNotification', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a KycNotification', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addKycNotificationToCollectionIfMissing', () => {
      it('should add a KycNotification to an empty array', () => {
        const kycNotification: IKycNotification = sampleWithRequiredData;
        expectedResult = service.addKycNotificationToCollectionIfMissing([], kycNotification);
        expect(expectedResult).toEqual([kycNotification]);
      });

      it('should not add a KycNotification to an array that contains it', () => {
        const kycNotification: IKycNotification = sampleWithRequiredData;
        const kycNotificationCollection: IKycNotification[] = [
          {
            ...kycNotification,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addKycNotificationToCollectionIfMissing(kycNotificationCollection, kycNotification);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a KycNotification to an array that doesn't contain it", () => {
        const kycNotification: IKycNotification = sampleWithRequiredData;
        const kycNotificationCollection: IKycNotification[] = [sampleWithPartialData];
        expectedResult = service.addKycNotificationToCollectionIfMissing(kycNotificationCollection, kycNotification);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(kycNotification);
      });

      it('should add only unique KycNotification to an array', () => {
        const kycNotificationArray: IKycNotification[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const kycNotificationCollection: IKycNotification[] = [sampleWithRequiredData];
        expectedResult = service.addKycNotificationToCollectionIfMissing(kycNotificationCollection, ...kycNotificationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const kycNotification: IKycNotification = sampleWithRequiredData;
        const kycNotification2: IKycNotification = sampleWithPartialData;
        expectedResult = service.addKycNotificationToCollectionIfMissing([], kycNotification, kycNotification2);
        expect(expectedResult).toEqual([kycNotification, kycNotification2]);
      });

      it('should accept null and undefined values', () => {
        const kycNotification: IKycNotification = sampleWithRequiredData;
        expectedResult = service.addKycNotificationToCollectionIfMissing([], null, kycNotification, undefined);
        expect(expectedResult).toEqual([kycNotification]);
      });

      it('should return initial array if no KycNotification is added', () => {
        const kycNotificationCollection: IKycNotification[] = [sampleWithRequiredData];
        expectedResult = service.addKycNotificationToCollectionIfMissing(kycNotificationCollection, undefined, null);
        expect(expectedResult).toEqual(kycNotificationCollection);
      });
    });

    describe('compareKycNotification', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareKycNotification(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 30507 };
        const entity2 = null;

        const compareResult1 = service.compareKycNotification(entity1, entity2);
        const compareResult2 = service.compareKycNotification(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 30507 };
        const entity2 = { id: 553 };

        const compareResult1 = service.compareKycNotification(entity1, entity2);
        const compareResult2 = service.compareKycNotification(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 30507 };
        const entity2 = { id: 30507 };

        const compareResult1 = service.compareKycNotification(entity1, entity2);
        const compareResult2 = service.compareKycNotification(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
