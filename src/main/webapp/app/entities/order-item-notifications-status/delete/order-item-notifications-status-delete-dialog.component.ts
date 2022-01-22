import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrderItemNotificationsStatus } from '../order-item-notifications-status.model';
import { OrderItemNotificationsStatusService } from '../service/order-item-notifications-status.service';

@Component({
  templateUrl: './order-item-notifications-status-delete-dialog.component.html',
})
export class OrderItemNotificationsStatusDeleteDialogComponent {
  orderItemNotificationsStatus?: IOrderItemNotificationsStatus;

  constructor(protected orderItemNotificationsStatusService: OrderItemNotificationsStatusService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.orderItemNotificationsStatusService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
