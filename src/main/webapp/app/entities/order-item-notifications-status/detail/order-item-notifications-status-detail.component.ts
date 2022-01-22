import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrderItemNotificationsStatus } from '../order-item-notifications-status.model';

@Component({
  selector: 'jhi-order-item-notifications-status-detail',
  templateUrl: './order-item-notifications-status-detail.component.html',
})
export class OrderItemNotificationsStatusDetailComponent implements OnInit {
  orderItemNotificationsStatus: IOrderItemNotificationsStatus | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderItemNotificationsStatus }) => {
      this.orderItemNotificationsStatus = orderItemNotificationsStatus;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
