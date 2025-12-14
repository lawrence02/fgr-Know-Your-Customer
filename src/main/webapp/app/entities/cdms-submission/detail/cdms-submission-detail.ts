import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { ICdmsSubmission } from '../cdms-submission.model';

@Component({
  selector: 'jhi-cdms-submission-detail',
  templateUrl: './cdms-submission-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class CdmsSubmissionDetail {
  cdmsSubmission = input<ICdmsSubmission | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
