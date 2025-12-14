import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { ICdmsSubmission } from 'app/entities/cdms-submission/cdms-submission.model';
import { CdmsSubmissionService } from 'app/entities/cdms-submission/service/cdms-submission.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { ChannelType } from 'app/entities/enumerations/channel-type.model';
import { KycStatus } from 'app/entities/enumerations/kyc-status.model';
import { IKycConsent } from 'app/entities/kyc-consent/kyc-consent.model';
import { KycConsentService } from 'app/entities/kyc-consent/service/kyc-consent.service';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IKycCase } from '../kyc-case.model';
import { KycCaseService } from '../service/kyc-case.service';

import { KycCaseFormGroup, KycCaseFormService } from './kyc-case-form.service';

@Component({
  selector: 'jhi-kyc-case-update',
  templateUrl: './kyc-case-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class KycCaseUpdate implements OnInit {
  isSaving = false;
  kycCase: IKycCase | null = null;
  kycStatusValues = Object.keys(KycStatus);
  channelTypeValues = Object.keys(ChannelType);

  consentsCollection = signal<IKycConsent[]>([]);
  submissionsCollection = signal<ICdmsSubmission[]>([]);
  customersSharedCollection = signal<ICustomer[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected kycCaseService = inject(KycCaseService);
  protected kycCaseFormService = inject(KycCaseFormService);
  protected kycConsentService = inject(KycConsentService);
  protected cdmsSubmissionService = inject(CdmsSubmissionService);
  protected customerService = inject(CustomerService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: KycCaseFormGroup = this.kycCaseFormService.createKycCaseFormGroup();

  compareKycConsent = (o1: IKycConsent | null, o2: IKycConsent | null): boolean => this.kycConsentService.compareKycConsent(o1, o2);

  compareCdmsSubmission = (o1: ICdmsSubmission | null, o2: ICdmsSubmission | null): boolean =>
    this.cdmsSubmissionService.compareCdmsSubmission(o1, o2);

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ kycCase }) => {
      this.kycCase = kycCase;
      if (kycCase) {
        this.updateForm(kycCase);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertErrorModel>('fgrKnowYourCustomerApp.error', { ...err, key: `error.file.${err.key}` }),
        ),
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const kycCase = this.kycCaseFormService.getKycCase(this.editForm);
    if (kycCase.id === null) {
      this.subscribeToSaveResponse(this.kycCaseService.create(kycCase));
    } else {
      this.subscribeToSaveResponse(this.kycCaseService.update(kycCase));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKycCase>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(kycCase: IKycCase): void {
    this.kycCase = kycCase;
    this.kycCaseFormService.resetForm(this.editForm, kycCase);

    this.consentsCollection.set(
      this.kycConsentService.addKycConsentToCollectionIfMissing<IKycConsent>(this.consentsCollection(), kycCase.consent),
    );
    this.submissionsCollection.set(
      this.cdmsSubmissionService.addCdmsSubmissionToCollectionIfMissing<ICdmsSubmission>(this.submissionsCollection(), kycCase.submission),
    );
    this.customersSharedCollection.set(
      this.customerService.addCustomerToCollectionIfMissing<ICustomer>(this.customersSharedCollection(), kycCase.customer),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.kycConsentService
      .query({ filter: 'kyccase-is-null' })
      .pipe(map((res: HttpResponse<IKycConsent[]>) => res.body ?? []))
      .pipe(
        map((kycConsents: IKycConsent[]) =>
          this.kycConsentService.addKycConsentToCollectionIfMissing<IKycConsent>(kycConsents, this.kycCase?.consent),
        ),
      )
      .subscribe((kycConsents: IKycConsent[]) => this.consentsCollection.set(kycConsents));

    this.cdmsSubmissionService
      .query({ 'kycCaseId.specified': 'false' })
      .pipe(map((res: HttpResponse<ICdmsSubmission[]>) => res.body ?? []))
      .pipe(
        map((cdmsSubmissions: ICdmsSubmission[]) =>
          this.cdmsSubmissionService.addCdmsSubmissionToCollectionIfMissing<ICdmsSubmission>(cdmsSubmissions, this.kycCase?.submission),
        ),
      )
      .subscribe((cdmsSubmissions: ICdmsSubmission[]) => this.submissionsCollection.set(cdmsSubmissions));

    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.kycCase?.customer),
        ),
      )
      .subscribe((customers: ICustomer[]) => this.customersSharedCollection.set(customers));
  }
}
