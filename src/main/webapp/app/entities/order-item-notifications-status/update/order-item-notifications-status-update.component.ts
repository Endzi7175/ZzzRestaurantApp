import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IOrderItemNotificationsStatus, OrderItemNotificationsStatus } from '../order-item-notifications-status.model';
import { OrderItemNotificationsStatusService } from '../service/order-item-notifications-status.service';

@Component({
  selector: 'jhi-order-item-notifications-status-update',
  templateUrl: './order-item-notifications-status-update.component.html',
})
export class OrderItemNotificationsStatusUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    created: [],
    accepted: [],
    prepared: [],
    served: [],
    canceled: [],
  });

  constructor(
    protected orderItemNotificationsStatusService: OrderItemNotificationsStatusService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderItemNotificationsStatus }) => {
      if (orderItemNotificationsStatus.id === undefined) {
        const today = dayjs().startOf('day');
        orderItemNotificationsStatus.created = today;
        orderItemNotificationsStatus.accepted = today;
        orderItemNotificationsStatus.prepared = today;
        orderItemNotificationsStatus.served = today;
        orderItemNotificationsStatus.canceled = today;
      }

      this.updateForm(orderItemNotificationsStatus);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orderItemNotificationsStatus = this.createFromForm();
    if (orderItemNotificationsStatus.id !== undefined) {
      this.subscribeToSaveResponse(this.orderItemNotificationsStatusService.update(orderItemNotificationsStatus));
    } else {
      this.subscribeToSaveResponse(this.orderItemNotificationsStatusService.create(orderItemNotificationsStatus));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrderItemNotificationsStatus>>): void {
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

  protected updateForm(orderItemNotificationsStatus: IOrderItemNotificationsStatus): void {
    this.editForm.patchValue({
      id: orderItemNotificationsStatus.id,
      created: orderItemNotificationsStatus.created ? orderItemNotificationsStatus.created.format(DATE_TIME_FORMAT) : null,
      accepted: orderItemNotificationsStatus.accepted ? orderItemNotificationsStatus.accepted.format(DATE_TIME_FORMAT) : null,
      prepared: orderItemNotificationsStatus.prepared ? orderItemNotificationsStatus.prepared.format(DATE_TIME_FORMAT) : null,
      served: orderItemNotificationsStatus.served ? orderItemNotificationsStatus.served.format(DATE_TIME_FORMAT) : null,
      canceled: orderItemNotificationsStatus.canceled ? orderItemNotificationsStatus.canceled.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IOrderItemNotificationsStatus {
    return {
      ...new OrderItemNotificationsStatus(),
      id: this.editForm.get(['id'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      accepted: this.editForm.get(['accepted'])!.value ? dayjs(this.editForm.get(['accepted'])!.value, DATE_TIME_FORMAT) : undefined,
      prepared: this.editForm.get(['prepared'])!.value ? dayjs(this.editForm.get(['prepared'])!.value, DATE_TIME_FORMAT) : undefined,
      served: this.editForm.get(['served'])!.value ? dayjs(this.editForm.get(['served'])!.value, DATE_TIME_FORMAT) : undefined,
      canceled: this.editForm.get(['canceled'])!.value ? dayjs(this.editForm.get(['canceled'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
