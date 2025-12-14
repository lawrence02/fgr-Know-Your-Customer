import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IKycConsent } from '../kyc-consent.model';

@Component({
  selector: 'jhi-kyc-consent-detail',
  templateUrl: './kyc-consent-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class KycConsentDetail {
  kycConsent = input<IKycConsent | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
