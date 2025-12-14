import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IKycNotification } from '../kyc-notification.model';

@Component({
  selector: 'jhi-kyc-notification-detail',
  templateUrl: './kyc-notification-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class KycNotificationDetail {
  kycNotification = input<IKycNotification | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
