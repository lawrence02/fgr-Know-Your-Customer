import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { ICdmsSubmission } from '../cdms-submission.model';
import { CdmsSubmissionService } from '../service/cdms-submission.service';

@Component({
  templateUrl: './cdms-submission-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class CdmsSubmissionDeleteDialog {
  cdmsSubmission?: ICdmsSubmission;

  protected cdmsSubmissionService = inject(CdmsSubmissionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cdmsSubmissionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
