import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { ICustomerIdentifier } from '../customer-identifier.model';

@Component({
  selector: 'jhi-customer-identifier-detail',
  templateUrl: './customer-identifier-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class CustomerIdentifierDetail {
  customerIdentifier = input<ICustomerIdentifier | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
