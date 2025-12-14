import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DocumentType } from 'app/entities/enumerations/document-type.model';
import { IKycCase } from 'app/entities/kyc-case/kyc-case.model';
import { KycCaseService } from 'app/entities/kyc-case/service/kyc-case.service';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IKycDocument } from '../kyc-document.model';
import { KycDocumentService } from '../service/kyc-document.service';

import { KycDocumentFormGroup, KycDocumentFormService } from './kyc-document-form.service';

@Component({
  selector: 'jhi-kyc-document-update',
  templateUrl: './kyc-document-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class KycDocumentUpdate implements OnInit {
  isSaving = false;
  kycDocument: IKycDocument | null = null;
  documentTypeValues = Object.keys(DocumentType);

  kycCasesSharedCollection = signal<IKycCase[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected kycDocumentService = inject(KycDocumentService);
  protected kycDocumentFormService = inject(KycDocumentFormService);
  protected kycCaseService = inject(KycCaseService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: KycDocumentFormGroup = this.kycDocumentFormService.createKycDocumentFormGroup();

  compareKycCase = (o1: IKycCase | null, o2: IKycCase | null): boolean => this.kycCaseService.compareKycCase(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ kycDocument }) => {
      this.kycDocument = kycDocument;
      if (kycDocument) {
        this.updateForm(kycDocument);
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
    const kycDocument = this.kycDocumentFormService.getKycDocument(this.editForm);
    if (kycDocument.id === null) {
      this.subscribeToSaveResponse(this.kycDocumentService.create(kycDocument));
    } else {
      this.subscribeToSaveResponse(this.kycDocumentService.update(kycDocument));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKycDocument>>): void {
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

  protected updateForm(kycDocument: IKycDocument): void {
    this.kycDocument = kycDocument;
    this.kycDocumentFormService.resetForm(this.editForm, kycDocument);

    this.kycCasesSharedCollection.set(
      this.kycCaseService.addKycCaseToCollectionIfMissing<IKycCase>(this.kycCasesSharedCollection(), kycDocument.kycCase),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.kycCaseService
      .query()
      .pipe(map((res: HttpResponse<IKycCase[]>) => res.body ?? []))
      .pipe(
        map((kycCases: IKycCase[]) => this.kycCaseService.addKycCaseToCollectionIfMissing<IKycCase>(kycCases, this.kycDocument?.kycCase)),
      )
      .subscribe((kycCases: IKycCase[]) => this.kycCasesSharedCollection.set(kycCases));
  }
}
