import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { CustomerType } from 'app/entities/enumerations/customer-type.model';
import SharedModule from 'app/shared/shared.module';
import { ICustomer } from '../customer.model';
import { CustomerService } from '../service/customer.service';

import { CustomerFormGroup, CustomerFormService } from './customer-form.service';

@Component({
  selector: 'jhi-customer-update',
  templateUrl: './customer-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class CustomerUpdate implements OnInit {
  isSaving = false;
  customer: ICustomer | null = null;
  customerTypeValues = Object.keys(CustomerType);

  protected customerService = inject(CustomerService);
  protected customerFormService = inject(CustomerFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CustomerFormGroup = this.customerFormService.createCustomerFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customer }) => {
      this.customer = customer;
      if (customer) {
        this.updateForm(customer);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customer = this.customerFormService.getCustomer(this.editForm);
    if (customer.id === null) {
      this.subscribeToSaveResponse(this.customerService.create(customer));
    } else {
      this.subscribeToSaveResponse(this.customerService.update(customer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomer>>): void {
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

  protected updateForm(customer: ICustomer): void {
    this.customer = customer;
    this.customerFormService.resetForm(this.editForm, customer);
  }
}
