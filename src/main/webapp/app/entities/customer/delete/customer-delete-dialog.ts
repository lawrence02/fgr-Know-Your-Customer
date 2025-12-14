import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { ICustomer } from '../customer.model';
import { CustomerService } from '../service/customer.service';

@Component({
  templateUrl: './customer-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class CustomerDeleteDialog {
  customer?: ICustomer;

  protected customerService = inject(CustomerService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.customerService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
