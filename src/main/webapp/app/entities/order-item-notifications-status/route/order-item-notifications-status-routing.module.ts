import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrderItemNotificationsStatusComponent } from '../list/order-item-notifications-status.component';
import { OrderItemNotificationsStatusDetailComponent } from '../detail/order-item-notifications-status-detail.component';
import { OrderItemNotificationsStatusUpdateComponent } from '../update/order-item-notifications-status-update.component';
import { OrderItemNotificationsStatusRoutingResolveService } from './order-item-notifications-status-routing-resolve.service';

const orderItemNotificationsStatusRoute: Routes = [
  {
    path: '',
    component: OrderItemNotificationsStatusComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrderItemNotificationsStatusDetailComponent,
    resolve: {
      orderItemNotificationsStatus: OrderItemNotificationsStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrderItemNotificationsStatusUpdateComponent,
    resolve: {
      orderItemNotificationsStatus: OrderItemNotificationsStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrderItemNotificationsStatusUpdateComponent,
    resolve: {
      orderItemNotificationsStatus: OrderItemNotificationsStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(orderItemNotificationsStatusRoute)],
  exports: [RouterModule],
})
export class OrderItemNotificationsStatusRoutingModule {}
