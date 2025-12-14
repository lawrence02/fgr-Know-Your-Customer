import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Data, ParamMap, Router, RouterLink } from '@angular/router';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';

import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { KycConsentDeleteDialog } from '../delete/kyc-consent-delete-dialog';
import { IKycConsent } from '../kyc-consent.model';
import { EntityArrayResponseType, KycConsentService } from '../service/kyc-consent.service';

@Component({
  selector: 'jhi-kyc-consent',
  templateUrl: './kyc-consent.html',
  imports: [RouterLink, FormsModule, SharedModule, SortDirective, SortByDirective, FormatMediumDatetimePipe],
})
export class KycConsent implements OnInit {
  subscription: Subscription | null = null;
  kycConsents = signal<IKycConsent[]>([]);
  isLoading = false;

  sortState = sortStateSignal({});

  readonly router = inject(Router);
  protected readonly kycConsentService = inject(KycConsentService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);

  trackId = (item: IKycConsent): number => this.kycConsentService.getKycConsentIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          if (this.kycConsents().length === 0) {
            this.load();
          } else {
            this.kycConsents.set(this.refineData(this.kycConsents()));
          }
        }),
      )
      .subscribe();
  }

  delete(kycConsent: IKycConsent): void {
    const modalRef = this.modalService.open(KycConsentDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.kycConsent = kycConsent;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend().subscribe((res: EntityArrayResponseType) => this.onResponseSuccess(res));
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(event);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.kycConsents.set(this.refineData(dataFromBody));
  }

  protected refineData(data: IKycConsent[]): IKycConsent[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: IKycConsent[] | null): IKycConsent[] {
    return data ?? [];
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject: any = {
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    return this.kycConsentService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(sortState: SortState): void {
    const queryParamsObj = {
      sort: this.sortService.buildSortParam(sortState),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }
}
