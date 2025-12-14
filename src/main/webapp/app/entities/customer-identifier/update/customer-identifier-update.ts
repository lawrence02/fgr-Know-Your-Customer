import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { ChannelType } from 'app/entities/enumerations/channel-type.model';
import { IdentifierType } from 'app/entities/enumerations/identifier-type.model';
import SharedModule from 'app/shared/shared.module';
import { ICustomerIdentifier } from '../customer-identifier.model';
import { CustomerIdentifierService } from '../service/customer-identifier.service';

import { CustomerIdentifierFormGroup, CustomerIdentifierFormService } from './customer-identifier-form.service';

@Component({
  selector: 'jhi-customer-identifier-update',
  templateUrl: './customer-identifier-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class CustomerIdentifierUpdate implements OnInit {
  isSaving = false;
  customerIdentifier: ICustomerIdentifier | null = null;
  identifierTypeValues = Object.keys(IdentifierType);
  channelTypeValues = Object.keys(ChannelType);

  customersSharedCollection = signal<ICustomer[]>([]);

  protected customerIdentifierService = inject(CustomerIdentifierService);
  protected customerIdentifierFormService = inject(CustomerIdentifierFormService);
  protected customerService = inject(CustomerService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CustomerIdentifierFormGroup = this.customerIdentifierFormService.createCustomerIdentifierFormGroup();

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customerIdentifier }) => {
      this.customerIdentifier = customerIdentifier;
      if (customerIdentifier) {
        this.updateForm(customerIdentifier);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customerIdentifier = this.customerIdentifierFormService.getCustomerIdentifier(this.editForm);
    if (customerIdentifier.id === null) {
      this.subscribeToSaveResponse(this.customerIdentifierService.create(customerIdentifier));
    } else {
      this.subscribeToSaveResponse(this.customerIdentifierService.update(customerIdentifier));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomerIdentifier>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(customerIdentifier: ICustomerIdentifier): void {
    this.customerIdentifier = customerIdentifier;
    this.customerIdentifierFormService.resetForm(this.editForm, customerIdentifier);

    this.customersSharedCollection.set(
      this.customerService.addCustomerToCollectionIfMissing<ICustomer>(this.customersSharedCollection(), customerIdentifier.customer),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.customerIdentifier?.customer),
        ),
      )
      .subscribe((customers: ICustomer[]) => this.customersSharedCollection.set(customers));
  }
}
