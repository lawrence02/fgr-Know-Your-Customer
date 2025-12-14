import { Component, inject, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { DataUtils } from 'app/core/util/data-util.service';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IKycCase } from '../kyc-case.model';

@Component({
  selector: 'jhi-kyc-case-detail',
  templateUrl: './kyc-case-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class KycCaseDetail {
  kycCase = input<IKycCase | null>(null);

  protected dataUtils = inject(DataUtils);

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    globalThis.history.back();
  }
}
