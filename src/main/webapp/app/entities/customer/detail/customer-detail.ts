import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { ICustomer } from '../customer.model';

@Component({
  selector: 'jhi-customer-detail',
  templateUrl: './customer-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CustomerDetail {
  customer = input<ICustomer | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
