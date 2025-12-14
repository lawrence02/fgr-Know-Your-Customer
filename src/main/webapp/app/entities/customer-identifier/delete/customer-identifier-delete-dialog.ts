import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { ICustomerIdentifier } from '../customer-identifier.model';
import { CustomerIdentifierService } from '../service/customer-identifier.service';

@Component({
  templateUrl: './customer-identifier-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class CustomerIdentifierDeleteDialog {
  customerIdentifier?: ICustomerIdentifier;

  protected customerIdentifierService = inject(CustomerIdentifierService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.customerIdentifierService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
