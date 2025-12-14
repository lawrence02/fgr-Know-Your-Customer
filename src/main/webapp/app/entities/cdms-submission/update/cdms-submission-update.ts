import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { SubmissionStatus } from 'app/entities/enumerations/submission-status.model';
import SharedModule from 'app/shared/shared.module';
import { ICdmsSubmission } from '../cdms-submission.model';
import { CdmsSubmissionService } from '../service/cdms-submission.service';

import { CdmsSubmissionFormGroup, CdmsSubmissionFormService } from './cdms-submission-form.service';

@Component({
  selector: 'jhi-cdms-submission-update',
  templateUrl: './cdms-submission-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class CdmsSubmissionUpdate implements OnInit {
  isSaving = false;
  cdmsSubmission: ICdmsSubmission | null = null;
  submissionStatusValues = Object.keys(SubmissionStatus);

  protected cdmsSubmissionService = inject(CdmsSubmissionService);
  protected cdmsSubmissionFormService = inject(CdmsSubmissionFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CdmsSubmissionFormGroup = this.cdmsSubmissionFormService.createCdmsSubmissionFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cdmsSubmission }) => {
      this.cdmsSubmission = cdmsSubmission;
      if (cdmsSubmission) {
        this.updateForm(cdmsSubmission);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cdmsSubmission = this.cdmsSubmissionFormService.getCdmsSubmission(this.editForm);
    if (cdmsSubmission.id === null) {
      this.subscribeToSaveResponse(this.cdmsSubmissionService.create(cdmsSubmission));
    } else {
      this.subscribeToSaveResponse(this.cdmsSubmissionService.update(cdmsSubmission));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICdmsSubmission>>): void {
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

  protected updateForm(cdmsSubmission: ICdmsSubmission): void {
    this.cdmsSubmission = cdmsSubmission;
    this.cdmsSubmissionFormService.resetForm(this.editForm, cdmsSubmission);
  }
}
