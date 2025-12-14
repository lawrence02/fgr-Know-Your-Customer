import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IKycCase } from '../kyc-case.model';
import { KycCaseService } from '../service/kyc-case.service';

@Component({
  templateUrl: './kyc-case-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class KycCaseDeleteDialog {
  kycCase?: IKycCase;

  protected kycCaseService = inject(KycCaseService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.kycCaseService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
