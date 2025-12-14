import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IKycDocument } from '../kyc-document.model';
import { KycDocumentService } from '../service/kyc-document.service';

@Component({
  templateUrl: './kyc-document-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class KycDocumentDeleteDialog {
  kycDocument?: IKycDocument;

  protected kycDocumentService = inject(KycDocumentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.kycDocumentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
