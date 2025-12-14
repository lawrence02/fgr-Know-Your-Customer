import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IKycNotification } from '../kyc-notification.model';
import { KycNotificationService } from '../service/kyc-notification.service';

@Component({
  templateUrl: './kyc-notification-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class KycNotificationDeleteDialog {
  kycNotification?: IKycNotification;

  protected kycNotificationService = inject(KycNotificationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.kycNotificationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
