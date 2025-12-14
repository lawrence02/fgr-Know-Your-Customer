import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { NotificationType } from 'app/entities/enumerations/notification-type.model';
import { IKycCase } from 'app/entities/kyc-case/kyc-case.model';
import { KycCaseService } from 'app/entities/kyc-case/service/kyc-case.service';
import SharedModule from 'app/shared/shared.module';
import { IKycNotification } from '../kyc-notification.model';
import { KycNotificationService } from '../service/kyc-notification.service';

import { KycNotificationFormGroup, KycNotificationFormService } from './kyc-notification-form.service';

@Component({
  selector: 'jhi-kyc-notification-update',
  templateUrl: './kyc-notification-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class KycNotificationUpdate implements OnInit {
  isSaving = false;
  kycNotification: IKycNotification | null = null;
  notificationTypeValues = Object.keys(NotificationType);

  kycCasesSharedCollection = signal<IKycCase[]>([]);

  protected kycNotificationService = inject(KycNotificationService);
  protected kycNotificationFormService = inject(KycNotificationFormService);
  protected kycCaseService = inject(KycCaseService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: KycNotificationFormGroup = this.kycNotificationFormService.createKycNotificationFormGroup();

  compareKycCase = (o1: IKycCase | null, o2: IKycCase | null): boolean => this.kycCaseService.compareKycCase(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ kycNotification }) => {
      this.kycNotification = kycNotification;
      if (kycNotification) {
        this.updateForm(kycNotification);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const kycNotification = this.kycNotificationFormService.getKycNotification(this.editForm);
    if (kycNotification.id === null) {
      this.subscribeToSaveResponse(this.kycNotificationService.create(kycNotification));
    } else {
      this.subscribeToSaveResponse(this.kycNotificationService.update(kycNotification));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKycNotification>>): void {
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

  protected updateForm(kycNotification: IKycNotification): void {
    this.kycNotification = kycNotification;
    this.kycNotificationFormService.resetForm(this.editForm, kycNotification);

    this.kycCasesSharedCollection.set(
      this.kycCaseService.addKycCaseToCollectionIfMissing<IKycCase>(this.kycCasesSharedCollection(), kycNotification.kycCase),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.kycCaseService
      .query()
      .pipe(map((res: HttpResponse<IKycCase[]>) => res.body ?? []))
      .pipe(
        map((kycCases: IKycCase[]) =>
          this.kycCaseService.addKycCaseToCollectionIfMissing<IKycCase>(kycCases, this.kycNotification?.kycCase),
        ),
      )
      .subscribe((kycCases: IKycCase[]) => this.kycCasesSharedCollection.set(kycCases));
  }
}
