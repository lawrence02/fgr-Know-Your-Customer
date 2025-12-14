import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ChannelType } from 'app/entities/enumerations/channel-type.model';
import SharedModule from 'app/shared/shared.module';
import { IKycConsent } from '../kyc-consent.model';
import { KycConsentService } from '../service/kyc-consent.service';

import { KycConsentFormGroup, KycConsentFormService } from './kyc-consent-form.service';

@Component({
  selector: 'jhi-kyc-consent-update',
  templateUrl: './kyc-consent-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class KycConsentUpdate implements OnInit {
  isSaving = false;
  kycConsent: IKycConsent | null = null;
  channelTypeValues = Object.keys(ChannelType);

  protected kycConsentService = inject(KycConsentService);
  protected kycConsentFormService = inject(KycConsentFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: KycConsentFormGroup = this.kycConsentFormService.createKycConsentFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ kycConsent }) => {
      this.kycConsent = kycConsent;
      if (kycConsent) {
        this.updateForm(kycConsent);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const kycConsent = this.kycConsentFormService.getKycConsent(this.editForm);
    if (kycConsent.id === null) {
      this.subscribeToSaveResponse(this.kycConsentService.create(kycConsent));
    } else {
      this.subscribeToSaveResponse(this.kycConsentService.update(kycConsent));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKycConsent>>): void {
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

  protected updateForm(kycConsent: IKycConsent): void {
    this.kycConsent = kycConsent;
    this.kycConsentFormService.resetForm(this.editForm, kycConsent);
  }
}
