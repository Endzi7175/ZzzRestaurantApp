import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRestaurantOrderItem, RestaurantOrderItem } from '../restaurant-order-item.model';
import { RestaurantOrderItemService } from '../service/restaurant-order-item.service';
import { IOrderItemNotificationsStatus } from 'app/entities/order-item-notifications-status/order-item-notifications-status.model';
import { OrderItemNotificationsStatusService } from 'app/entities/order-item-notifications-status/service/order-item-notifications-status.service';
import { IRestaurantOrder } from 'app/entities/restaurant-order/restaurant-order.model';
import { RestaurantOrderService } from 'app/entities/restaurant-order/service/restaurant-order.service';
import { OrderItemStatus } from 'app/entities/enumerations/order-item-status.model';

@Component({
  selector: 'jhi-restaurant-order-item-update',
  templateUrl: './restaurant-order-item-update.component.html',
})
export class RestaurantOrderItemUpdateComponent implements OnInit {
  isSaving = false;
  orderItemStatusValues = Object.keys(OrderItemStatus);

  orderItemStatusesCollection: IOrderItemNotificationsStatus[] = [];
  restaurantOrdersSharedCollection: IRestaurantOrder[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    menuItemId: [],
    priceExcludingTax: [],
    priceIncludingTax: [],
    quantity: [],
    status: [],
    orderItemStatus: [],
    restaurantOrder: [],
  });

  constructor(
    protected restaurantOrderItemService: RestaurantOrderItemService,
    protected orderItemNotificationsStatusService: OrderItemNotificationsStatusService,
    protected restaurantOrderService: RestaurantOrderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restaurantOrderItem }) => {
      this.updateForm(restaurantOrderItem);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const restaurantOrderItem = this.createFromForm();
    if (restaurantOrderItem.id !== undefined) {
      this.subscribeToSaveResponse(this.restaurantOrderItemService.update(restaurantOrderItem));
    } else {
      this.subscribeToSaveResponse(this.restaurantOrderItemService.create(restaurantOrderItem));
    }
  }

  trackOrderItemNotificationsStatusById(index: number, item: IOrderItemNotificationsStatus): number {
    return item.id!;
  }

  trackRestaurantOrderById(index: number, item: IRestaurantOrder): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRestaurantOrderItem>>): void {
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

  protected updateForm(restaurantOrderItem: IRestaurantOrderItem): void {
    this.editForm.patchValue({
      id: restaurantOrderItem.id,
      name: restaurantOrderItem.name,
      menuItemId: restaurantOrderItem.menuItemId,
      priceExcludingTax: restaurantOrderItem.priceExcludingTax,
      priceIncludingTax: restaurantOrderItem.priceIncludingTax,
      quantity: restaurantOrderItem.quantity,
      status: restaurantOrderItem.status,
      orderItemStatus: restaurantOrderItem.orderItemStatus,
      restaurantOrder: restaurantOrderItem.restaurantOrder,
    });

    this.orderItemStatusesCollection = this.orderItemNotificationsStatusService.addOrderItemNotificationsStatusToCollectionIfMissing(
      this.orderItemStatusesCollection,
      restaurantOrderItem.orderItemStatus
    );
    this.restaurantOrdersSharedCollection = this.restaurantOrderService.addRestaurantOrderToCollectionIfMissing(
      this.restaurantOrdersSharedCollection,
      restaurantOrderItem.restaurantOrder
    );
  }

  protected loadRelationshipsOptions(): void {
    this.orderItemNotificationsStatusService
      .query({ filter: 'orderitem-is-null' })
      .pipe(map((res: HttpResponse<IOrderItemNotificationsStatus[]>) => res.body ?? []))
      .pipe(
        map((orderItemNotificationsStatuses: IOrderItemNotificationsStatus[]) =>
          this.orderItemNotificationsStatusService.addOrderItemNotificationsStatusToCollectionIfMissing(
            orderItemNotificationsStatuses,
            this.editForm.get('orderItemStatus')!.value
          )
        )
      )
      .subscribe(
        (orderItemNotificationsStatuses: IOrderItemNotificationsStatus[]) =>
          (this.orderItemStatusesCollection = orderItemNotificationsStatuses)
      );

    this.restaurantOrderService
      .query()
      .pipe(map((res: HttpResponse<IRestaurantOrder[]>) => res.body ?? []))
      .pipe(
        map((restaurantOrders: IRestaurantOrder[]) =>
          this.restaurantOrderService.addRestaurantOrderToCollectionIfMissing(restaurantOrders, this.editForm.get('restaurantOrder')!.value)
        )
      )
      .subscribe((restaurantOrders: IRestaurantOrder[]) => (this.restaurantOrdersSharedCollection = restaurantOrders));
  }

  protected createFromForm(): IRestaurantOrderItem {
    return {
      ...new RestaurantOrderItem(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      menuItemId: this.editForm.get(['menuItemId'])!.value,
      priceExcludingTax: this.editForm.get(['priceExcludingTax'])!.value,
      priceIncludingTax: this.editForm.get(['priceIncludingTax'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      status: this.editForm.get(['status'])!.value,
      orderItemStatus: this.editForm.get(['orderItemStatus'])!.value,
      restaurantOrder: this.editForm.get(['restaurantOrder'])!.value,
    };
  }
}
