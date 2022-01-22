import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OrderItemNotificationsStatusComponent } from './list/order-item-notifications-status.component';
import { OrderItemNotificationsStatusDetailComponent } from './detail/order-item-notifications-status-detail.component';
import { OrderItemNotificationsStatusUpdateComponent } from './update/order-item-notifications-status-update.component';
import { OrderItemNotificationsStatusDeleteDialogComponent } from './delete/order-item-notifications-status-delete-dialog.component';
import { OrderItemNotificationsStatusRoutingModule } from './route/order-item-notifications-status-routing.module';

@NgModule({
  imports: [SharedModule, OrderItemNotificationsStatusRoutingModule],
  declarations: [
    OrderItemNotificationsStatusComponent,
    OrderItemNotificationsStatusDetailComponent,
    OrderItemNotificationsStatusUpdateComponent,
    OrderItemNotificationsStatusDeleteDialogComponent,
  ],
  entryComponents: [OrderItemNotificationsStatusDeleteDialogComponent],
})
export class OrderItemNotificationsStatusModule {}
